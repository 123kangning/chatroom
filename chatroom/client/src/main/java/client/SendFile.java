package client;

import io.netty.channel.ChannelHandlerContext;
import static client.ChatClient.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SendFile {
    public SendFile(ChannelHandlerContext ctx, File file){
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            fileLength= (int) randomAccessFile.length();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
