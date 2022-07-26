package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendChatRequestMessage;

import static client.ChatClient.*;

public class ClientFriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        if(immediate){
            System.out.println(msg.getMessage());
            return ;
        }
        if(!haveNoRead){
            System.out.println("主人，您有未查看的信息，请注意查看...");
            haveNoRead=true;
        }
    }
}
