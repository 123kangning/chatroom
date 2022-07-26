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
            String sql="select fromID from friend where (fromID=? and toID=?) or (fromID=? and toID=?)";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.setInt(3,FriendID);
            ps.setInt(4,userID);
            ResultSet set=ps.executeQuery();
            if(!set.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"朋友不存在"));
                return;
            }
            String sql1="select userID, content from message where (msg_type='F' or msg_type='S') and talker_type='F' and ((userID=? and talkerID=?)||(userID=? and talkerID=?)) order by msg_id desc limit ?";
            PreparedStatement ps1= connection.prepareStatement(sql1);
            ps1.setInt(1,userID);
            ps1.setInt(2,FriendID);
            ps1.setInt(3,FriendID);
            ps1.setInt(4,userID);
            ps1.setInt(5,count);
            set=ps1.executeQuery();
            List<String> messageList=new ArrayList<>();
            while(set.next()){
                messageList.add(((set.getInt(1)==FriendID)?String.format("\t\t\t%50s:%d",set.getString(2),userID):
                        String.format("\t%d:%s",FriendID,set.getString(2))));
            }
            Collections.reverse(messageList);
            ResponseMessage message=new ResponseMessage(true,"");
            message.setFriendList(messageList);
            message.setMessageType(Message.FriendQueryRequestMessage);
            sql="update message set isAccept ='T' where userID=? and talker_type='F' and (msg_type='F' or msg_type='S') and talkerID=?";
            ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.executeUpdate();
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
