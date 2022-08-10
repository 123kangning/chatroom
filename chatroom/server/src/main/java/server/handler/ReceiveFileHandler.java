package server.handler;

import cn.hutool.core.date.DateTime;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import message.FileResponseMessage;
import message.FriendChatRequestMessage;
import message.GroupChatRequestMessage;
import message.SendFileMessage;

import java.io.*;
import java.util.Arrays;

@Slf4j
public class ReceiveFileHandler extends ChannelInboundHandlerAdapter {
    private static FriendChatRequestMessage friendMsg;
    private static GroupChatRequestMessage groupMsg;
    private String path;
    private static int start=0;
    private static int size,rate,length;
    private static boolean receive;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DateTime dateTime=new DateTime(System.currentTimeMillis());
        if(msg instanceof FriendChatRequestMessage){
            log.info("msg instanceof FriendChatRequestMessage ");

            if(((FriendChatRequestMessage) msg).getFile()==null){//不是发送文件的消息
                super.channelRead(ctx, msg);
                return;
            }
            byte[] bytes=((FriendChatRequestMessage) msg).getFile();
            if(bytes.length!=1||bytes[0]!=1){
                friendMsg = (FriendChatRequestMessage) msg;
                size=friendMsg.getFileSize();
                if(friendMsg.getPath()!=null){
                    path= friendMsg.getPath();
                }else {
                    path= System.getProperty("user.dir") + "/server/story/" + friendMsg.getFileName() + dateTime;
                }
                friendMsg.setPath(path);
            }

                File file=new File(path);
                log.info("path = {} ,file.exists = {}, file.length = {} ,fileLength = {}",path,file.exists(),file.length(),size);
                if(file.exists()&&file.length()==size){
                    //接收完毕
                    log.info("接收完毕");
                    super.channelRead(ctx, friendMsg);
                }else{
                    //开始接收
                    ctx.writeAndFlush(new FileResponseMessage(0,path));
                }
        }
        else if(msg instanceof GroupChatRequestMessage){
            //log.info("msg instanceof GroupChatRequestMessage");

            if(receive){
                groupMsg = (GroupChatRequestMessage) msg;
                size=groupMsg.getFileSize();
                path= System.getProperty("user.dir") + "/server/story/" + groupMsg.getFileName() + dateTime;
                groupMsg.setPath(path);
                //  file=new RandomAccessFile(path,"rw");
                rate=0;
                receive=false;
                ctx.writeAndFlush(new FileResponseMessage(0,path));
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
            //file.write(bytes);
            //log.info("length = {} ,start = {},size={},real={}",length,start,size,bytes.length);
            int current=(int)((start*1.0/size)*100);

            if(start<size){//说明文件可能还未接收完毕，返回读取的字节数
                if(current>rate){
                    ctx.writeAndFlush(new FileResponseMessage(start,path));
                    rate=current;
                }
                //log.info("接收尚未结束");
            }else{//接收完毕
                log.info("接收完毕");
                receive=true;
                ctx.writeAndFlush(new FileResponseMessage(start,path));
                start=0;
                //file.close();
                super.channelRead(ctx, groupMsg);
            }
        }
        else{
            super.channelRead(ctx, msg);
        }
    }
}
