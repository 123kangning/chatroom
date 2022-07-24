package client;

import client.view.EnterView;
import client.view.MainView;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import message.*;

import static client.ChatClient.*;

@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        boolean success=msg.getSuccess();
        String reason=msg.getReason();
        int ResponseMessageType=msg.getMessageType();
        //log.info("ResponseMessageType={}",ResponseMessageType);
        if(!success){
            System.out.print("操作失败 "+reason);
            waitSuccess=0;
        }else{
            waitSuccess=1;
            if(msg.getMessageType()==Message.FriendQueryRequestMessage){
                friendList=msg.getFriendList();
                //log.info("friendList=msg.getFriendList()");
            }
            else if(msg.getMessageType()==Message.noticeMapMessage){
                noticeMap=msg.getNoticeMap();
            }else{
                System.out.print("操作成功 "+reason);
            }

        }
        synchronized (waitMessage){
            waitMessage.notifyAll();
        }
        //log.info("waitMessage.notifyAll();");
    }
}
