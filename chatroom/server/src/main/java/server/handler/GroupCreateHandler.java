package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.GroupCreateRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.jdbcPool;

@Slf4j
public class GroupCreateHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) {
        try {
            Connection connection= jdbcPool.getConnection();
            int userID = msg.getUserID();
            String groupName = msg.getGroupName();
            String sql1 = "insert into group1(group_name,ownerID) values(?,?)";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setString(1, groupName);
            ps1.setInt(2, userID);
            int row = ps1.executeUpdate();
            log.info("第一次 row={}", row);
            String sql2 = "select groupID from group1 order by groupID desc limit 1";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ResultSet set = ps2.executeQuery();
            int groupID = 0;
            if (set.next()) {
                groupID = set.getInt(1);
            }
            String sql = "insert into group2(groupID,group_name,userID,user_type,say) values(?,?,?,'9','T')";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, groupID);
            ps.setString(2, groupName);
            ps.setInt(3, userID);
            row = ps.executeUpdate();
            log.info("第二次 row={}", row);
            ResponseMessage message;
            if (row == 1) {
                message = new ResponseMessage(true, "");
            } else {
                message = new ResponseMessage(false, "创建失败");
            }
            ctx.writeAndFlush(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
