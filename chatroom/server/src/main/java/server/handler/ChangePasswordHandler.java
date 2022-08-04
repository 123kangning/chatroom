package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.ChangePasswordRequestMessage;
import message.ResponseMessage;

import java.sql.PreparedStatement;

import static server.ChatServer.connection;

public class ChangePasswordHandler extends SimpleChannelInboundHandler<ChangePasswordRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChangePasswordRequestMessage msg) throws Exception {
        int userID = msg.getUserID();
        String password = msg.getPassword();
        String sql = "update user set password=? where userID=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, password);
        ps.setInt(2, userID);
        ps.executeUpdate();
        ctx.writeAndFlush(new ResponseMessage(true, ""));
    }
}
