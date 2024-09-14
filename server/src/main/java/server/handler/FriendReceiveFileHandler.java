package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FileResponseMessage;
import message.FriendChatRequestMessage;
import message.GroupChatRequestMessage;
import message.SendFile1Message;

import java.io.RandomAccessFile;
@Slf4j
public class FriendReceiveFileHandler extends SimpleChannelInboundHandler<SendFile1Message> {
    private static int rate=0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendFile1Message msg) throws Exception {
        String serverPath=msg.getServerPath();
        int start;
        int fileLength;
        byte[] bytes;
        try (RandomAccessFile file = new RandomAccessFile(serverPath, "rw")) {
            start = msg.getStart();
            fileLength = msg.getFileLength();
            file.seek(start);
            bytes = msg.getFile();
            file.write(bytes);
        }
        start+=bytes.length;
        int percent=(int)(((start*1.0)/fileLength)*100);
        log.info("send start = {}, fileLength = {}",start,fileLength);
        if(start>=fileLength){
            //将文件合成FriendChat消息传递
            ctx.writeAndFlush(new FileResponseMessage(start,serverPath));
            rate=0;
            if(msg.getChatType()==1){
                GroupChatRequestMessage message=new GroupChatRequestMessage();
                message.setFile(new byte[]{1});
                super.channelRead(ctx,message);
            }else{
                FriendChatRequestMessage message=new FriendChatRequestMessage();
                message.setFile(new byte[]{1});
                super.channelRead(ctx,message);
            }
        }else{
            //返回给客户端发送文件的进度
            if(percent>rate){
                ctx.writeAndFlush(new FileResponseMessage(start,serverPath));
                rate=percent;
            }
        }
    }
}
