package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendAddRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static server.ChatServer.jdbcPool;

@Slf4j
public class FriendAddHandler extends SimpleChannelInboundHandler<FriendAddRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendAddRequestMessage msg) throws Exception {
        if (msg.getSetList()) {
            choice1(ctx, msg);

        } else {
            choice2(ctx, msg);
        }
    }

    public void choice1(ChannelHandlerContext ctx, FriendAddRequestMessage msg) {
        try {
            Connection connection= jdbcPool.getConnection();
            ResponseMessage message;
            List<Integer> friendIDList = msg.getFriendIDList();
            int length = friendIDList.size();
            int count = 0;
            for (int FriendID : friendIDList) {
                int userID = msg.getUserId();
                String username = "";
                String friendName = "";
                if (!GroupJoinHandler.CheckHaveMessage(userID, "A", FriendID, "F", 0, "请求添加你为好友", "F")) {
                    log.info("消息判断不存在");
                    continue;
                }
                String s1 = "select toID from friend where (fromID=? and toID=?) or (toID=? and fromID=?)";
                PreparedStatement check = connection.prepareStatement(s1);
                check.setInt(1, userID);
                check.setInt(2, FriendID);
                check.setInt(3, userID);
                check.setInt(4, FriendID);
                ResultSet checkSet = check.executeQuery();
                if (checkSet.next()) {
                    ctx.writeAndFlush(new ResponseMessage(false, "该用户已经是您的朋友了"));
                    return;
                }
                String s = "select username from user where userID=?";
                PreparedStatement ps = connection.prepareStatement(s);
                ps.setInt(1, userID);
                ResultSet set = ps.executeQuery();
                if (set.next()) {
                    username = set.getString(1);
                }
                ps.setInt(1, FriendID);
                set = ps.executeQuery();
                if (set.next()) {
                    friendName = set.getString(1);
                } else {
                    message = new ResponseMessage(false, "该用户不存在");
                    ctx.writeAndFlush(message);
                    return;
                }

                if(!msg.isNoAdd()){
                    String sql = "insert into  friend(fromID,toID,shield,to_name,From_name) values(?,?,'0',?,?)";
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, userID);
                    ps.setInt(2, FriendID);
                    ps.setString(3, friendName);
                    ps.setString(4, username);
                    count += ps.executeUpdate();
                }


                String sql = "update message set isAccept='T' where userID=? and talkerID=? and talker_type='F' and msg_type='A' and isAccept='F'";
                ps = connection.prepareStatement(sql);
                ps.setInt(1, userID);
                ps.setInt(2, FriendID);
                int row = ps.executeUpdate();
            }
            if(msg.isNoAdd()){
                message = new ResponseMessage(true, "");
            }else{
                if (count == length) {
                    message = new ResponseMessage(true, "");
                } else if (count > 0) {
                    message = new ResponseMessage(false, "部分添加失败");
                } else {
                    message = new ResponseMessage(false, "添加失败");
                }
            }
            ctx.writeAndFlush(message);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void choice2(ChannelHandlerContext ctx, FriendAddRequestMessage msg) {
        try {
            Connection connection= jdbcPool.getConnection();
            int userID = msg.getUserId();
            int FriendID = msg.getFriendId();
            if (!GroupJoinHandler.CheckHaveMessage(userID, "A", FriendID, "F", 0, "请求添加你为好友", "F")) {
                log.info("消息判断不存在");
                return;
            }
            String username = "";
            String friendName = "";
            ResponseMessage message;
            String s1 = "select toID from friend where (fromID=? and toID=?) or (toID=? and fromID=?)";
            PreparedStatement check = connection.prepareStatement(s1);
            check.setInt(1, userID);
            check.setInt(2, FriendID);
            check.setInt(4, userID);
            check.setInt(3, FriendID);
            ResultSet checkSet = check.executeQuery();
            if (checkSet.next()) {
                ctx.writeAndFlush(new ResponseMessage(false, "该用户已经是您的朋友了"));
                return;
            }
            String s = "select username from user where userID=?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setInt(1, userID);
            ResultSet set = ps.executeQuery();
            if (set.next()) {
                username = set.getString(1);
            }
            ps.setInt(1, FriendID);
            set = ps.executeQuery();
            if (set.next()) {
                friendName = set.getString(1);
            } else {
                message = new ResponseMessage(false, "该用户不存在");
                ctx.writeAndFlush(message);
                return;
            }
            String sql = "insert into  friend(fromID,toID,shield,to_name,From_name) values(?,?,'0',?,?)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, FriendID);
            ps.setString(3, friendName);
            ps.setString(4, username);
            int row = ps.executeUpdate();
            if (row == 1) {
                message = new ResponseMessage(true, "");
            } else {
                message = new ResponseMessage(false, "添加失败");
            }
            sql = "update message set isAccept='T' where userID=? and talkerID=? and talker_type='F' and msg_type='A' and isAccept='F'";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, FriendID);
            ps.executeUpdate();
            ctx.writeAndFlush(message);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
