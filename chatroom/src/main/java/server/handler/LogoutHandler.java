package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.LogoutRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogoutHandler extends SimpleChannelInboundHandler<LogoutRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestMessage msg){
        try{
            long userID=msg.getUserID();
            String sql="update test1 set online='F' where userID=?";
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setLong(1,userID);
            statement.executeUpdate();
            sql="select online from test1 where userID=?";
            statement=connection.prepareStatement(sql);
            statement.setLong(1,userID);
            ResultSet set=statement.executeQuery();
            ResponseMessage message;
            if(set.next()&&set.getString(1).equals("T")){
                message=new ResponseMessage(false,"退出失败");
            }else{
                message=new ResponseMessage(true,"");
            }
            message.setMessageType(Message.LogoutResponseMessage);
            ctx.writeAndFlush(message);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
