package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import lombok.extern.slf4j.Slf4j;
import message.LoginRequestMessage;
import message.ResponseMessage;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;
import server.handler.LoginHandler;
import server.handler.LogoutHandler;
import server.handler.SignInHandler;
import server.handler.SignOutHandler;

import java.sql.*;
import java.util.Set;

@Slf4j
public class ChatServer {

    public static Connection connection;
    public static void jdbc(){
        final String URL="jdbc:mysql://localhost:3306/test";
        final String NAME="root";
        final String PASSWORD="9264wkn.";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL,NAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        jdbc();
        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup worker=new NioEventLoopGroup();
        LoggingHandler Log=new LoggingHandler(LogLevel.DEBUG);
//        MessageCodec codec=new MessageCodec();

        ChannelFuture future;
        try {
            future=new ServerBootstrap()
                    .group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast( new ProtocolFrameDecoder())
                                    /*.addLast(Log)*/
                                    .addLast(new MessageCodec())
                                    .addLast(new LoginHandler())
                                    .addLast(new LogoutHandler())
                                    .addLast(new SignOutHandler())
                                    .addLast(new SignInHandler());
                        }
                    })
                    .bind(8084);
            Channel channel=future.sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
