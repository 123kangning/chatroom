package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FileResponseMessage;
import static client.ChatClient.*;
@Slf4j
public class FileResponseHandler extends SimpleChannelInboundHandler<FileResponseMessage> {
    private static boolean receive;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileResponseMessage msg) throws Exception {
        if(!receive){
            breakPointSend.writeUTF(msg.getServerPath());
            breakPointSend.writeInt(0);
            receive=true;
            blockingQueue.put(msg.getServerPath());
            synchronized (waitMessage){
                waitMessage.notify();
            }
        }
        int percent=(int)(((msg.getLength()*1.0)/fileLength)*100);
        //log.info("percent = {}",percent);
        System.out.print("\r|");
        for(int i=0;i<percent;i++){
            System.out.print("#");
        }
        for(int i=percent;i<100;i++){
            System.out.print("-");
        }
        System.out.printf("|%3d%%",percent);
            breakPointSend.seek(breakPointSend.getFilePointer()-4);
            log.info("breakPointSend.getFilePointer() = {}",breakPointSend.getFilePointer());
            breakPointSend.writeInt(msg.getLength());
            if(percent==100){
                receive=false;
                breakPointSend.close();
            }
    }
}
