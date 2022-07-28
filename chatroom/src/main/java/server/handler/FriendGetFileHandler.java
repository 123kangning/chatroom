package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.FriendGetFileRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
public class FriendGetFileHandler extends SimpleChannelInboundHandler<FriendGetFileRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendGetFileRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int FriendID=msg.getFriendID();
            String sql="select content from message where userID=? and talkerID =? and talker_type='F' and msg_type=? and isAccept='F'";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.setString(3,"F");
            ResultSet set=ps.executeQuery();
            if(set.next()){
                File file=new File(set.getString(1));
                log.info("file绝对路径：{}",file.getAbsolutePath());
                log.info("exist = {}",file.exists());
                ResponseMessage message=new ResponseMessage(true,"");
                if(!msg.isRefuse()){
                    message.setFile(file);
                    message.setMessageType(Message.FriendGetFileRequestMessage);
                }
                ctx.writeAndFlush(message);
                log.info("服务端文件发送成功");
                String sql1="update message set isAccept ='T' where content =?";
                PreparedStatement ps1= connection.prepareStatement(sql1);
                ps1.setString(1,set.getString(1));
                ps1.executeUpdate();
                sql="update message set isAccept ='T' where userID=? and talker_type='F' and (msg_type='F' or msg_type='S') and talkerID=?";
                ps= connection.prepareStatement(sql);
                ps.setInt(1,userID);
                ps.setInt(2,FriendID);
                ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
