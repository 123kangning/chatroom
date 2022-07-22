package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendChatRequestMessage;

public class ClientFriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        System.out.printf("\n用户%d发来一条消息：%s\n",msg.getUserID(),msg.getMessage());
    }
}
