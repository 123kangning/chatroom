package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.Message;
import message.ResponseMessage;
import message.SignInRequestMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.ChatServer.jdbcPool;

@Slf4j
public class SignInHandler extends SimpleChannelInboundHandler<SignInRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SignInRequestMessage msg) throws Exception {
        try {
            Connection connection= jdbcPool.getConnection();
            log.info("run SignInRequestMessage");
            String username = msg.getUsername();
            String password = msg.getPassword();
            String mail = msg.getMail();
            log.info("{}", msg);
            ResponseMessage message;
            String sql = "select * from user where mail=? ";
            log.info("connection = {}", connection);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, mail);

            ResultSet set = statement.executeQuery();
            /*if(set==null){
                log.info("set == null,没找到");
            }else{
                log.info("next 不为空！！！！！！！！！！");
            }*/
            if (set.next()) {
                log.info("注册失败");
                message = new ResponseMessage(false, "注册失败,每个邮箱只能申请一个账号");
            } else {
                log.info("注册成功");
                int userID;

                String sql1 = "insert into user(username,password,mail,online) values(?,?,?,?)";
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                statement1.setString(1, username);
                statement1.setString(2, password);
                statement1.setString(3, mail);
                statement1.setString(4, "F");
                statement1.executeUpdate();
                String sql2 = "select userID from user where mail=?";
                PreparedStatement statement2 = connection.prepareStatement(sql2);
                statement2.setString(1, mail);
                ResultSet set1 = statement2.executeQuery();

                set1.next();
                userID = set1.getInt(1);
                message = new ResponseMessage(true, "您的id为" + userID);
            }
            message.setMessageType(Message.SignInResponseMessage);
            ctx.writeAndFlush(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
