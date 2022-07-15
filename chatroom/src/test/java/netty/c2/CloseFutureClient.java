package netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group=new NioEventLoopGroup();
        ChannelFuture channelFuture=new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost",8080);
        Channel channel=channelFuture.sync().channel();
        //log.debug("{}",channel);
        new Thread(()->{
            Scanner scanner=new Scanner(System.in);
            while(true){
                String line=scanner.nextLine();
                if("q".equals(line)){
                    channel.close(); // close方法还是一个异步操作！！！

                    break;
                }
                channel.writeAndFlush(line);
            }
        },"input").start();
        //获取CloseFuture 对象，1）同步处理关闭，2）异步处理关闭
        ChannelFuture closeFuture=channel.closeFuture();
        /*System.out.println("waiting close...");
        closeFuture.sync();
        log.debug("关闭之后的操作");*/
        closeFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            log.debug("关闭之后的操作");
            //优雅地关闭
            group.shutdownGracefully();
        });
    }
}
