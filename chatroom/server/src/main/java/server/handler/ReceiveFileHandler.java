package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import message.FileResponseMessage;
import message.FriendChatRequestMessage;
import message.GroupChatRequestMessage;

import java.io.*;

@Slf4j
public class ReceiveFileHandler extends ChannelInboundHandlerAdapter {
    private static final ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();
    private static int sum=0;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FriendChatRequestMessage){
            log.info("msg instanceof FriendChatRequestMessage");
            FriendChatRequestMessage friendMsg = (FriendChatRequestMessage) msg;
            if(friendMsg.getFile()==null){
                super.channelRead(ctx, msg);
                return;
            }
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(friendMsg.getFile());
            byte[] bytes=bos.toByteArray();
            buf.writeBytes(bytes);
            int length=bytes.length;
            sum+=length;
            log.info("length = {} ,sum = {}",length,sum);
            if(length>0){//说明文件可能还为接收完毕，返回读取的字节数
                ctx.writeAndFlush(new FileResponseMessage(sum));
            }else{//接收完毕
                log.info("接收完毕");
                ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(buf.array()));
                friendMsg.setFile((File)ois.readObject());
                super.channelRead(ctx, friendMsg);
            }
        }
        if(msg instanceof GroupChatRequestMessage){
            log.info("msg instanceof GroupChatRequestMessage");
            GroupChatRequestMessage groupMsg = (GroupChatRequestMessage) msg;
            if(groupMsg.getFile()==null){
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
                ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(buf.array()));
                groupMsg.setFile((File)ois.readObject());
                super.channelRead(ctx, groupMsg);
            }
        }
        /*super.channelRead(ctx, msg);*/
    }
}
