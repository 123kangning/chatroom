package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.ResponseMessage;
import message.SignOutRequestMessage;
import server.session.SessionMap;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static server.ChatServer.jdbcPool;

@Slf4j
public class SignOutHandler extends SimpleChannelInboundHandler<SignOutRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SignOutRequestMessage msg) throws Exception {
        Connection connection= jdbcPool.getConnection();
        int userID = SessionMap.getUser(ctx.channel());
        String sql = "delete from user where userID=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, userID);
        int row = statement.executeUpdate();
        log.info("row = {}", row);
        ResponseMessage message;
        if (row == 1) {
            message = new ResponseMessage(true, "");
        } else {
            message = new ResponseMessage(false, "注销失败");
        }
        ctx.writeAndFlush(message);
        String sql1="delete from friend where fromID=? or toID=?";
        PreparedStatement ps=connection.prepareStatement(sql1);
        ps.setInt(1,userID);
        ps.setInt(2,userID);

        sql1="delete from group2 where userID=? ";
        ps=connection.prepareStatement(sql1);
        ps.setInt(1,userID);
        ps.executeUpdate();
        sql1="delete from message where talkerID=?  and isAccept='F'";
        ps=connection.prepareStatement(sql1);
        ps.setInt(1,userID);
        ps.executeUpdate();
        log.info("ctx.writeAndFlush(message)");
        connection.close();
    }
}
