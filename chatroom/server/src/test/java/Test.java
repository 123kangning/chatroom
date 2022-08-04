import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import message.PingMessage;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;
import message.*;

import java.net.InetSocketAddress;

public class Test {
    public static void main(String[] args) throws InterruptedException{
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler Log = new LoggingHandler(LogLevel.DEBUG);
        MessageCodec clientCodec = new MessageCodec();

        try {
            ChannelFuture future = new Bootstrap()
                    .group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws InterruptedException {
                            nioSocketChannel.pipeline()
                            .addLast(Log)
                                    .addLast(new ChannelInboundHandlerAdapter(){

                                    });
                        }
                    })
                    .connect(new InetSocketAddress("192.168.30.213", 8080));
            Channel channel = future.sync().channel();

            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
