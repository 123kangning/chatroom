package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupQueryRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupQueryHandler extends SimpleChannelInboundHandler<GroupQueryRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQueryRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            String sql="select groupID,group_name,user_type from group2 where userID=?";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ResultSet set=ps.executeQuery();
            List<String> list=new ArrayList<>();
            while(set.next()){
                String ans=String.format("\t群组%5d ,群名:%10s ",set.getInt(1),set.getString(2));
                String user_type=set.getString(3);
                if(user_type.equals("9")){
                    ans=ans.concat("，群主");
                }else if(user_type.equals("1")){
                    ans=ans.concat("，管理员");
                }else{
                    ans=ans.concat("，普通成员");
                }
                list.add(ans);
            }
            ResponseMessage message=new ResponseMessage(true,"");
            message.setFriendList(list);
            message.setMessageType(Message.FriendQueryRequestMessage);
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
