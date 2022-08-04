package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.LogoutRequestMessage;
import message.Message;
import message.ResponseMessage;
import server.session.SessionMap;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static server.ChatServer.connection;

@Slf4j
public class LogoutHandler extends SimpleChannelInboundHandler<LogoutRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestMessage msg) {
        try {
            log.info("LogoutHandler");
            long userID = SessionMap.getUser(ctx.channel());
            log.info("这个管道是 {}", ctx.channel());
            log.info("In LogoutHandler userID={}", SessionMap.getUser(ctx.channel()));
            if (userID == 0) {
                ctx.writeAndFlush(new ResponseMessage(true, ""));
                return;
            }
            String sql = "update user set online='F' where userID=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, userID);
            int row = statement.executeUpdate();

            ResponseMessage message;
            if (row != 1) {
                message = new ResponseMessage(false, "退出失败");
            } else {
                message = new ResponseMessage(true, "");
            }
            message.setMessageType(Message.LogoutResponseMessage);
            if (ctx.channel() != null)
                ctx.writeAndFlush(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
