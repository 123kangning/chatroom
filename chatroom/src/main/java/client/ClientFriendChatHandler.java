package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendChatRequestMessage;

import static client.ChatClient.*;

public class ClientFriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        if(immediate&&msg.getUserID()==talkWith){
            System.out.println(msg.getMessage());
        }else{
            if(haveNoRead==0&&immediate==false){
                System.out.println("主人，您有未查看的信息，请注意查看...");
            }
            int count=msg.getCount();

                haveNoRead+=count;

        }

        /*if(haveNoRead){
            System.out.println("主人，您有未查看的信息，请注意查看...");
        }*/
    }
}
