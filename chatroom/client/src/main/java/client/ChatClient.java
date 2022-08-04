package client;

import client.view.EnterView;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import message.PingMessage;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;


@Slf4j
public class ChatClient {

    public static String myUsername;
    public static int myUserID;
    public static volatile int mailAuthCode;
    public static volatile int haveNoRead = 0;//默认没有未读消息
    public static final Object waitMessage = new Object();//服务端消息返回时，notify线程 View handler
    public static final Object waitRVFile = new Object();
    public static volatile String checkRECV = "n";
    public static volatile boolean isY = false;
    public static volatile int waitSuccess = 0;//1表示消息成功、0表示消息失败
    public static volatile Map<String, List<String>> noticeMap;
    public static volatile List<String> friendList;//查询朋友列表
    public static volatile int gradeInGroup = 0;
    public static volatile boolean immediate = false;
    public static volatile boolean immediateGroup = false;
    public static volatile String haveFile = "";
    public static volatile int talkWith = 0;//正与哪个朋友聊天
    public static volatile int talkWithGroup = 0;//正在哪个群聊聊天

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler Log = new LoggingHandler(LogLevel.DEBUG);
        MessageCodec clientCodec = new MessageCodec();

        try {
            ChannelFuture future = new Bootstrap()
                    .group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) {
                            nioSocketChannel.pipeline()
                                    /*.addLast(Log)*/
                                    .addLast(new ProtocolFrameDecoder())
                                    .addLast(clientCodec)
                                    .addLast(new IdleStateHandler(0, 7, 0))
                                    .addLast(new ChannelDuplexHandler() {
                                        @Override
                                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            if (event.state() == IdleState.WRITER_IDLE) {
                                                ctx.writeAndFlush(new PingMessage());
                                            }
                                            super.userEventTriggered(ctx, evt);
                                        }
                                    })
                                    .addLast(new ResponseHandler())
                                    .addLast(new ClientFriendChatHandler())
                                    .addLast("View handler", new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) {

                                            new Thread(() -> {
                                                new EnterView(ctx);
                                            }, "system.in").start();
                                        }
                                    });
                        }
                    })
                    .connect(new InetSocketAddress("192.168.30.213", 8080));
            Channel channel = future.sync().channel();

            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
