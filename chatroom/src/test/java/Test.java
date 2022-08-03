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
        /*System.out.println("11");
        Scanner scanner=new Scanner(System.in);
        String s=scanner.nextLine();
        System.out.println("s.length="+s.length());*/
        System.out.printf("%s%5d,","啦啦啦",20);
        /*select talkerID,talker_type,content,isAccept,msg_type from message where (msg_type='S' or msg_type='F') and groupID=2 and talker_type='G' and (user
                -> ID=2 or talkerID=2) and ((talkerID=2 and group by talkerID ) or talkerID!=2 )  order by msg_id desc limit 20;*/
        System.out.printf("\n%50s\n","11啦啦啦");
        System.out.printf("%50s","111111111啦啦啦啦啦啦啦啦啦啦啦啦");
        //System.out.println("".equals(""));
    }
}
