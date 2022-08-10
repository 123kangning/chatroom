package server.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.GroupChatRequestMessage;
import message.SendFileMessage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
@Slf4j
public class SendFileHandler extends SimpleChannelInboundHandler<SendFileMessage> {
    private static int rate=0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendFileMessage msg) {
        String path=System.getProperty("user.dir")+"/server/story/"+msg.getFileName();
        try {
            RandomAccessFile file=new RandomAccessFile(path,"r");
            int fileLength= (int) file.length();
            int once=fileLength/100;
            if(once<1024){
                once=1024;
            }else if(once>1048000){
                once=1048000;
            }
            byte[] bytes=new byte[once];
            int sum=0,byteRead=0;
            while((byteRead=file.read(bytes))!=-1){
                sum+=byteRead;
                int percent=(int)((sum*1.0/fileLength)*100);
                if(byteRead<once){
                    bytes= Arrays.copyOfRange(bytes,0,byteRead);
                }
                ChannelFuture future= ctx.writeAndFlush(new SendFileMessage(percent,bytes));
                future.addListener((ChannelFutureListener) future1 -> {
                    if(!future1.isSuccess()){
                        log.debug("出现错误");
                    }
                    Throwable throwable=future1.cause();
                    if(throwable!=null){
                        throwable.printStackTrace();
                    }
                });
                //log.info("还在发送，fileLength={},sum={},send={},bytes.length={}",fileLength,sum,byteRead,bytes.length);
            }
            log.info("发送完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
