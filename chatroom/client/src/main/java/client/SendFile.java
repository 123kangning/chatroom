package client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.*;

import static client.ChatClient.*;

import java.io.*;
@Slf4j
public class SendFile {
    public SendFile(ChannelHandlerContext ctx, File file,Message message){

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            fileLength= (int) randomAccessFile.length();
            if(fileLength>1024*1024*1024){
                System.out.println("文件大于1G，拒绝发送！");
                return;
            }

            if(message instanceof FriendChatRequestMessage){
                ((FriendChatRequestMessage)message).setPath(file.getAbsolutePath());
                ((FriendChatRequestMessage)message).setFileName(file.getName());
                ((FriendChatRequestMessage)message).setFileSize(fileLength);
            }else{
                ((GroupChatRequestMessage)message).setPath(file.getAbsolutePath());
                ((GroupChatRequestMessage)message).setFileName(file.getName());
                ((GroupChatRequestMessage)message).setFileSize(fileLength);
            }

            int once=fileLength/100;
            if(once<1024){
                once=1024;
            }else if(once>284634){//似乎是一个比较合适的上限
                once=284634;
            }
            byte[] bytes=new byte[once];

            int byteRead=0;
            while((byteRead=randomAccessFile.read(bytes))!=-1){
                if(message instanceof FriendChatRequestMessage){
                    ((FriendChatRequestMessage)message).setFile(bytes);
                }else{
                    ((GroupChatRequestMessage)message).setFile(bytes);
                }
                ChannelFuture sendFile= ctx.writeAndFlush(message);
                //log.info("还在发送");
            }
            try {
                synchronized (waitMessage) {
                    waitMessage.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
