package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendAddRequestMessage;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendAddHandler extends SimpleChannelInboundHandler<FriendAddRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendAddRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserId();
            int FriendID=msg.getFriendId();
            String username="";
            String friendName="";
            ResponseMessage message;
            String s1="select toID from friend where (fromID=? and toID=?) or (toID=? and fromID=?)";
            PreparedStatement check= connection.prepareStatement(s1);
            check.setInt(1,userID);
            check.setInt(2,FriendID);
            check.setInt(3,userID);
            check.setInt(4,FriendID);
            ResultSet checkSet=check.executeQuery();
            if(checkSet.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"该用户已经是您的朋友了"));
                return;
            }
            String s="select username from user where userID=?";
            PreparedStatement ps=connection.prepareStatement(s);
            ps.setInt(1,userID);
            ResultSet set=ps.executeQuery();
            if(set.next()){
                username=set.getString(1);
            }
            ps.setInt(1,FriendID);
            set=ps.executeQuery();
            if(set.next()){
                friendName=set.getString(1);
            }else{
                message=new ResponseMessage(false,"该用户不存在");
                ctx.writeAndFlush(message);
                return;
            }
            String sql="insert into  friend(fromID,toID,shield,to_name,From_name) values(?,?,'0',?,?)";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.setString(3,friendName);
            ps.setString(4,username);
            int row=ps.executeUpdate();
            if(row==1){
                message=new ResponseMessage(true,"");
            }else{
                message=new ResponseMessage(false,"添加失败");
            }
            ctx.writeAndFlush(message);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
