package server.handler;
import static server.ChatServer.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.GroupDeleteRequestMessage;
import message.ResponseMessage;
import server.session.SessionMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Slf4j
public class GroupDeleteHandler extends SimpleChannelInboundHandler<GroupDeleteRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupDeleteRequestMessage msg) throws Exception {
        int userID=msg.getUserID();
        int groupID= msg.getGroupId();
        String sql="select userID from group2 where groupID=? and user_type!='9'";
        PreparedStatement ps= connection.prepareStatement(sql);
        ps.setInt(1,groupID);
        ResultSet set=ps.executeQuery();
        while(set.next()){
            new GroupQuitHandler().SendInsertIntoMessage(set.getInt(1),userID,"G",groupID,"该群聊已被解散","F");
            log.info("发送解散消息");
            Channel channel= SessionMap.getChannel(set.getInt(1));
            if(channel!=null)
                channel.writeAndFlush(new FriendChatRequestMessage());
        }

        sql="delete from group2 where groupID=?";
        ps= connection.prepareStatement(sql);
        ps.setInt(1,groupID);
        ps.executeUpdate();
        log.info("delete in group2");
        sql="delete from group1 where groupID=?";
        ps= connection.prepareStatement(sql);
        ps.setInt(1,groupID);
        ps.executeUpdate();
        log.info("delete in group1");
        ctx.writeAndFlush(new ResponseMessage());
    }
}
