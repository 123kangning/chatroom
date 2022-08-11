package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupUnSayRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.jdbcPool;

public class GroupUnSayHandler extends SimpleChannelInboundHandler<GroupUnSayRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupUnSayRequestMessage msg) throws Exception {
        try {
            Connection connection= jdbcPool.getConnection();
            int unSayID = msg.getUnSayID();
            int userID=msg.getUserID();
            int groupID = msg.getGroupID();
            String say = msg.getSay();
            String sql = "select groupID from group2 where groupID=? and userID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, groupID);
            ps.setInt(2, unSayID);
            ResultSet set = ps.executeQuery();
            if (!set.next()) {
                ctx.writeAndFlush(new ResponseMessage(false, "群聊中无此人"));
                return;
            }
            String sql0="select userID from group2 where userID=? and  user_type<(select user_type from group2 where userID=? and groupID=?)";
            PreparedStatement ps0= connection.prepareStatement(sql0);
            ps0.setInt(1,unSayID);
            ps0.setInt(2,userID);
            ps0.setInt(3,groupID);
            ResultSet set0=ps0.executeQuery();
            if(!set0.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"您没有权限禁言该成员"));
                return;
            }

            String sql1 = "update group2 set say=? where groupID=? and userID=?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setString(1, say);
            ps1.setInt(2, groupID);
            ps1.setInt(3, unSayID);
            int row = ps1.executeUpdate();
            ResponseMessage message;
            if (row == 1) {
                message = new ResponseMessage(true, "");
            } else {
                message = new ResponseMessage(false, "禁言失败");
            }
            ctx.writeAndFlush(message);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
