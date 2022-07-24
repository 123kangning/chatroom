package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.FriendDeleteRequestMessage;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FriendDeleteHandler extends SimpleChannelInboundHandler<FriendDeleteRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendDeleteRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int FriendID=msg.getFriendId();
            String sql="delete from friend where (fromID=? and toID=?) or (fromID=? and toID=?)";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.setInt(3,FriendID);
            ps.setInt(4,userID);
            int row=ps.executeUpdate();
            ResponseMessage message;
            if(row==1){
                message=new ResponseMessage(true,"");
            }else{
                message=new ResponseMessage(false,"没有这个朋友");
            }
            ctx.writeAndFlush(message);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
