package ChannelFile;

import io.netty.bootstrap.ServerBootstrap;
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



    public static void main(String[] args) {
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
                            nioSocketChannel.pipeline()
                                    .addLast(new ChannelInboundHandlerAdapter(){
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            String s="测试是否传递";
                                            super.channelRead(ctx, s);
                                        }
                                    })
                                    .addLast(new SimpleChannelInboundHandler<String>() {

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                            System.out.println("s = "+msg);
                                        }
                                    });
                        }
                    })
                    .bind(8088);
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
