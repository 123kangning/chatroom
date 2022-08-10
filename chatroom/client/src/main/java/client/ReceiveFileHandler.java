package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.SendFileMessage;

import java.io.RandomAccessFile;

import static client.ChatClient.*;
@Slf4j
public class ReceiveFileHandler extends SimpleChannelInboundHandler<SendFileMessage> {
    private static RandomAccessFile file;
    private static int rate=0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendFileMessage msg) throws Exception {
        int start=msg.getStart();
        byte[] bytes=msg.getFile();
        file= new RandomAccessFile(path,"rw");
        file.seek(start);
        file.write(bytes);
        start+=bytes.length;

        int percent=(int)(((start*1.0)/fileLength)*100);
        //log.info("start = {},once = {}, percent = {}",start,bytes.length,percent);
        if(percent>rate){//打印进度条
            System.out.print("\r|");
            for(int i=0;i<percent;i++){
                System.out.print("#");
            }
            for(int i=percent;i<100;i++){
                System.out.print("-");
            }
            System.out.printf("|%3d%%",percent);
            rate=percent;
        }
        breakPointReceive.writeInt(start);
        breakPointReceive.seek(breakPointReceive.getFilePointer()-4);
        /*
        * 纠结一件事，把close方法每次调用都执行，害怕开销太大;
        * 把close在最后执行，怕发生断掉续传时没有即使写入部分数据
        * */
        if(percent==100){
            file.close();
            rate=0;
            synchronized (waitMessage){
                waitMessage.notify();
                System.out.println();
            }
        }
    }
}
