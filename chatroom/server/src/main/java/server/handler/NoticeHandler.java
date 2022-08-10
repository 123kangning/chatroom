package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.Message;
import message.NoticeRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static server.ChatServer.jdbcPool;

@Slf4j
public class NoticeHandler extends SimpleChannelInboundHandler<NoticeRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NoticeRequestMessage msg) {
        try {
            Connection connection= jdbcPool.getConnection();
            int userID = msg.getUserID();
            String sql = "select  talkerID ,count(1) from message  where userID=? and talker_type='F' and isAccept ='F' group by talkerID";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet set = ps.executeQuery();
            Map<String, List<String>> noticeMap = new HashMap<>();
            List<String> friend = new ArrayList<>();
            List<String> group = new ArrayList<>();
            while (set.next()) {
                if (set.getInt(2) == 0) {
                    break;
                }
                friend.add(String.format("\t用户%8d 发来%3d 条消息", set.getInt(1), set.getInt(2)));
                log.info(String.format("\t用户%8d 发来%3d 条消息", set.getInt(1), set.getInt(2)));
            }
            String sql1 = "select groupID ,count(1) from message  where userID=? and talker_type='G' and isAccept='F' group by groupID";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, userID);
            ResultSet set1 = ps1.executeQuery();
            /*log.info("set.next={}",set1.next());*/
            while (set1.next()) {
                if (set1.getInt(2) == 0) {
                    break;
                }
                group.add(String.format("\t群组%8d 发来%3d 条消息", set1.getInt(1), set1.getInt(2)));
                log.info(String.format("\t群组%8d 发来%3d 条消息", set1.getInt(1), set1.getInt(2)));
            }
            noticeMap.put("F", friend);
            noticeMap.put("G", group);
            ResponseMessage message = new ResponseMessage(true, "");
            message.setNoticeMap(noticeMap);
            message.setMessageType(Message.noticeMapMessage);
            ctx.writeAndFlush(message);
            log.info("ctx.writeAndFlush(noticeMap)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
