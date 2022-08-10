package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendShieldRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.jdbcPool;

@Slf4j
public class FriendShieldHandler extends SimpleChannelInboundHandler<FriendShieldRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendShieldRequestMessage msg) throws Exception {
        try {
            Connection connection= jdbcPool.getConnection();
            int userID = msg.getUserID();
            int FriendID = msg.getFriendId();
            String shield = "1";
            String sql = "select fromID,toID,shield from friend where (fromID=? and toID=?) or (toID=? and fromID=?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, FriendID);
            ps.setInt(4, userID);
            ps.setInt(3, FriendID);
            ResultSet set = ps.executeQuery();
            if (set.next()) {
                log.info("fromID={},toID={},shied={}", set.getInt(1), set.getInt(2), set.getString(3));
                if (set.getString(3).equals("3")) {//已经是双向屏蔽
                    ctx.writeAndFlush(new ResponseMessage(true, ""));
                    return;
                }
                //判断是否要双向屏蔽
                if ((set.getInt(1) == userID && set.getString(3).equals("2")) ||
                        (set.getInt(1) == FriendID && set.getString(3).equals("1"))) {

                    shield = "3";
                }
            }
            sql = "update friend set shield=? where fromID=? and toID=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, shield);
            ps.setInt(2, userID);
            ps.setInt(3, FriendID);
            int row = ps.executeUpdate();
            ResponseMessage message;
            if (row == 1) {
                message = new ResponseMessage(true, "");
            } else {
                if (shield.equals("1")) {//不是双向屏蔽
                    shield = "2";
                }
                ps.setString(1, shield);
                ps.setInt(3, userID);
                ps.setInt(2, FriendID);
                row = ps.executeUpdate();
                if (row == 1) {
                    message = new ResponseMessage(true, "");
                } else {
                    message = new ResponseMessage(false, "没有这个朋友");
                }
            }
            ctx.writeAndFlush(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
