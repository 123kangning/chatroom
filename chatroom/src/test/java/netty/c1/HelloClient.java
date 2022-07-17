package netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1.客户端启动器类
        new Bootstrap()
                //2.添加 EventLoop
                .group(new NioEventLoopGroup())
                //3.选择客户端channel实现
                .channel(NioSocketChannel.class)
                //4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override //在连接建立后被调用
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                for(int i=0;i<10;i++){
                                    ByteBuf buf=ctx.alloc().buffer();
                                    buf.writeBytes(new byte[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'});
                                    ctx.writeAndFlush(buf);
                                }
                            }
                        });//把hello转为ByteBuf
                    }
                })
                //5.连接到服务器
                .connect(new InetSocketAddress("localhost",8080));
    }
}
