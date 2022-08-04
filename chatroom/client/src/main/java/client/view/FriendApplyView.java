package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.FriendAddRequestMessage;
import message.FriendApplyQueryMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static client.ChatClient.*;

public class FriendApplyView {
    public FriendApplyView(ChannelHandlerContext ctx) {
        System.out.printf("\n\t+------------- 您的ID为 %8d --------------+\n", myUserID);
        System.out.println("\t+------------------好友申请栏-------------------+");
        ctx.writeAndFlush(new FriendApplyQueryMessage(myUserID));
        try {
            synchronized (waitMessage) {
                waitMessage.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String s1 : friendList) {
            System.out.println(s1);
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("输入[1]通过好友请求，[2]拒绝好友请求，[0]退出：");
            String s0 = scanner.nextLine();
            while (!StringUtils.isNumber(s0)) {
                System.out.println("输入不规范，请重新输入您的选择：");
                s0 = scanner.nextLine();
            }
            switch (Integer.parseInt(s0)) {
                case 1: {
                    System.out.println("请输入要通过的好友ID,若有多个，以空格分隔开：");
                    s0 = scanner.nextLine();
                    String[] s1 = s0.split(" ");
                    List<Integer> list = new ArrayList<>();
                    for (String s : s1) {
                        list.add(Integer.parseInt(s));
                    }
                    FriendAddRequestMessage message = new FriendAddRequestMessage(myUserID, list);
                    message.setNoAdd(false);
                    message.setSetList(true);
                    ctx.writeAndFlush(message);
                    try {
                        synchronized (waitMessage) {
                            waitMessage.wait();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2:{
                    System.out.println("请输入要拒绝的好友ID,若有多个，以空格分隔开：");
                    s0 = scanner.nextLine();
                    String[] s1 = s0.split(" ");
                    List<Integer> list = new ArrayList<>();
                    for (String s : s1) {
                        list.add(Integer.parseInt(s));
                    }
                    FriendAddRequestMessage message = new FriendAddRequestMessage(myUserID, list);
                    message.setNoAdd(true);
                    message.setSetList(true);
                    ctx.writeAndFlush(message);
                    try {
                        synchronized (waitMessage) {
                            waitMessage.wait();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case 0:
                    return;
            }


        }
    }
}
