package client.view;

import client.ChatClient;
import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.LogoutRequestMessage;
import message.SignOutRequestMessage;

import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class MainView {
    public MainView(ChannelHandlerContext ctx) {
        while (true) {
            System.out.printf("\n\t+------ 您的ID为 %d ------+\n" +
                    "\t| 6 -> 进入消息通知栏  \t|\n" +
                    "\t+-----------------------+\n" +
                    "\t| 5 -> 进入群组管理界面\t|\n" +
                    "\t+-----------------------+\n" +
                    "\t| 4 -> 进入好友管理界面\t|\n" +
                    "\t+-----------------------+\n" +
                    "\t| 3 -> 退出          \t|\n" +
                    "\t+-----------------------+\n" +
                    "\t| 2 -> 注销          \t|\n" +
                    "\t+-----------------------+\n", myUserID);

            if (haveNoRead > 0) {
                System.out.println("\thaveNoRead = " + haveNoRead);
                System.out.println("\t主人，您有未查看的信息，请注意查看...");
            }

            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            while (!StringUtils.isNumber(s)) {
                System.out.println("输入不规范，请重新输入您的选择：");
                s = scanner.nextLine();
            }
            switch (Integer.parseInt(s)) {

                case 2: {
                    System.out.println("确定注销吗？[y/n]");
                    //log.info("myUserID = {}",ChatClient.myUserID);
                    String s1 = scanner.nextLine();
                    if (s1.compareToIgnoreCase("y") == 0) {
                        ctx.writeAndFlush(new SignOutRequestMessage(ChatClient.myUserID));

                        ChatClient.wait1();

                        if (waitSuccess == 1) {
                            return;
                        }
                    }
                    haveNoRead = 0;
                    break;
                }
                case 3: {
                    System.out.println("确定退出吗？[y/n]");
                    //log.info("myUserID = {}",ChatClient.myUserID);
                    String s1 = scanner.nextLine();
                    //log.info("s={}",s1);
                    if (s1.compareToIgnoreCase("y") == 0) {
                        ctx.writeAndFlush(new LogoutRequestMessage());

                        ChatClient.wait1();

                        if (waitSuccess == 1) {
                            return;
                        }
                    }
                    haveNoRead = 0;
                    break;
                }
                case 4:
                    new FriendView(ctx);
                    break;
                case 5:
                    new GroupView(ctx);
                    break;
                case 6:
                    new NoticeView(ctx);
                    break;
            }

        }
    }
}
