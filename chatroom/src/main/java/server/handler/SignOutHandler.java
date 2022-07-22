package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.ResponseMessage;
import message.SignInRequestMessage;
import message.SignOutRequestMessage;
import server.session.SessionMap;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;

@Slf4j
public class SignOutHandler extends SimpleChannelInboundHandler<SignOutRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SignOutRequestMessage msg) throws Exception {
        long userID= SessionMap.getUser(ctx.channel());
        String sql="delete from user where userID=?";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setLong(1,userID);
        int row=statement.executeUpdate();
        log.info("row = {}",row);
        ResponseMessage message;
        if(row==1){
            message=new ResponseMessage(true,"");
        }else{
            message=new ResponseMessage(false,"注销失败");
        }
        ctx.writeAndFlush(message);
        log.info("ctx.writeAndFlush(message)");
    }
}
