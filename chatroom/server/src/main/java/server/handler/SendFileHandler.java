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
            int once=msg.getOnce();
            byte[] bytes=new byte[once];
            int start= msg.getStart(), byteRead;
            file.seek(start);
            while((byteRead=file.read(bytes))!=-1){
                if(byteRead<once){
                    bytes= Arrays.copyOfRange(bytes,0,byteRead);
                }

                ChannelFuture future= ctx.writeAndFlush(new SendFileMessage(start,bytes));
                future.addListener((ChannelFutureListener) future1 -> {
                    if(!future1.isSuccess()){
                        log.debug("出现错误");
                    }
                    Throwable throwable=future1.cause();
                    if(throwable!=null){
                        throwable.printStackTrace();
                    }
                });
                start+=byteRead;
                file.seek(start);
                //log.info("还在发送，fileLength={},sum={},send={},bytes.length={}",fileLength,sum,byteRead,bytes.length);
            }

            log.info("发送完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
