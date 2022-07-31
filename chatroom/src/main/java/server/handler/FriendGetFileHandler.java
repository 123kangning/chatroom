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
            boolean isGroup=msg.isGroup();
            int groupID=msg.getGroupID();
            String sql;
            PreparedStatement ps;
            ResultSet set;
            if(isGroup){
                sql="select content,msg_id from message where userID=? and talkerID =? and talker_type='F' and msg_type=? and isAccept='F' and groupID=?";
                ps= connection.prepareStatement(sql);
                ps.setInt(1,userID);
                ps.setInt(2,FriendID);
                ps.setString(3,"G");
                ps.setInt(4,groupID);
            }else{
                sql="select content,msg_id from message where userID=? and talkerID =? and talker_type='F' and msg_type=? and isAccept='F'";
                ps= connection.prepareStatement(sql);
                ps.setInt(1,userID);
                ps.setInt(2,FriendID);
                ps.setString(3,"F");
            }
            set=ps.executeQuery();

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
                String sql1="update message set isAccept ='T' where msg_id =?";
                PreparedStatement ps1= connection.prepareStatement(sql1);
                ps1.setInt(1,set.getInt(2));
                ps1.executeUpdate();
                /*sql="update message set isAccept ='T' where msg_id=?";
                ps= connection.prepareStatement(sql);
                ps.setInt(1,set.getInt(2));
                ps.executeUpdate();*/
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
