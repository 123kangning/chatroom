package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendApplyQueryMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class FriendApplyQueryHandler extends SimpleChannelInboundHandler<FriendApplyQueryMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendApplyQueryMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            String sql="select talkerID from message where userID=? and talker_type='F' and msg_type='A' and isAccept='F'";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ResultSet set=ps.executeQuery();
            ResponseMessage message;
            List<String> friendList=new ArrayList<>();
            while(set.next()){
                log.info(String.format("\t用户%5d 请求添加你为好友",set.getInt(1)));
                friendList.add(String.format("\t用户%5d 请求添加你为好友",set.getInt(1)));
            }
            message=new ResponseMessage(true,"");
            message.setFriendList(friendList);
            message.setMessageType(Message.FriendQueryRequestMessage);
            ctx.writeAndFlush(message);
            sql="update message set isAccept='T' where userID=? and talker_type='F' and msg_type='A' and isAccept='F'";
            ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.executeUpdate();
            log.info("ctx.writeAndFlush(message) FriendApplyQueryHandler");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
