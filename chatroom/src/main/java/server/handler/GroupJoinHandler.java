package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupJoinRequestMessage;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupJoinHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int groupID=msg.getGroupId();
            String group_name;
            String sqlCheck="select group_name from group1 where groupID=?";
            PreparedStatement psCheck= connection.prepareStatement(sqlCheck);
            psCheck.setInt(1,groupID);
            ResultSet setCheck=psCheck.executeQuery();
            ResponseMessage message;
            if(!setCheck.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"该群组不存在"));
                return;
            }else{
                group_name= setCheck.getString(1);
            }
            String sql="select groupID from group2 where groupID=? and userID=?";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,groupID);
            ps.setInt(2,userID);
            ResultSet set=ps.executeQuery();
            if(set.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"你已经在群聊中了"));
                return;
            }

            String sql1="insert into group2(groupID,group_name,userID,user_type,say) values(?,?,?,'0','T')";
            PreparedStatement ps1= connection.prepareStatement(sql1);
            ps1.setInt(1,groupID);
            ps1.setString(2,group_name);
            ps1.setInt(3,userID);
            int row=ps1.executeUpdate();
            if(row==1){
                message=new ResponseMessage(true,"");
            }else{
                message=new ResponseMessage(false,"加入群聊失败");
            }
            ctx.writeAndFlush(message);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
