package client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.*;

import static client.ChatClient.*;

import java.io.*;
import java.util.Arrays;

@Slf4j
public class SendFile {
    private static final int MAX_LENGTH = 1 << 30;

    public SendFile(ChannelHandlerContext ctx, File file,Message message){
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            breakPointSend=new RandomAccessFile(System.getProperty("user.dir")+"/client/src/main/java/breakPointSend","rw");
            fileLength= (int) randomAccessFile.length();
            if(fileLength>MAX_LENGTH){
                System.out.println("文件大于1G，拒绝发送！");
                return;
            }

            if(message instanceof FriendChatRequestMessage){
                ((FriendChatRequestMessage)message).setFileName(file.getName());
                ((FriendChatRequestMessage)message).setFileSize(fileLength);
            }else{
                ((GroupChatRequestMessage)message).setFileName(file.getName());
                ((GroupChatRequestMessage)message).setFileSize(fileLength);
            }

            int once=fileLength/100;
            if(once<1024){
                once=1024;
            }else if(once>1048000){
                once=1048000;
            }
            byte[] bytes=new byte[once];
            int start=0,byteRead=0;
            while((byteRead=randomAccessFile.read(bytes))!=-1){
                if(message instanceof FriendChatRequestMessage){
                    ((FriendChatRequestMessage)message).setFile(bytes);
                }else{
                    ((GroupChatRequestMessage)message).setFile(bytes);
                }

                ChannelFuture sendFile= ctx.writeAndFlush(message);
                sendFile.sync();
                /*sendFile.addListener((ChannelFutureListener) future1 -> {
                    if(!future1.isSuccess()){
                        log.debug("出现错误");
                    }
                    Throwable throwable=future1.cause();
                    if(throwable!=null){
                        throwable.printStackTrace();
                    }
                });*/
                //log.info("还在发送，fileSize={},sum={},send={},bytes.length={}",fileLength,sum,byteRead,bytes.length);
            }//log.info("发送完毕");

            ChatClient.wait1();

            System.out.println();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
