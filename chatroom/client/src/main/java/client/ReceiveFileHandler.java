package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.SendFileMessage;

import java.io.RandomAccessFile;

import static client.ChatClient.*;

public class ReceiveFileHandler extends SimpleChannelInboundHandler<SendFileMessage> {
    private static RandomAccessFile file;
    private static int rate=0;
    private static boolean receive=true;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendFileMessage msg) throws Exception {
        if(receive){
            file=new RandomAccessFile(path,"rw");
            receive=false;
        }
        file.write(msg.getFile());
        int percent=msg.getPercent();

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
        if(percent==100){
            file.close();
            rate=0;
            receive=true;
            synchronized (waitMessage){
                waitMessage.notify();
                System.out.println();
            }
        }
    }
}
