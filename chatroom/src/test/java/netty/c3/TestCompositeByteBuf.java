package netty.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1= ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{'1','2','3','4','5'});
        ByteBuf buf2= ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{'6','7','8','9','0'});

        /*ByteBuf buffer= ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(buf1).writeBytes(buf2);*/

        CompositeByteBuf buffer=ByteBufAllocator.DEFAULT.compositeBuffer();
        buffer.addComponents(true,buf1,buf2);
        log.info("buffer = "+buffer);
        log.info((String) buffer.getCharSequence(0,10, StandardCharsets.UTF_8));
        log.info("buffer = "+buffer);
    }
}
