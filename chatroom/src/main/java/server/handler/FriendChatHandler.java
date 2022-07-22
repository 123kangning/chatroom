package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.ResponseMessage;
import server.service.User;
import server.session.SessionMap;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class FriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        try{
            int userID=msg.getUserID();
            int FriendID=msg.getFriendId();
            String chat=msg.getMessage();
            String sql="select * from friend where (toID =? and fromID=?) or (toID =? and fromID=?)";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,userID);
            ps.setInt(2,FriendID);
            ps.setInt(3,FriendID);
            ps.setInt(4,userID);
            ResultSet set=ps.executeQuery();
            ResponseMessage message=null;
            if(set.next()){//找到添加的朋友
                int u1=set.getInt(1);
                int u2=set.getInt(2);
                String sql1="select online from test1 where userID=?";
                PreparedStatement ps1=connection.prepareStatement(sql1);
                Channel channel;
                if(u2==userID){//u1是朋友
                    int temp=u1;
                    u1=u2;
                    u2=temp;
                }//调整之后，消息从u1发往u2
                ps1.setInt(1,u2);
                channel= SessionMap.getChannel(u2);

                ResultSet set1=ps1.executeQuery();
                if(set1.next()){
                    //！！！！！！！！！！！！！！！！
                    //后期需要加上把消息存入数据库的代码
                    //！！！！！！！！！！！！！！！！！

                    if(set1.getString(1).equals("T")){
                        log.info("new ResponseMessage(true,\"\")");
                        message=new ResponseMessage(true,"");
                        log.info("Friend channel = "+channel);
                        log.info("msg = "+msg);
                        channel.writeAndFlush(msg);
                    }else{
                        log.info("new ResponseMessage(true,\"但是朋友不在线\")");
                        message=new ResponseMessage(true,"但是朋友不在线");
                    }
                }
            }else{//未找到
                log.info("new ResponseMessage(false,\"找不到该朋友\")");
                message=new ResponseMessage(false,"找不到该朋友");
            }
            ctx.writeAndFlush(message);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
