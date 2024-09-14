package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.AuthCodeRequestMessage;
import message.ResponseMessage;
import server.session.MailSession;

import java.util.Random;

public class SendAuthCodeHandler extends SimpleChannelInboundHandler<AuthCodeRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthCodeRequestMessage msg) throws Exception {
        String mail=msg.getMail();
        int mailAuthCode = new Random().nextInt(899999) + 100000;
        ResponseMessage message = new ResponseMessage(true, "");
        message.setMailAuthCode(mailAuthCode);
        ctx.writeAndFlush(message);
        MailSession.email(mail, mailAuthCode);
    }
}
