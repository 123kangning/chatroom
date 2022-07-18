package procotol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import message.LoginRequestMessage;

public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel=new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,10,4,2,0),
                new LoggingHandler(LogLevel.DEBUG),new MessageCodec());
        //测试encode
        LoginRequestMessage message=new LoginRequestMessage("张三","zhangSan");
        channel.writeOutbound(message);
        //测试decode
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null,message,buf);
        buf.readByte();

        channel.writeInbound(buf);

    }
}
