package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import message.LoginRequestMessage;
import message.ResponseMessage;
import protocol.MessageCodec;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;



public class ChatClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group=new NioEventLoopGroup();
        LoggingHandler log=new LoggingHandler(LogLevel.DEBUG);
        MessageCodec clientCodec=new MessageCodec();

        ChannelFuture future=new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(log)
                                .addLast(clientCodec)
                                .addLast(new SimpleChannelInboundHandler<ResponseMessage>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage) throws Exception {
                                        boolean success=responseMessage.getSuccess();
                                        String reason=responseMessage.getReason();
                                        if(success){
                                            System.out.print("操作成功， ");
                                        }else{
                                            System.out.print("操作失败， ");
                                        }
                                        System.out.println(reason);
                                    }
                                })
                                .addLast("client handler",new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        boolean success=((ResponseMessage)msg).getSuccess();
                                        String reason=((ResponseMessage)msg).getReason();
                                        if(success){
                                            System.out.print("操作成功， ");
                                        }else{
                                            System.out.print("操作失败， ");
                                        }
                                        System.out.println(reason);
                                        super.channelRead(ctx, msg);
                                    }

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                        new Thread(()->{
                                            ByteBuf buf=ctx.alloc().buffer();
                                            System.out.println("\t+-----------------+");
                                            System.out.println("\t|   1   登录       |");
                                            System.out.println("\t+-----------------+");
                                            System.out.println("\t|   2   注册       |");
                                            System.out.println("\t+-----------------+");
                                            System.out.println("\t|   0   退出       |");
                                            System.out.println("\t+-----------------+\n");
                                            System.out.println("输入你的选择：");
                                            Scanner scanner=new Scanner(System.in);
                                            switch (Integer.parseInt(scanner.nextLine())){
                                                case 1:
                                                    //Scanner scanner1=new Scanner(System.in);
                                                    System.out.println("请输入用户ID：");
                                                    long username= Long.parseLong(scanner.nextLine());
                                                    System.out.println("请输入密码：");
                                                    String password=scanner.nextLine();
                                                    System.out.println("username="+username+", password="+password);

                                                    LoginRequestMessage message=new LoginRequestMessage(username,password);
                                                    ctx.writeAndFlush(message);
                                                    try {
                                                        System.in.read();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case 2:
                                                    break;
                                                case 0:
                                                    return;
                                            }

                                        },"system.in").start();
                                        super.channelActive(ctx);
                                    }
                                });
                    }
                })
                .connect(new InetSocketAddress("localhost",8081));
        Channel channel=future.sync().channel();
        channel.closeFuture().sync();
    }
}
