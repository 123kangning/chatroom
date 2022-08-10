package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.GroupJoinRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.jdbcPool;

@Slf4j
public class GroupJoinHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        ResponseMessage message;
        int count = 0;
        if (msg.getSetList()) {
            for (int groupID : msg.getList()) {
                if (CheckHaveMessage(msg.getUserID(), "A", 0, "G", groupID, "邀请你加入群组", "F")) {
                    log.info("消息判断存在");
                    count += choice(ctx, msg, groupID);
                } else {
                    log.info("消息判断不存在");
                }
            }
        } else {//在用户申请加入群组时，talkerID为申请的用户的ID
            if (CheckHaveMessage(msg.getUserID(), "A", msg.getTalkerID(), "F", msg.getGroupId(), "申请加入群组", "F")) {
                log.info("消息判断存在");
                count += choice(ctx, msg, msg.getGroupId());
            } else {
                log.info("消息判断不存在");
            }
        }
        message = new ResponseMessage(true, "");
        message.setReadCount(count);
        ctx.writeAndFlush(message);
    }

    protected int choice(ChannelHandlerContext ctx, GroupJoinRequestMessage msg, int groupID) {
        try {
            Connection connection= jdbcPool.getConnection();
            int userID = msg.getUserID();
            int talkerID=msg.getTalkerID();
            String talker_type=msg.getTalker_type();
            String group_name;
            String sqlCheck = "select group_name from group1 where groupID=?";
            PreparedStatement psCheck = connection.prepareStatement(sqlCheck);
            psCheck.setInt(1, groupID);
            ResultSet setCheck = psCheck.executeQuery();
            ResponseMessage message;
            if (!setCheck.next()) {
                ctx.writeAndFlush(new ResponseMessage(false, "该群组不存在"));
                return 0;
            } else {
                group_name = setCheck.getString(1);
            }
            String sql = "select groupID from group2 where groupID=? and userID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, groupID);
            ps.setInt(2, talker_type.equals("G")?userID:talkerID);
            ResultSet set = ps.executeQuery();
            if (set.next()) {
                ctx.writeAndFlush(new ResponseMessage(false, "你已经在群聊中了"));
                return 0;
            }
            String sql1 = "insert into group2(groupID,group_name,userID,user_type,say) values(?,?,?,'0','T')";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, groupID);
            ps1.setString(2, group_name);
            ps1.setInt(3, talker_type.equals("G")?userID:talkerID);
            ps1.executeUpdate();
            log.info("case 2");
            int count;
            log.info("userID = {}, talker_type = {}",userID,msg.getTalker_type());
            if(msg.getTalker_type().equals("G")){
                String sql3 = "select count(msg_id) from message where userID=? and groupID=? and  msg_type='A' and talker_type='G' and content='邀请你加入群组' and isAccept='F'";
                PreparedStatement ps3 = connection.prepareStatement(sql3);
                ps3.setInt(1, userID);
                ps3.setInt(2, groupID);
                ResultSet set2 = ps3.executeQuery();
                set2.next();
                count = set2.getInt(1);
                String sql2 = "update message set isAccept='T' where userID=? and groupID=? and talker_type=? and msg_type='A' and isAccept='F'";
                PreparedStatement ps2 = connection.prepareStatement(sql2);
                ps2.setInt(1, userID);
                ps2.setInt(2, groupID);
                ps2.setString(3, msg.getTalker_type());
                int row=ps2.executeUpdate();
                log.info("ps2.executeUpdate() = {}",row);
            }else{
                String sql3 = "select count(msg_id) from message where talkerID=? and groupID=? and  msg_type='A' and talker_type='F' and content='申请加入群组' and isAccept='F'";
                PreparedStatement ps3 = connection.prepareStatement(sql3);
                ps3.setInt(1, talkerID);
                ps3.setInt(2, groupID);
                ResultSet set2 = ps3.executeQuery();
                set2.next();
                count = set2.getInt(1);
                String sql2 = "update message set isAccept='T' where talkerID=? and groupID=? and talker_type=? and msg_type='A' and isAccept='F'";
                PreparedStatement ps2 = connection.prepareStatement(sql2);
                ps2.setInt(1, msg.getTalkerID());
                ps2.setInt(2, groupID);
                ps2.setString(3, msg.getTalker_type());
                int row=ps2.executeUpdate();
                log.info("ps2.executeUpdate() = {}",row);
            }

            log.info("同类型邀请 count = {}", count);

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean CheckHaveMessage(int userID, String msg_type, int talkerID, String talker_type, int groupID, String content, String isAccept) {
        try {
            Connection connection= jdbcPool.getConnection();
            String sql;
            if (talker_type.equals("G")) {
                sql = "select msg_id from message where userID=? and msg_type=?  and talker_type=? and groupID=? and content=? and isAccept=?";
            } else {
                sql = "select msg_id from message where userID=? and msg_type=?  and talker_type=? and groupID=? and content=? and isAccept=? and talkerID=?";
            }

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setString(2, msg_type);
            ps.setString(3, talker_type);
            ps.setInt(4, groupID);
            ps.setString(5, content);
            ps.setString(6, isAccept);
            if (talker_type.equals("F")) {
                ps.setInt(7, talkerID);
            }

            ResultSet set = ps.executeQuery();
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
