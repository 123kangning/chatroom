package client;

import client.view.FriendView;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;

import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class ClientFriendChatHandler extends SimpleChannelInboundHandler<FriendChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendChatRequestMessage msg) throws Exception {
        if ((immediate && msg.getUserID() == talkWith) || (immediateGroup && msg.getGroup() == talkWithGroup)) {
            String ans = String.format("%s%s", msg.getPrefix(), msg.getMessage());
            System.out.println(ans);
            //log.info("in ClientFriendChatHandler");
            if (msg.getMsg_type().equals("F")) {
                isY = true;
                System.out.println("有文件到来，输入y对这个文件进行处理，输入n暂不处理：");
                //log.info("FriendView.receiveFile(msg.getMessage(), new Scanner(System.in),ctx,msg.getUserID())");
                new Thread(() -> {
                    int count = 1000;
                    while (!checkRECV.equalsIgnoreCase("y") && count > 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count--;
                        //log.info("count-- = {}",count);
                    }
                    if (count > 0) {
                        if (immediate) {
                            //log.info("if(immediate)==1");
                            FriendView.receiveFile(ans, new Scanner(System.in), ctx, msg.getUserID(), false, 0);
                        } else {
                            //log.info("if(immediate)==2");
                            FriendView.receiveFile(ans, new Scanner(System.in), ctx, msg.getUserID(), true, msg.getGroup());
                        }

                        synchronized (waitRVFile) {
                            waitRVFile.notifyAll();
                        }
                    }

                }).start();
            }
        } else {
            if (haveNoRead == 0 && (!immediate && !immediateGroup) && msg.getCount() > 0) {
                System.out.println("主人，您有未查看的信息，请注意查看...");
            }
            int count = msg.getCount();
            //System.out.println("count = "+count);
            haveNoRead += count;

        }

        /*if(haveNoRead){
            System.out.println("主人，您有未查看的信息，请注意查看...");
        }*/
    }
}
