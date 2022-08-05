package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import message.FileResponseMessage;
import message.FriendChatRequestMessage;
import message.GroupChatRequestMessage;

import java.io.*;

@Slf4j
public class ReceiveFileHandler extends ChannelInboundHandlerAdapter {
    private static FriendChatRequestMessage friendMsg;
    private static GroupChatRequestMessage groupMsg;
    private static final ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();
    private static int sum=0;
    private static int size,rate;
    private static boolean receive=true;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FriendChatRequestMessage){
            log.info("msg instanceof FriendChatRequestMessage");
            if(receive){
                friendMsg = (FriendChatRequestMessage) msg;
                size=friendMsg.getFileSize();
                rate=0;
                receive=false;
            }
            if(friendMsg.getFile()==null){
                receive=true;
                super.channelRead(ctx, msg);
                return;
            }
            byte[] bytes=friendMsg.getFile();
            buf.writeBytes(bytes);
            int length=bytes.length;
            sum+=length;
            log.info("length = {} ,sum = {}",length,sum);
            int current=(int)((sum*1.0/size)*100);

            if(sum<size){//说明文件可能还未接收完毕，返回读取的字节数
                if(current>rate){
                    ctx.writeAndFlush(new FileResponseMessage(sum));
                    rate=current;
                }
                log.info("接收尚未结束");
            }else{//接收完毕
                log.info("接收完毕");
                receive=true;
                friendMsg.setFile(ByteBufUtil.getBytes(buf));
                sum=0;
                super.channelRead(ctx, friendMsg);
            }
        }
        else if(msg instanceof GroupChatRequestMessage){
            log.info("msg instanceof GroupChatRequestMessage");
            if(receive){
                groupMsg = (GroupChatRequestMessage) msg;
                size=groupMsg.getFileSize();
                receive=false;
            }

            if(groupMsg.getFile()==null){
                receive=true;
                super.channelRead(ctx, msg);
                return;
            }
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(groupMsg.getFile());
            byte[] bytes=bos.toByteArray();
            buf.writeBytes(bytes);
            int length=bytes.length;
            sum+=length;
            log.info("length = {} ,sum = {}",length,sum);
            if(length>0){//说明文件可能还为接收完毕，返回读取的字节数
                ctx.writeAndFlush(new FileResponseMessage(sum));
            }else{//接收完毕
                log.info("接收完毕");
                receive=true;
                groupMsg.setFile(buf.array());
                sum=0;
                super.channelRead(ctx, groupMsg);
            }
        }
        else{
            super.channelRead(ctx, msg);
        }
    }
}
