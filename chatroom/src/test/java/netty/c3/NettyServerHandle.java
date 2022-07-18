package netty.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;
public class NettyServerHandle implements ChannelInboundHandler {
    /**
     * ??????
     *
     * @param ctx ???????
     * @param msg ??
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws
            Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("???????:" +
                byteBuf.toString(CharsetUtil.UTF_8));
    }
    /**
     * ??????
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {ctx.writeAndFlush(Unpooled.copiedBuffer("??,??Netty???.",
            CharsetUtil.UTF_8));
    }
    /**
     * ??????
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
    {
    }
    /**
     * ??????
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws
            Exception {
    }
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws
            Exception {
    }@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }
}