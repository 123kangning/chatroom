package netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        //1.客户端启动器类
        ChannelFuture channelFuture=new Bootstrap()
                //2.添加 EventLoop
                .group(new NioEventLoopGroup())
                //3.选择客户端channel实现
                .channel(NioSocketChannel.class)
                //4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override //在连接建立后被调用
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());//把hello转为ByteBuf
                    }
                })
                //5.连接到服务器
                //connect 异步非阻塞，另一个线程（NIO线程）去执行connect,当前线程没有进行等待（）无阻塞向下执行，需要使用sync()方法等待connect方法执行成功
                .connect(new InetSocketAddress("localhost",8080));
        //￥使用sync方法同步处理i结果
        /*channelFuture.sync();//阻塞方法，直到连接建立
        Channel channel=channelFuture.channel();//代表连接对象，如果没有sync()方法，这个channel就是一个无用的channel
        //log.debug("{}",channel);
        //6. 向服务器发送数据
        channel.writeAndFlush("hello world");
        System.out.println("发送！！！");*/
        //￥使用addListener 异步处理结果,connect连接建立好之后，由NIO线程执行operationComplete方法
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

                Channel channel=channelFuture.channel();//代表连接对象，如果没有sync()方法，这个channel就是一个无用的channel
                log.debug("{}",channel);
                channel.writeAndFlush("hello");


            }
        });
        //Thread.sleep(10000);

    }
}
