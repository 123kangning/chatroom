package netty.c1;

import java.nio.ByteBuffer;

public class Buffer {
    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(10);
        System.out.println(buf.get());
        buf.get();

    }
}
