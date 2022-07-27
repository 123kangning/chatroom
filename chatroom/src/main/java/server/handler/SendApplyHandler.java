package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendChatRequestMessage;
import message.ResponseMessage;
import message.SendApplyMessage;
import server.session.SessionMap;

import static server.ChatServer.connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SendApplyHandler extends SimpleChannelInboundHandler<SendApplyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendApplyMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int friendID=msg.getFriendID();
            int groupID=msg.getGroupID();
            String talker_type=msg.getTalker_type();
            String content=msg.getMessage();
            String s1="select toID from friend where (fromID=? and toID=?) or (toID=? and fromID=?)";
            PreparedStatement check= connection.prepareStatement(s1);
            check.setInt(1,userID);
            check.setInt(2,friendID);
            check.setInt(3,userID);
            check.setInt(4,friendID);
            ResultSet checkSet=check.executeQuery();
            if(checkSet.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"该用户已经是您的朋友了"));
                return;
            }
            String sql="insert into message(userID,msg_type,create_date,talkerID,talker_type,groupID,content,isAccept) values(?,'A',?,?,?,?,?,'F')";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,friendID);
            ps.setDate(2,new Date(System.currentTimeMillis()));
            ps.setInt(3,userID);
            ps.setString(4,talker_type);
            ps.setInt(5,groupID);
            ps.setString(6,content);
            int row=ps.executeUpdate();
            ResponseMessage message;
            if(row==1){
                message=new ResponseMessage(true,"");
                Channel channel= SessionMap.getChannel(friendID);
                if(channel!=null)
                    channel.writeAndFlush(new FriendChatRequestMessage());
            }else{
                message=new ResponseMessage(false,"申请失败");
            }
            ctx.writeAndFlush(message);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
