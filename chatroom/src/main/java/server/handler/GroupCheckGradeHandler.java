package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupCheckGradeRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupCheckGradeHandler extends SimpleChannelInboundHandler<GroupCheckGradeRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCheckGradeRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int groupID=msg.getGroupID();
            String sql="select user_type from group2 where groupID=? and userID=?";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,groupID);
            ps.setInt(2,userID);
            ResultSet set=ps.executeQuery();
            ResponseMessage message;
            if(set.next()){
                message=new ResponseMessage(true,"");
                message.setMessageType(Message.CheckGradeInGroup);
                message.setGradeInGroup(Integer.parseInt(set.getString(1)));
            }else{
                message=new ResponseMessage(false,"判断失败");
            }
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
