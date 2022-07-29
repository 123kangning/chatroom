package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupApplyQueryRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupApplyQueryHandler extends SimpleChannelInboundHandler<GroupApplyQueryRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupApplyQueryRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            String sql="select groupID,talkerID,content from message where userID=? and talker_type ='G' and isAccept='F'";
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ResultSet set=ps.executeQuery();
            List<String> list=new ArrayList<>();
            while(set.next()){
                String content=set.getString(3);
                String people=content.equals("您已被移出群聊")?"执行人 用户":"邀请人 用户";
                String ans=String.format("%s%5d,%s%5d",content,set.getInt(1),people,set.getInt(2));
                list.add(ans);
            }
            ResponseMessage message=new ResponseMessage(true,"");
            message.setFriendList(list);
            message.setMessageType(Message.FriendQueryRequestMessage);
            ctx.writeAndFlush(message);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
