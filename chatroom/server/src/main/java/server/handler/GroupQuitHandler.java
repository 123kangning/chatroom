package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.GroupQuitRequestMessage;
import message.ResponseMessage;
import server.session.SessionMap;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.connection;

@Slf4j
public class GroupQuitHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        try {
            log.info("enter GroupQuitHandler");
            int delID = msg.getDelID();
            int userID = msg.getUserID();
            int groupID = msg.getGroupId();
            String sql = "select say from group2 where groupID=? and userID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, groupID);
            ps.setInt(2, delID);
            ResultSet set = ps.executeQuery();
            log.info("1 run");
            if (!set.next()) {
                ctx.writeAndFlush(new ResponseMessage(false, "群聊中无此人"));
                log.info("群聊中无此人");
                return;
            }
            String sql0;
            if(userID!=delID){
                sql0="select userID from group2 where userID=? and  user_type<(select user_type from group2 where userID=? and groupID=?)";
                PreparedStatement ps0= connection.prepareStatement(sql0);
                ps0.setInt(1,delID);
                ps0.setInt(2,userID);
                ps0.setInt(3,groupID);
                ResultSet set0=ps0.executeQuery();
                if(!set0.next()){
                    ctx.writeAndFlush(new ResponseMessage(false,"您没有权限踢出该成员"));
                    return;
                }
            }

            String sql1 = "delete from group2 where userID=? and groupID=?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, delID);
            ps1.setInt(2, groupID);
            int row = ps1.executeUpdate();
            log.info("2 run");
            ResponseMessage message;
            if (row == 1) {
                message = new ResponseMessage(true, "");
                log.info("new ResponseMessage(true,\"\")");
            } else {
                message = new ResponseMessage(false, "踢成员失败");
                log.info("new ResponseMessage(false,\"踢成员失败\")");
            }
            ctx.writeAndFlush(message);
            if (userID != delID) {
                row = SendInsertIntoMessage(delID, userID, "G", groupID, "您已被移出群聊", "F");
                Channel channel = SessionMap.getChannel(delID);
                if (channel != null)
                    channel.writeAndFlush(new FriendChatRequestMessage());
            }

            log.info("row=SendInsertIntoMessage = {}", row);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int SendInsertIntoMessage(int userID, int talkerID, String talker_type, int groupID, String content, String isAccept) throws SQLException {
        String sql = "insert into message(userID,msg_type,create_date,talkerID,talker_type,groupID,content,isAccept) values(?,'A',?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setDate(2, new Date(System.currentTimeMillis()));
        ps.setInt(3, talkerID);
        ps.setString(4, talker_type);
        ps.setInt(5, groupID);
        ps.setString(6, content);
        ps.setString(7, isAccept);
        return ps.executeUpdate();
    }
}
