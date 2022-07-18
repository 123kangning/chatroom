package netty.c1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel=new EmbeddedChannel(
                //最大容量为1024字节，长度部位开始的索引为0,长度部位占4个字节，长度部位之后到内容部位需要跳过0个字节，最后需要从头部开始剥离4个字节（剥离长度部位）
                new LengthFieldBasedFrameDecoder(1024,0,4,1,5),
                new LoggingHandler(LogLevel.DEBUG)
        );
        //4字节长度
        ByteBuf buffer= ByteBufAllocator.DEFAULT.buffer();
        send(buffer,"hello,world");
        send(buffer,"hi!");
        send(buffer,"I'm LiHua");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer,String msg) {

        byte[] bytes=msg.getBytes();
        int length=bytes.length;
        buffer.writeInt(length);//大端表示法
        buffer.writeByte(1);
        buffer.writeBytes(bytes);
    }
}
