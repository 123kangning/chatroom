package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import message.LoginRequestMessage;

public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                //帧解码器在检测到该消息为半包时不会立即发送该消息，而是等待后续数据传来之后，合成一个完整的信息再发送
                new LengthFieldBasedFrameDecoder(1024, 10, 4, 2, 0),
                new LoggingHandler(LogLevel.DEBUG), new MessageCodec());
        //测试encode
        LoginRequestMessage message = new LoginRequestMessage(1, "zhangSan");
        channel.writeOutbound(message);
        //测试decode
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);
        //buf.readByte();
        ByteBuf buf1 = buf.slice(0, 100);
        ByteBuf buf2 = buf.slice(100, buf.readableBytes() - 100);
        buf1.retain();
        channel.writeInbound(buf1); // netty在调用writeInbound时，自动调用byteBuf的release方法（此时，buf1,buf2,buf的内存都会被释放）,所以要先调用一次 retain
        channel.writeInbound(buf2);


    }
}
