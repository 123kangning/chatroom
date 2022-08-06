package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendNoticeMessage;
import message.Message;
import message.ResponseMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static server.ChatServer.connection;

@Slf4j
public class FriendNoticeHandler extends SimpleChannelInboundHandler<FriendNoticeMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendNoticeMessage msg) throws Exception {
        try {
            int userID = msg.getUserID();
            int FriendID = msg.getFriendID();
            int count = msg.getCount();
            String sql = "select fromID from friend where (fromID=? and toID=?) or (fromID=? and toID=?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, FriendID);
            ps.setInt(3, FriendID);
            ps.setInt(4, userID);
            ResultSet set = ps.executeQuery();
            if (!set.next()) {
                ctx.writeAndFlush(new ResponseMessage(false, "朋友不存在"));
                return;
            }
            String sql1 = "select userID, content,isAccept,msg_type from message where (msg_type='F' or msg_type='S') and talker_type='F' and ((userID=? and talkerID=?)||(userID=? and talkerID=?)) order by msg_id desc limit ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, userID);
            ps1.setInt(2, FriendID);
            ps1.setInt(3, FriendID);
            ps1.setInt(4, userID);
            ps1.setInt(5, count);
            set = ps1.executeQuery();
            List<String> messageList = new ArrayList<>();
            int count1 = 0;
            StringBuilder ans = new StringBuilder();

            while (set.next()) {
                if (set.getString(3).equals("F") && set.getInt(1) == userID) {//统计未读消息条数
                    count1++;
                }
                if (set.getString(4).equals("F") && set.getString(3).equals("F")) {//标记未处理文件
                    ans.append("1");
                } else {
                    ans.append("0");
                }
                String m=set.getString(2);
                if(set.getString(4).equals("F")){
                    for(int i=m.length()-1;i>=0;i--){
                        if(m.charAt(i)=='/'){
                            m=m.substring(i+1);
                            break;
                        }
                    }
                }
                messageList.add(((set.getInt(1) == FriendID) ? String.format("\t\t\t%80s:%d", m, userID) :
                        String.format("\t%d:%s", FriendID, m)));
                log.info(((set.getInt(1) == FriendID) ? String.format("\t\t\t%80s:%d", m, userID) :
                        String.format("\t%d:%s", FriendID, m)));
                log.info("ans = {}", ans);
            }
            Collections.reverse(messageList);
            ans.reverse();
            ResponseMessage message = new ResponseMessage(true, "");
            message.setFriendList(messageList);
            message.setMessageType(Message.FriendQueryRequestMessage);
            message.setHaveFile(String.valueOf(ans));
            log.info("finally ans = {}", ans);
            sql = "update message set isAccept ='T' where userID=? and talker_type='F' and msg_type='S' and talkerID=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, FriendID);
            ps.executeUpdate();

            log.info("count in FriendNoticeHandler = {}", count1);
            message.setReadCount(count1);
            ctx.writeAndFlush(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
