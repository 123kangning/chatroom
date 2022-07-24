package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendQueryRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendQueryHandler extends SimpleChannelInboundHandler<FriendQueryRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendQueryRequestMessage msg){
        try{
            int userID=msg.getUserID();
            List<String> friendList=new ArrayList<>();
            String sql="select * from friend where toID=? or fromID=?";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,userID);
            ResultSet set=ps.executeQuery();
            while(set.next()){
                int u1=set.getInt(1);
                int u2=set.getInt(2);
                String shield=set.getString(3);
                String to_name=set.getString(4);
                String from_name=set.getString(5);
                String ans;
                if(u1==userID){
                    ans=String.format("\t用户%5d , %10s ",u2,to_name);
                    if(shield.equals("1")){
                        ans=ans.concat(", 已屏蔽");
                    }
                }else{
                    ans=String.format("\t用户%5d , %10s ",u1,from_name);
                    if(shield.equals("2")){
                        ans=ans.concat(", 已屏蔽");
                    }
                }
                friendList.add(ans);
            }
            ResponseMessage message=new ResponseMessage(true,"");
            message.setFriendList(friendList);
            message.setMessageType(Message.FriendQueryRequestMessage);
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
