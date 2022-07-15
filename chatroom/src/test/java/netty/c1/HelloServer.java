package netty.c1;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        //1.启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                /*2.EventLoop(Selector,thread)循环处理事件，group 组，EventLoop理解为一个工人，他管理多个Channel,且需要和Channel绑定，
                只有在不进行IO操作时，才可以为handler指定不同的工人*/
                .group(new NioEventLoopGroup())//accept read
                //3.选择 服务器的ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                //4.boss 负责处理连接 worker(child)负责处理读写，决定了worker(child) 能执行哪些操作（handler）
                .childHandler(//handler理解为一道道处理工序，合在一起就是pipeline
                        //5.channel代表和客户端进行数据读写的的通道 Initializer是初始化器，负责添加别的 handler
                        new ChannelInitializer<NioSocketChannel>(){

                    @Override //accept 连接建立后调用initChannel方法
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //6.添加具体handler,即一道流水线工具
                        nioSocketChannel.pipeline().addLast(new StringDecoder());//将ByteBuf转换为字符串
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {//自定义handler
                            @Override //读事件
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //打印上一步转换好的字符串
                                System.out.println(msg);
                            }
                        });
                    }
                }).bind(8080);//ServerSocket启动后绑定的监听端口

    }
}
