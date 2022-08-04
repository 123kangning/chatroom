import cn.hutool.core.date.DateTime;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static client.ChatClient.myUserID;

@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
        Console console=System.console();
        String s="  ";
        while(s.length()>0){
            s= String.valueOf(console.readPassword());
            System.out.println(s);
        }


    }
}
