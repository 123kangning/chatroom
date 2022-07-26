package client;

import client.view.EnterView;
import client.view.MainView;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import message.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        boolean success=msg.getSuccess();
        String reason=msg.getReason();
        int ResponseMessageType=msg.getMessageType();
        //log.info("ResponseMessageType={}",ResponseMessageType);
        if(!success){
            System.out.print("操作失败 "+reason);
            waitSuccess=0;
        }else{
            waitSuccess=1;
            if(msg.getMessageType()==Message.FriendGetFileRequestMessage){
                System.out.println("接收成功，请选择一个目录将其保存下来[使用绝对路径]");
                receiveFile(msg.getFile());
                System.out.println("保存成功");
            }
            else if(msg.getMessageType()==Message.FriendQueryRequestMessage){
                friendList=msg.getFriendList();
                //log.info("friendList=msg.getFriendList()");
            }
            else if(msg.getMessageType()==Message.noticeMapMessage){
                noticeMap=msg.getNoticeMap();
            }else{
                System.out.print("操作成功 "+reason);
            }

        }
        synchronized (waitMessage){
            waitMessage.notifyAll();
        }
        //log.info("waitMessage.notifyAll();");
    }
    public void receiveFile(File file){
        try{
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
