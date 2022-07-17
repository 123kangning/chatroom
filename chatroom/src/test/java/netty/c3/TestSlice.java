package netty.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'1','2','3','4','5','6','7','8','9','0'});
        log.info((String) buf.getCharSequence(0,10, StandardCharsets.UTF_8));
        //切片过程中，并没有发生数据复制
        //切片前后使用的是同一块内存，不允许向切片后的ByteBUf中继续写入数据
        ByteBuf buf1=buf.slice(0,5);
        ByteBuf buf2=buf.slice(5,5);
        log.info((String) buf1.getCharSequence(0,5, StandardCharsets.UTF_8));
        log.info((String) buf2.getCharSequence(0,5, StandardCharsets.UTF_8));
        System.out.println("==================================");

        buf1.setByte(0,'!');
        buf2.setByte(0,'@');
        log.info((String) buf1.getCharSequence(0,5, StandardCharsets.UTF_8));
        log.info((String) buf2.getCharSequence(0,5, StandardCharsets.UTF_8));
        log.info((String) buf.getCharSequence(0,10, StandardCharsets.UTF_8));

    }
}
