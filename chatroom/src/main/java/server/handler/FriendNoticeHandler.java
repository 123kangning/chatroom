package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendNoticeMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FriendNoticeHandler extends SimpleChannelInboundHandler<FriendNoticeMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendNoticeMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int FriendID=msg.getFriendID();
            int count=msg.getCount();
            String sql="select userID, content from message where talker_type='F' and ((userID=? and talkerID=?)||(userID=? and talkerID=?)) order by msg_id desc limit ?";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.setInt(3,FriendID);
            ps.setInt(4,userID);
            ps.setInt(5,count);
            ResultSet set=ps.executeQuery();
            List<String> messageList=new ArrayList<>();
            while(set.next()){
                messageList.add(((set.getInt(1)==FriendID)?String.format("\t\t\t%30s:%d",set.getString(2),userID):
                        String.format("\t%d:%s",FriendID,set.getString(2))));
            }
            Collections.reverse(messageList);
            ResponseMessage message=new ResponseMessage(true,"");
            message.setFriendList(messageList);
            message.setMessageType(Message.FriendQueryRequestMessage);
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
