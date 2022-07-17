package netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.nativeimage.ImageSingletons;

import java.nio.charset.Charset;

@Slf4j
public class TestPipeline {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>(){

                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        log.info("start");
                        //1.通过Channel拿到Pipeline
                        ChannelPipeline pipeline= nioSocketChannel.pipeline();
                        //2.添加处理器 ，netty会自动添加两个handler （head and last）
                        pipeline.addLast("h1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("1");
                                /*ByteBuf buf=(ByteBuf) msg;
                                String s=buf.toString(Charset.defaultCharset());*/
                                super.channelRead(ctx,msg);
                            }
                        }).addLast("h2",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("2");
//                                Student student=new Student((String)msg);
                                super.channelRead(ctx,msg);

                            }
                        }).addLast("h3",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("3 ");
                                //ctx.writeAndFlush方法，从当前handler出发，向前寻找出站处理器
//                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("hello".getBytes()));
                                //Channel.writeAndFlush方法，从尾部handler出发向前寻找出站处理器
                                nioSocketChannel.writeAndFlush(msg);
                                //作用：唤醒下一个入站处理器，这条语句已经没有意义了
                                //super.channelRead(ctx,msg);

                            }
                        }).addLast("h4",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("4");
                                super.write(ctx, msg, promise);
                            }
                        }).addLast("h5",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("5");
                                super.write(ctx, msg, promise);
                            }
                        }).addLast("h6",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8080);
    }

    static class Student{
        private String name;
        public Student(String name){
            this.name=name;
        }

        @Override
        public String toString() {
            return "name = "+this.name;
        }
    }
}
