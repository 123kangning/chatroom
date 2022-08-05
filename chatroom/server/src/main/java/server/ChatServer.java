package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import message.LogoutRequestMessage;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;
import server.handler.*;
import server.session.SessionMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ChatServer {

    public static Connection connection;

    public static void jdbc() {
        final String URL = "jdbc:mysql://localhost:3306/chatroom";
        final String NAME = "root";
        final String PASSWORD = "9264wkn.";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        jdbc();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler Log = new LoggingHandler(LogLevel.DEBUG);
//        MessageCodec codec=new MessageCodec();

        ChannelFuture future;
        try {
            future = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            log.info("channel = " + nioSocketChannel);
                            nioSocketChannel.pipeline().addLast(new ProtocolFrameDecoder())
                                    .addLast(new MessageCodec())
                                    /*.addLast(Log)*/
                                    .addLast(new IdleStateHandler(15, 0, 0))
                                    .addLast(new ChannelDuplexHandler() {
                                        @Override
                                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            if (event.state() == IdleState.READER_IDLE) {

                                            }
                                            super.userEventTriggered(ctx, evt);
                                        }
                                    })
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                            log.info("exceptionCaught start");
                                            log.info(cause.toString());

                                            //LogoutRequestMessage msg = new LogoutRequestMessage();
                                            log.info("exceptionCaught end");

                                        }

                                        @Override
                                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                            log.info("channelInactive");
                                            if (SessionMap.getUser(ctx.channel()) != 0) {
                                                LogoutRequestMessage msg = new LogoutRequestMessage();
                                                super.channelRead(ctx, msg);
                                            }
                                        }
                                    })
                                    .addLast(new LoginHandler())
                                    .addLast(new LogoutHandler())
                                    .addLast(new SignOutHandler())
                                    .addLast(new ReceiveFileHandler())
                                    .addLast(new FriendChatHandler())
                                    .addLast(new NoticeHandler())
                                    .addLast(new SendApplyHandler())
                                    .addLast(new FriendQueryHandler())
                                    .addLast(new FriendAddHandler())
                                    .addLast(new FriendDeleteHandler())
                                    .addLast(new FriendShieldHandler())
                                    .addLast(new FriendUnShiedHandler())
                                    .addLast(new FriendNoticeHandler())
                                    .addLast(new FriendGetFileHandler())
                                    .addLast(new FriendApplyQueryHandler())
                                    .addLast(new GroupCreateHandler())
                                    .addLast(new GroupQueryHandler())
                                    .addLast(new GroupJoinHandler())
                                    .addLast(new GroupCheckGradeHandler())
                                    .addLast(new GroupMemberHandler())
                                    .addLast(new GroupUnSayHandler())
                                    .addLast(new GroupQuitHandler())
                                    .addLast(new GroupApplyQueryHandler())
                                    .addLast(new GroupCutManagerHandler())
                                    .addLast(new GroupAddManagerHandler())
                                    .addLast(new ReceiveMessageHandler())
                                    .addLast(new GroupDeleteHandler())
                                    .addLast(new GroupNoticeHandler())
                                    .addLast(new GroupChatHandler())
                                    .addLast(new SignInHandler())
                                    .addLast(new ChangePasswordHandler())
                                    .addLast(new SendAuthCodeHandler())
                                    .addLast(new SearchPasswordHandler());
                        }
                    })
                    .bind(8080);
            Channel channel = future.sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
