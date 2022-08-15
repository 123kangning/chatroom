package client;

import io.netty.channel.ChannelHandlerContext;
import message.SendFileMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static client.ChatClient.*;

public class StartReceiveFile {
    public StartReceiveFile(ChannelHandlerContext ctx,String serverFileName){
        int start=0;
        try {
            breakPointReceive=new RandomAccessFile(breakPointReceivePath,"rw");

            while(breakPointReceive.read()!=-1){
                breakPointReceive.seek(breakPointReceive.getFilePointer()-1);
                if(breakPointReceive.length()-breakPointReceive.getFilePointer()<=8)break;
                if(serverFileName.equals(breakPointReceive.readUTF())){
                    breakPointReceive.readInt();
                    int point=(int)breakPointReceive.getFilePointer();
                    int tempLength=breakPointReceive.readInt();
                    if(tempLength<fileLength){//需要断点续传
                        start=tempLength;
                        breakPointReceive.seek(point);
                        break;
                    }
                }else{
                    breakPointReceive.readInt();
                    breakPointReceive.readInt();
                }
            }
            /*
             * 后期加上遍历断点续传文件的代码
             * */

            if(start==0){//第一次发送，无需断点续传
                breakPointReceive.seek(breakPointReceive.length());
                breakPointReceive.writeUTF(serverFileName);
                breakPointReceive.writeInt(fileLength);
            }
            int once=(fileLength-start)/100;
            if(once<1024){
                once=1024;
            }else if(once>1048576){
                once=1048576;
            }
            ctx.writeAndFlush(new SendFileMessage(serverFileName,start,once));

            /*
            * 一会考虑一下这里需要加阻塞吗
            * */
            //ChatClient.wait1();//经过考虑，不需要加
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
