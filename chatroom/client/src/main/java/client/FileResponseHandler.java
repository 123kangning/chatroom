package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FileResponseMessage;
import static client.ChatClient.*;
@Slf4j
public class FileResponseHandler extends SimpleChannelInboundHandler<FileResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileResponseMessage msg) throws Exception {
        int percent=(int)(((msg.getLength()*1.0)/fileLength)*100);
        //log.info("percent = {}",percent);
        System.out.print("\r|");
        for(int i=0;i<percent;i++){
            System.out.print("#");
        }
        for(int i=percent;i<100;i++){
            System.out.print("-");
        }
        System.out.print("|");
        /*if(percent==100){//1.客户端循环将文件所有内容发送完毕之后，才陷入阻塞
            synchronized (waitMessage){
                waitMessage.notifyAll();
            }
        }*/
        //2.客户端每次发送文件的一部分之后都会陷入阻塞，等待进度消息返回后被唤醒
        /*synchronized (waitMessage){
            waitMessage.notifyAll();
        }*/
    }
}
