import cn.hutool.core.date.DateTime;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import static client.ChatClient.myUserID;
@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
        try{
            File file=new File("/home/kangning/1.log");
            Scanner scanner=new Scanner(System.in);
            String addFile=scanner.nextLine();
            log.info("addFile = {}",addFile);
            File tempFile1=new File(addFile);
            while(!tempFile1.isDirectory()){
                System.out.println("不是目录，请重新输入：");
                addFile=scanner.nextLine();
                tempFile1=new File(addFile);
            }
            if(addFile.charAt(addFile.length()-1)!='/'){
                addFile=addFile.concat("/");
            }
            addFile=addFile.concat(file.getName());
            tempFile1=new File(addFile);
            FileChannel readChannel1= new FileInputStream(file).getChannel();
            FileChannel writeChannel1= new FileOutputStream(tempFile1).getChannel();
            ByteBuffer buf=ByteBuffer.allocate(1024);
            while(readChannel1.read(buf)!=-1){
                buf.flip();
                writeChannel1.write(buf);
                buf.clear();
            }
            tempFile1.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
