package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.ResponseMessage;
import message.SearchPasswordRequestMessage;
import server.session.MailSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import static server.ChatServer.connection;

public class SearchPasswordHandler extends SimpleChannelInboundHandler<SearchPasswordRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SearchPasswordRequestMessage msg) throws Exception {
        int userID = msg.getUserID();
        String mail = "";
        String sql = "select mail from user where userID=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet set = ps.executeQuery();
        if (set.next()) {
            mail = set.getString(1);
            int mailAuthCode = new Random().nextInt(899999) + 100000;
            ResponseMessage message = new ResponseMessage(true, "");
            message.setMailAuthCode(mailAuthCode);
            ctx.writeAndFlush(message);
            MailSession.email(mail, mailAuthCode);
        }
    }
}
