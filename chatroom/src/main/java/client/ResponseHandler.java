package client;

import client.view.EnterView;
import client.view.MainView;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import message.*;

import static client.ChatClient.waitMessage;
import static client.ChatClient.waitSuccess;

@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        boolean success=msg.getSuccess();
        String reason=msg.getReason();
        int ResponseMessageType=msg.getMessageType();
        log.info("ResponseMessageType={}",ResponseMessageType);
        if(!success){
            System.out.print("操作失败 "+reason);
            /*switch (ResponseMessageType){
                case Message.LoginResponseMessage:
                    new EnterView(ctx);
                    break;
               case Message.LogoutResponseMessage:
                   new MainView(ctx);
                   break;

            }
            return;*/
            waitSuccess=0;

        }else{
            System.out.print("操作成功 "+reason);
            waitSuccess=1;
        }
        synchronized (waitMessage){
            waitMessage.notifyAll();
        }




        /*switch (ResponseMessageType){
            case Message.LoginResponseMessage:
                log.info("{}",Message.LoginResponseMessage);
                new MainView(ctx);
                break;
            case Message.LogoutResponseMessage:
                log.info("Message.LogoutResponseMessage");
                new EnterView(ctx);
                break;
            case Message.SearchPasswordResponseMessage:
            case Message.SignInResponseMessage:
            case Message.SignOutResponseMessage:
            case Message.FriendAddResponseMessage:
            case Message.FriendChatResponseMessage:
            case Message.FriendDeleteResponseMessage:
            case Message.FriendQueryResponseMessage:
            case Message.FriendShieldResponseMessage:
            case Message.GroupChatResponseMessage:
            case Message.GroupCreateResponseMessage:
            case Message.GroupDeleteResponseMessage:
            case Message.GroupJoinResponseMessage:
            case Message.GroupNumberResponseMessage:
            case Message.GroupQuitResponseMessage:

        }*/
        //super.channelRead(ctx, msg);
    }

}
