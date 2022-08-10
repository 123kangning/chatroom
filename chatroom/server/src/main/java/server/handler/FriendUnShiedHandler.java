package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendUnShieldRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static server.ChatServer.jdbcPool;

@Slf4j
public class FriendUnShiedHandler extends SimpleChannelInboundHandler<FriendUnShieldRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendUnShieldRequestMessage msg) throws Exception {
        Connection connection= jdbcPool.getConnection();
        log.info("Enter");
        int userID = msg.getUserID();
        int FriendID = msg.getFriendId();
        String shield = "1";
        String sql = "select fromID,toID,shield from friend where (fromID=? and toID=?) or (toID=? and fromID=?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setInt(2, FriendID);
        ps.setInt(3, userID);
        ps.setInt(4, FriendID);
        ResultSet set = ps.executeQuery();
        if (set.next()) {
            log.info("fromID={},toID={},shied={}", set.getInt(1), set.getInt(2), set.getString(3));
            if (set.getString(3).equals("0")) {//已经没有屏蔽
                log.info("case 1");
                ctx.writeAndFlush(new ResponseMessage(true, ""));
                return;
            }
            //判断是单向屏蔽
            if ((set.getInt(1) == userID && set.getString(3).equals("1")) ||
                    (set.getInt(1) == FriendID && set.getString(3).equals("2"))) {
                log.info("case 2");
                shield = "0";
            }
            if (set.getString(3).equals("3")) {
                if (set.getInt(1) == userID) {
                    log.info("case 3");
                    shield = "2";
                } else {
                    log.info("case 4");
                    shield = "1";
                }
            }
            if (set.getInt(1) == userID) {

                sql = "update friend set shield=? where fromID=? and toID=?";
                ps = connection.prepareStatement(sql);
                ps.setString(1, shield);
                ps.setInt(2, userID);
                ps.setInt(3, FriendID);
                int row = ps.executeUpdate();
            } else {

                sql = "update friend set shield=? where fromID=? and toID=?";
                ps = connection.prepareStatement(sql);
                ps.setString(1, shield);
                ps.setInt(3, userID);
                ps.setInt(2, FriendID);
                int row = ps.executeUpdate();
            }
        }
        log.info("结束");
        ctx.writeAndFlush(new ResponseMessage(true, ""));
    }
}
