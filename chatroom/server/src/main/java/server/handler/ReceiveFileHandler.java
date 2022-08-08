package server.handler;

import cn.hutool.core.date.DateTime;
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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

@Slf4j
public class ReceiveFileHandler extends ChannelInboundHandlerAdapter {
    private static FriendChatRequestMessage friendMsg;
    private static GroupChatRequestMessage groupMsg;
    private static RandomAccessFile file;
    private String path;
    private static int start=0;
    private static int size,rate,length;
    private static boolean receive=true;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DateTime dateTime=new DateTime(System.currentTimeMillis());
        if(msg instanceof FriendChatRequestMessage){
            //log.info("msg instanceof FriendChatRequestMessage");

            if(receive){
                friendMsg = (FriendChatRequestMessage) msg;
                size=friendMsg.getFileSize();
                path= System.getProperty("user.dir") + "/server/story/" + friendMsg.getFileName() + dateTime;
                friendMsg.setPath(path);
                file=new RandomAccessFile(new File(path),"rw");
                rate=0;
                receive=false;
            }
            if(((FriendChatRequestMessage) msg).getFile()==null){
                receive=true;
                super.channelRead(ctx, msg);
                return;
            }

            byte[] bytes=((FriendChatRequestMessage) msg).getFile();
            length=bytes.length;
            start+=length;
            if(start>size){
                bytes=Arrays.copyOfRange(bytes,0,length+size-start);
            }
            file.write(bytes);

            log.info("length = {} ,start = {},size={},real={}",length,start,size,bytes.length);
            int current=(int)((start*1.0/size)*100);

            if(start<size){//说明文件可能还未接收完毕，返回读取的字节数
                if(current>rate){
                    ctx.writeAndFlush(new FileResponseMessage(start));
                    rate=current;
                    //log.info("rate=current");
                }
                //log.info("接收尚未结束");
            }else{//接收完毕
                log.info("接收完毕");
                receive=true;
                ctx.writeAndFlush(new FileResponseMessage(start));
                start=0;
                file.close();
                super.channelRead(ctx, friendMsg);
            }
        }
        else if(msg instanceof GroupChatRequestMessage){
            //log.info("msg instanceof GroupChatRequestMessage");

            if(receive){
                groupMsg = (GroupChatRequestMessage) msg;
                size=groupMsg.getFileSize();
                path= System.getProperty("user.dir") + "/server/story/" + groupMsg.getFileName() + dateTime;
                groupMsg.setPath(path);
                file=new RandomAccessFile(path,"rw");
                rate=0;
                receive=false;
            }

            if(((GroupChatRequestMessage) msg).getFile()==null){
                receive=true;
                super.channelRead(ctx, msg);
                return;
            }
            byte[] bytes=((GroupChatRequestMessage) msg).getFile();
            length=bytes.length;
            start+=length;

            if(start>size){
                bytes= Arrays.copyOfRange(bytes,0,length+size-start);
            }
            file.write(bytes);
            //log.info("length = {} ,start = {},size={},real={}",length,start,size,bytes.length);
            int current=(int)((start*1.0/size)*100);

            if(start<size){//说明文件可能还未接收完毕，返回读取的字节数
                if(current>rate){
                    ctx.writeAndFlush(new FileResponseMessage(start));
                    rate=current;
                }
                //log.info("接收尚未结束");
            }else{//接收完毕
                log.info("接收完毕");
                receive=true;
                ctx.writeAndFlush(new FileResponseMessage(start));
                start=0;
                file.close();
                super.channelRead(ctx, groupMsg);
            }
        }
        else{
            super.channelRead(ctx, msg);
        }
    }
}
