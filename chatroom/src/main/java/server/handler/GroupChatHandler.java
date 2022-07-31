package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendChatRequestMessage;
import message.GroupChatRequestMessage;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupChatHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int groupID=msg.getGroupId();
            String msg_type=msg.getMsg_type();
            String chat=msg.getMessage();
            File file=msg.getFile();
            String sql="select userID from group2 where groupID=? and userID=? and say='T'";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,groupID);
            ps.setInt(2,userID);
            ResultSet set=ps.executeQuery();
            if(!set.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"您已被禁言"));
                return;
            }
            sql="select userID from group2 where groupID=? and userID!=?";
            ps= connection.prepareStatement(sql);
            ps.setInt(1,groupID);
            ps.setInt(2,userID);
            set=ps.executeQuery();
            FriendChatHandler handler=new FriendChatHandler();
            while(set.next()) {
                int FriendID = set.getInt(1);
                String sql1 = "select online from user where userID=?";
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                ps1.setInt(1, FriendID);
                ResultSet set1 = ps1.executeQuery();
                FriendChatRequestMessage message;
                if (msg_type.equals("F")) {
                    message = new FriendChatRequestMessage(userID, FriendID, file, "F");
                } else {
                    message = new FriendChatRequestMessage(userID, FriendID, chat, "S");
                }
                message.setTalker_type("G");
                message.setGroup(groupID);
                ctx.writeAndFlush(handler.ChatHandler(userID, FriendID, msg_type, "G", groupID, chat, file, set1.getString(1), message));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
