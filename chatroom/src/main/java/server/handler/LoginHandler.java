package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.LoginRequestMessage;
import message.Message;
import message.ResponseMessage;
import server.session.SessionMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.connection;

@Slf4j
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestMessage>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) {
        try{
            log.info("run LoginRequestMessage");
            int userID=msg.getUserID();
            String password=msg.getPassword();
            log.info("{}",msg);
            ResponseMessage message;
            String sql="select online from user where userID=? and password=? ";
            log.info("connection = {}",connection);
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setLong(1,userID);
            statement.setObject(2,password);
            ResultSet set= statement.executeQuery();

            if(set.next()){
                if(set.getString(1).equals("T")){
                    log.info("已登录，准备强制登录");
                    Channel channel=SessionMap.getChannel(userID);
                    channel.close();
                }
                    log.info("登录成功");
                    sql="update user set online='T' where  userID=?";
                    statement=connection.prepareStatement(sql);
                    statement.setLong(1,userID);
                    statement.executeUpdate();
                    String sql1="select msg_ID from message where userID=? and isAccept=?";
                    PreparedStatement ps= connection.prepareStatement(sql1);
                    ps.setInt(1,userID);
                    ps.setString(2,"F");
                    set=ps.executeQuery();
                    /*if(set1.next()){
                        ctx.writeAndFlush(new FriendChatRequestMessage());
                    }*/
                    message=new ResponseMessage(true,"");

            }else{
                log.info("登录失败");
                message=new ResponseMessage(false,"登录失败,ID或密码错误");
            }
            SessionMap.add(userID,ctx.channel());
            log.info("userID={},channel={}",userID,SessionMap.getChannel(userID));

            int count=0;
            while(set.next())count++;
            if(message.getSuccess()){
                FriendChatRequestMessage message1=new FriendChatRequestMessage();
                message1.setCount(count);
                ctx.writeAndFlush(message1);
            }
            message.setMessageType(Message.LoginResponseMessage);
            ctx.writeAndFlush(message);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
