package netty.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Slf4j
public class TestByteBuf {
    public static void main(String[] args) {
        //ByteBuf可动态扩容
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();

        StringBuilder sb=new StringBuilder();
        for(int i=0;i<300;i++){
            sb.append("a");
        }
        buf.writeBytes(sb.toString().getBytes());
        log.debug("buf = {},",buf);
    }
}
