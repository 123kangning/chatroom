package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.GroupAddManagerRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static server.ChatServer.jdbcPool;

@Slf4j
public class GroupAddManagerHandler extends SimpleChannelInboundHandler<GroupAddManagerRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupAddManagerRequestMessage msg) throws Exception {
        Connection connection= jdbcPool.getConnection();
        int managerID = msg.getManagerID();
        int groupID = msg.getGroupID();
        String sql = "update group2 set user_type='1' where userID=? and groupID=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, managerID);
        ps.setInt(2, groupID);
        int row = ps.executeUpdate();
        log.info("row={}", row);
        ctx.writeAndFlush(new ResponseMessage(true, ""));
    }
}
