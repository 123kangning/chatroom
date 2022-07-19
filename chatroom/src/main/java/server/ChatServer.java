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
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL,NAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
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
                                    .addLast(Log)
                                    .addLast(new MessageCodec())
                                    .addLast(new SimpleChannelInboundHandler<LoginRequestMessage>(){

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws SQLException {
                                            long userID=msg.getUserID();
                                            String password=msg.getPassword();
                                            log.info("{}",msg);
                                            System.out.println("#############");
                                            ResponseMessage message;
                                            String sql="select online from test1 where userID=? and password=? ";
                                            PreparedStatement statement=connection.prepareStatement(sql);
                                            statement.setLong(1,userID);
                                            statement.setObject(2,password);
                                            ResultSet set= statement.executeQuery();
                                            if(set==null){
                                                log.info("set == null,没找到");
                                            }else{
                                                log.info("next 不为空！！！！！！！！！！");
                                            }
                                            if(set.next()&&set.getString(1).equals("F")){
                                                log.info("登录成功");
                                            }else{
                                                log.info("登录失败");
                                            }
                                            System.out.println("#############");
                                            //ctx.writeAndFlush(message);
                                        }
                                    });
                        }
                    })
                    .bind(8081);
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
