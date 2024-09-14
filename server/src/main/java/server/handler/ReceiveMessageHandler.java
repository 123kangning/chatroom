package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.ReceiveMessageRequestMessage;
import message.ResponseMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static server.ChatServer.jdbcPool;

@Slf4j
public class ReceiveMessageHandler extends SimpleChannelInboundHandler<ReceiveMessageRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ReceiveMessageRequestMessage msg) throws Exception {
        Connection connection= jdbcPool.getConnection();
        for (int msg_id : msg.getList()) {
            String sql = "update message set isAccept='T' where msg_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, msg_id);
            int row = ps.executeUpdate();
            log.info("接收消息成功");
        }
        ResponseMessage message = new ResponseMessage(true, "");
        message.setReadCount(1);
        ctx.writeAndFlush(message);
        connection.close();
    }
}
