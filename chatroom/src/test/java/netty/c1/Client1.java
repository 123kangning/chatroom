package netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Random;

public class Client1 {
    public static void main(String[] args) {
        send();
    }

    public static void send() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buf = ctx.alloc().buffer();
                                char c = 'a';
                                Random random = new Random();
                                for (int i = 0; i < 10; i++) {
                                    ByteBuf buf1 = fill(c, random.nextInt(10) + 1);
                                    buf.writeBytes(buf1);
                                    c++;
                                }
                                ctx.writeAndFlush(buf);
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress(8010));


    }

    public static ByteBuf fill(char c, int len) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(16);
        for (int i = 0; i < len; i++) {
            buf.writeByte(c);
        }
        for (int i = len; i < 16; i++) {
            buf.writeByte('_');
        }
        return buf;
    }
}
