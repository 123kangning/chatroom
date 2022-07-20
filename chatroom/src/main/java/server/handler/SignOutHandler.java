package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.SignInRequestMessage;
import message.SignOutRequestMessage;
import static server.ChatServer.connection;
import java.sql.PreparedStatement;

public class SignOutHandler extends SimpleChannelInboundHandler<SignOutRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SignOutRequestMessage msg) throws Exception {
        long userID= msg.getUserID();
        String sql="delete from test1 where userID=?";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setLong(1,userID);
        statement.execute();

    }
}
