package client;

import io.netty.channel.ChannelFuture;
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
            breakPointSend=new RandomAccessFile(breakPointSendPath,"rw");
            String sendFilePath=file.getAbsolutePath();
            String serverPath=null;
            fileLength= (int) randomAccessFile.length();
            if(fileLength>MAX_LENGTH){
                System.out.println("文件大于1G，拒绝发送！");
                return;
            }
            int start=0;
            //遍历断点续传配置文件
            while(breakPointSend.read()!=-1){
                if(breakPointSend.length()-breakPointSend.getFilePointer()<=8)break;
                breakPointSend.seek(breakPointSend.getFilePointer()-1);
                int tempLength;
                if(breakPointSend.readUTF().equals(sendFilePath)){
                    breakPointSend.readInt();
                    int lineStart= (int) breakPointSend.getFilePointer();
                    String s=breakPointSend.readUTF();
                    if((tempLength=breakPointSend.readInt())<fileLength){
                        //需要断点续传
                        serverPath=s;
                        start=tempLength;
                        breakPointSend.seek(lineStart);
                        break;
                    }
                }else{
                    breakPointSend.readInt();
                    breakPointSend.readUTF();
                    breakPointSend.readInt();
                }
            }
            if(start==0){//不需要断点续传
                breakPointSend.seek(breakPointSend.length());
                breakPointSend.writeUTF(sendFilePath);
                breakPointSend.writeInt(fileLength);
            }



            if(message instanceof FriendChatRequestMessage){
                ((FriendChatRequestMessage)message).setFileName(file.getName());
                ((FriendChatRequestMessage)message).setFileSize(fileLength);
                ((FriendChatRequestMessage)message).setPath(serverPath);
                ((FriendChatRequestMessage)message).setFile(new byte[]{2});
            }else{
                ((GroupChatRequestMessage)message).setFileName(file.getName());
                ((GroupChatRequestMessage)message).setFileSize(fileLength);
                ((GroupChatRequestMessage)message).setPath(serverPath);
                ((GroupChatRequestMessage)message).setFile(new byte[]{2});
            }

            ctx.writeAndFlush(message);

            int once=(fileLength-start)/100;
            if(once<1024){
                once=1024;
            }else if(once>1048000){
                once=1048000;
            }
            byte[] bytes=new byte[once];
            int byteRead;
            randomAccessFile.seek(start);
            ChatClient.wait1();
            serverPath=(String)blockingQueue.take();
            while((byteRead=randomAccessFile.read(bytes))!=-1){
                if(byteRead<once){
                    bytes=Arrays.copyOfRange(bytes,0,byteRead);
                }
                SendFile1Message sendFile1Message=new SendFile1Message(serverPath,bytes,start,fileLength);
                if(message instanceof GroupChatRequestMessage){
                    sendFile1Message.setChatType((byte)1);
                }

                ChannelFuture sendFile= ctx.writeAndFlush(sendFile1Message);
                start+=byteRead;
                sendFile.sync();
            }//log.info("发送完毕");

            ChatClient.wait1();

            System.out.println();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
