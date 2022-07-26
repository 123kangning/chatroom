package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.GroupCreateRequestMessage;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class GroupCreateHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) {
        try{
            int userID=msg.getUserID();
            String groupName=msg.getGroupName();
            String sql="insert into group1(group_name,userID,user_type,say) values(?,?,'9','T')";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setString(1,groupName);
            ps.setInt(2,userID);
            int row=ps.executeUpdate();
            ResponseMessage message;
            if(row==1){
                message=new ResponseMessage(true,"");
            }else{
                message=new ResponseMessage(false,"创建失败");
            }
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
