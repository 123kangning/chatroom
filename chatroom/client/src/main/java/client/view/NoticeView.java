package client.view;

import client.ChatClient;
import io.netty.channel.ChannelHandlerContext;
import message.NoticeRequestMessage;

import java.io.IOException;
import java.util.List;

import static client.ChatClient.*;

public class NoticeView {
    public NoticeView(ChannelHandlerContext ctx) {
        System.out.printf("\n\t+------------- 您的ID为 %8d --------------+\n", myUserID);
        System.out.println("\t+------------------消息通知栏-------------------+");
        ctx.writeAndFlush(new NoticeRequestMessage(myUserID));

        ChatClient.wait1();

        List<String> friend = noticeMap.get("F");
        List<String> group = noticeMap.get("G");
        for (String s1 : friend) {
            System.out.println(s1);
        }
        for (String s1 : group) {
            System.out.println(s1);
        }
        System.out.println("按下回车返回...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
