package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupQuitRequestMessage;
import message.ResponseMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.connection;

public class GroupQuitHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        try{
            int delID=msg.getDelID();
            int groupID=msg.getGroupId();
            String sql="select groupID from group2 where groupID=? and userID=?";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,groupID);
            ps.setInt(2,delID);
            ResultSet set=ps.executeQuery();
            if(!set.next()){
                ctx.writeAndFlush(new ResponseMessage(false, "群聊中无此人"));
                return;
            }
            String sql1="delete from group2 where userID=? and groupID=?";
            PreparedStatement ps1= connection.prepareStatement(sql1);
            ps1.setInt(1,delID);
            ps1.setInt(2,groupID);
            int row=ps1.executeUpdate();
            ResponseMessage message;
            if(row==1){
                message=new ResponseMessage(true,"");
            }else{
                message=new ResponseMessage(false,"踢成员失败");
            }
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
