import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import message.PingMessage;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;
import message.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.Scanner;
@Slf4j
public class Test {
    public static void main(String[] args){
        /*File file=new File("/home/kangning/1.log");
        log.info("file.length = {}",file.length());
        try (RandomAccessFile file1 = new RandomAccessFile(file, "r")) {
            log.info("file.length = {}",file1.length());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        Scanner scanner=new Scanner(System.in);
        for(int i=0;i<10;i++){
            System.out.print("\r|");
            for(int j=0;j<i*10;j++){
                System.out.print("#");
            }
            for(int j=i*10;j<100;j++){
                System.out.print("-");
            }
            System.out.print("|");
        }
    }
}
