package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupMemberRequestMessage;
import message.Message;
import message.ResponseMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static server.ChatServer.connection;

public class GroupMemberHandler extends SimpleChannelInboundHandler<GroupMemberRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMemberRequestMessage msg) throws Exception {
        try {
            int groupID = msg.getGroupId();
            String sql = "select userID ,user_type,say from group2 where groupID=? order by user_type desc";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, groupID);
            ResultSet set = ps.executeQuery();
            List<String> list = new ArrayList<>();
            while (set.next()) {
                String ans = String.format("\t用户%5d, ", set.getInt(1));
                switch (Integer.parseInt(set.getString(2))) {
                    case 0:
                        ans = ans.concat(" 成员");
                        break;
                    case 1:
                        ans = ans.concat("管理员");
                        break;
                    case 9:
                        ans = ans.concat(" 群主");
                        break;
                }
                if (set.getString(3).equals("F")) {
                    ans = ans.concat("，已禁言");
                }
                list.add(ans);
            }
            ResponseMessage message = new ResponseMessage(true, "");
            message.setMessageType(Message.FriendQueryRequestMessage);
            message.setFriendList(list);
            ctx.writeAndFlush(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
