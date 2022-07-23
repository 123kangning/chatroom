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

import java.sql.Date;
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
            int groupID=msg.getGroup();
            String talker_type=msg.getTalker_type();
            String msg_type=msg.getMsg_type();
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
                String shield= set.getString(3);
                if((u2==userID&&shield.equals("1"))||(u1==userID&&shield.equals("2"))||shield.equals("3")){//处理屏蔽关系
                    ctx.writeAndFlush(new ResponseMessage(true,""));
                    return;
                }
                String sql1="select online from user where userID=? ";
                PreparedStatement ps1=connection.prepareStatement(sql1);
                if(u2==userID){
                    int temp=u1;
                    u1=u2;
                    u2=temp;
                }
               //调整之后，消息从u1发往u2

                ps1.setInt(1,u2);

                ResultSet set1=ps1.executeQuery();
                if(set1.next()){

                    //！！！！！！！！！！！！！！！！
                    //后期需要加上把消息存入数据库的代码
                    //！！！！！！！！！！！！！！！！！
                    String isAccept;
                    if(set1.getString(1).equals("T")){
                        isAccept="T";
                        log.info("new ResponseMessage(true,\"\")");
                        message=new ResponseMessage(true,"");

                        log.info("msg = "+msg);
                        Channel channel= SessionMap.getChannel(u2);
                        log.info("Friend channel = {}，u1={},u2={},userID={},FriendID={}",channel,u1,u2,userID,FriendID);
                        channel.writeAndFlush(msg);
                    }else{
                        isAccept="F";
                        log.info("new ResponseMessage(true,\"但是朋友不在线\")");
                        message=new ResponseMessage(true,"但是朋友不在线");
                    }
                    String sql2="insert into message(userID,msg_type,create_date,talkerID,talker_type,groupID,content,isAccept) values(?,?,?,?,?,?,?,?)";
                    PreparedStatement ps2=connection.prepareStatement(sql2);
                    ps2.setInt(1,u2);
                    ps2.setString(2,msg_type);
                    ps2.setDate(3,new Date(System.currentTimeMillis()));
                    ps2.setInt(4,u1);
                    log.info("msg_type = {}!!! groupID={}!!!",msg_type,msg.getGroup());
                    ps2.setString(5,talker_type);
                    ps2.setInt(6,groupID);
                    ps2.setString(7,chat);
                    ps2.setString(8,"F");
                    int row=ps2.executeUpdate();
                    log.info("row in executeUpdate = "+row);

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
