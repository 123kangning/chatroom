package ChannelFile;

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

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


@Slf4j
public class ChatClient {
    public static final Object waitMessage = new Object();
    public static volatile int waitSuccess = 0;
    public static volatile List<String> friendList;
    public static volatile String haveFile = "";


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler Log = new LoggingHandler(LogLevel.DEBUG);
        MessageCodec clientCodec = new MessageCodec();

        try {
            ChannelFuture future = new Bootstrap()
                    .group(group)
                    /*.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)*/
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) {
                            nioSocketChannel.pipeline()
                                    /*.addLast(Log)*/
                                    .addLast(new ProtocolFrameDecoder())
                                    .addLast(clientCodec)

                                    .addLast(new ResponseHandler())
                                    .addLast("View handler", new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) {
                                            Scanner scanner=new Scanner(System.in);
                                            File file;
                                            System.out.println("请输入待发送的文件的[绝对路径]：");
                                            file = new File(scanner.nextLine());
                                            while (!file.exists() || !file.isFile()) {
                                                if (!file.exists()) {
                                                    System.out.println("文件不存在，请重新输入待发送的文件的[绝对路径]");
                                                } else {
                                                    System.out.println("不是文件，请重新输入待发送的文件的[绝对路径]");
                                                }
                                                file = new File(scanner.nextLine());
                                            }

                                        }
                                    });
                        }
                    })
                    .connect(new InetSocketAddress("localhost", 8080));
            Channel channel = future.sync().channel();

            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
