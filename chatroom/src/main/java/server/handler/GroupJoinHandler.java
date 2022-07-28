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
        ResponseMessage message;
        int count=0;
        if(msg.getSetList()){
            for(int groupID:msg.getList()){
                count+=choice(ctx,msg,groupID);
            }
        }else{
            count+=choice(ctx,msg,msg.getGroupId());
        }
        message=new ResponseMessage(true,"");
        message.setReadCount(count);
        ctx.writeAndFlush(message);
    }
    protected int choice(ChannelHandlerContext ctx,GroupJoinRequestMessage msg,int groupID){
        try{
            int userID=msg.getUserID();
            String group_name;
            String sqlCheck="select group_name from group1 where groupID=?";
            PreparedStatement psCheck= connection.prepareStatement(sqlCheck);
            psCheck.setInt(1,groupID);
            ResultSet setCheck=psCheck.executeQuery();
            ResponseMessage message;
            if(!setCheck.next()){
                ctx.writeAndFlush(new ResponseMessage(false,"该群组不存在"));
                return 0;
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
                return 0;
            }
            if(!msg.getNoAdd()){
                String sql1="insert into group2(groupID,group_name,userID,user_type,say) values(?,?,?,'0','T')";
                PreparedStatement ps1= connection.prepareStatement(sql1);
                ps1.setInt(1,groupID);
                ps1.setString(2,group_name);
                ps1.setInt(3,userID);
                ps1.executeUpdate();
            }

            sql="update message set isAccept='T' where userID=? and talkerID=? and talker_type='G' and msg_type='A' and isAccept='F'";
            ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,groupID);
            ps.executeUpdate();
            return 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 1;
    }
}
