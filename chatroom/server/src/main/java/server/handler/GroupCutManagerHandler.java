package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.GroupCutManagerRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static server.ChatServer.jdbcPool;

@Slf4j
public class GroupCutManagerHandler extends SimpleChannelInboundHandler<GroupCutManagerRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCutManagerRequestMessage msg) throws Exception {
        Connection connection= jdbcPool.getConnection();
        int managerID = msg.getManagerID();
        int groupID = msg.getGroupID();
        String sql = "select groupID from group2 where userID=? and user_type='1' and groupID=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, managerID);
        ps.setInt(2, groupID);
        ResultSet set = ps.executeQuery();
        if (!set.next()) {
            ctx.writeAndFlush(new ResponseMessage(false, "不存在该管理员"));
            return;
        }
        sql = "update group2 set user_type='0' where userID=? and groupID=?";
        ps = connection.prepareStatement(sql);
        ps.setInt(1, managerID);
        ps.setInt(2, groupID);
        int row = ps.executeUpdate();
        log.info("row={}", row);
        ctx.writeAndFlush(new ResponseMessage(true, ""));
        connection.close();
    }
}
