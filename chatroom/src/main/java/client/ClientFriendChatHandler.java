package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import client.view.FriendView;

import java.util.Random;
import java.util.Scanner;

import static client.ChatClient.*;
@Slf4j
public class ClientFriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        if(immediate&&msg.getUserID()==talkWith){
            String ans=String.format("\t%d:%s",msg.getUserID(),msg.getMessage());
            System.out.println(ans);
            log.info("in ClientFriendChatHandler");
            if(msg.getMsg_type().equals("F")){
                isY=true;
                System.out.println("有文件到来，输入y对这个文件进行处理，输入n暂不处理：");
                log.info("FriendView.receiveFile(msg.getMessage(), new Scanner(System.in),ctx,msg.getUserID())");
                new Thread(()->{
                    int count=1000;
                    while(!checkRECV.equalsIgnoreCase("y")&&count>0){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count--;
                        //log.info("count-- = {}",count);
                    }
                    if(count>0){
                        FriendView.receiveFile(ans, new Scanner(System.in),ctx,msg.getUserID());
                        synchronized (waitRVFile){
                            waitRVFile.notifyAll();
                        }
                    }

                }).start();

            }
        }else{
            if(haveNoRead==0&&immediate==false){
                System.out.println("主人，您有未查看的信息，请注意查看...");
            }
            int count=msg.getCount();
            System.out.println("count = "+count);
                haveNoRead+=count;

        }

        /*if(haveNoRead){
            System.out.println("主人，您有未查看的信息，请注意查看...");
        }*/
    }
}
