package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.ResponseMessage;
import server.session.SessionMap;

import java.io.IOException;
import java.sql.*;

import static server.ChatServer.jdbcPool;


@Slf4j
public class FriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        try {
            Connection connection= jdbcPool.getConnection();
            int userID = msg.getUserID();
            int FriendID = msg.getFriendId();
            int groupID = msg.getGroup();
            String talker_type = msg.getTalker_type();
            String msg_type = msg.getMsg_type();
            String chat = msg.getMessage();

            String sql = "select * from friend where (toID =? and fromID=?) or (toID =? and fromID=?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, FriendID);
            ps.setInt(3, FriendID);
            ps.setInt(4, userID);
            ResultSet set = ps.executeQuery();
            ResponseMessage message = null;
            if (set.next()) {//找到添加的朋友
                int u1 = set.getInt(1);
                int u2 = set.getInt(2);
                String shield = set.getString(3);
                if ((u2 == userID && shield.equals("1")) || (u1 == userID && shield.equals("2")) || shield.equals("3")) {//处理屏蔽关系
                    ctx.writeAndFlush(new ResponseMessage(true, ""));
                    return;
                }
                String sql1 = "select online from user where userID=? ";
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                if (u2 == userID) {
                    int temp = u1;
                    u1 = u2;
                    u2 = temp;
                }
                //调整之后，消息从u1发往u2

                ps1.setInt(1, u2);

                ResultSet set1 = ps1.executeQuery();
                if (set1.next()) {
                    message = ChatHandler(u1, u2, msg_type, talker_type, groupID, chat,msg.getPath(),set1.getString(1));
                }
            } else {//未找到
                log.info("new ResponseMessage(false,\"找不到该朋友\")");
                message = new ResponseMessage(false, "找不到该朋友");
            }
            ctx.writeAndFlush(message);
            connection.close();
        } catch (SQLException | IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ResponseMessage ChatHandler(int fromID, int toID, String msg_type, String talker_type, int groupID, String chat,String path, String onLine) throws Exception {
        Connection connection= jdbcPool.getConnection();
        ResponseMessage message;
        String sql2 = "insert into message(userID,msg_type,create_date,talkerID,talker_type,groupID,content,isAccept) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps2 = connection.prepareStatement(sql2);
        ps2.setInt(1, toID);
        ps2.setString(2, msg_type);
        ps2.setDate(3, new Date(System.currentTimeMillis()));
        ps2.setInt(4, fromID);

        ps2.setString(5, talker_type);
        ps2.setInt(6, groupID);

        FriendChatRequestMessage msg = new FriendChatRequestMessage(fromID, toID, chat, msg_type);
        msg.setTalker_type(talker_type);
        msg.setGroup(groupID);
        if (talker_type.equals("G")) {
            msg.setPrefix(String.format("%3s %d:", new GroupNoticeHandler().Id2Type(fromID, groupID,connection), fromID));
        } else {
            msg.setPrefix(String.format("\t%d :", fromID));
        }

        if (msg_type.equals("S")) {
            ps2.setString(7, chat);
        } else {
            if (msg_type.equals("F")) {
                log.info("chat = {}", path);
                ps2.setString(7, path);
                msg.setMessage(path);
                msg.setMsg_type("F");
                msg.setFile(null);
                msg.setPath(path);
            }
        }

        //！！！！！！！！！！！！！！！！
        //后期需要加上把消息存入数据库的代码
        //！！！！！！！！！！！！！！！！！

        String isAccept;
        if (onLine.equals("T")) {
            isAccept = "T";
            log.info("new ResponseMessage(true,\"\")");
            message = new ResponseMessage(true, "");

            log.info("msg = " + msg);
            Channel channel = SessionMap.getChannel(toID);
            if (channel != null) {
                channel.writeAndFlush(msg);
            }
        } else {
            isAccept = "F";
            log.info("new ResponseMessage(true,\"但是朋友不在线\")");
            message = new ResponseMessage(true, "但是朋友不在线");
        }
        if (msg_type.equals("F")) {
            ps2.setString(8, "F");
        } else {
            ps2.setString(8, isAccept);
        }

        int row = ps2.executeUpdate();
        connection.close();
        return message;
    }
}
