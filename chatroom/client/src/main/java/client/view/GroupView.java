package client.view;

import client.ChatClient;
import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class GroupView {
    public GroupView(ChannelHandlerContext ctx) {
        while (true) {
            System.out.printf("\n\t+----------------- 您的ID为 %d -------------------+\n", myUserID);
            System.out.println("\t| 7 -> 选择群聊(通过ID)\t| 3 -> 群处理通知      \t|");//别的群邀请你加入或将你踢出
            System.out.println("\t+-----------------------+-----------------------+");
            System.out.println("\t| 6 -> 退出群聊(通过ID)\t| 2 -> 显示加入的群组列表\t|");
            System.out.println("\t+-----------------------+-----------------------+");
            System.out.println("\t| 5 -> 加入群聊(通过ID)\t| 0 -> 返回上级目录    \t|");
            System.out.println("\t+-----------------------+-----------------------+");
            System.out.println("\t| 4 -> 创建群聊       \t|");
            System.out.println("\t+-----------------------+");
            if (haveNoRead > 0) {
                System.out.println("\thaveNoRead = " + haveNoRead);
                System.out.println("\t主人，您有未查看的信息，请注意查看...");
            }
            Scanner scanner = new Scanner(System.in);

            String s0 = scanner.nextLine();
            while (!StringUtils.isNumber(s0)) {
                System.out.println("输入不规范，请重新输入您的选择：");
                s0 = scanner.nextLine();
            }
            switch (Integer.parseInt(s0)) {
                case 0:
                    return;
                case 2: {
                    query(ctx);
                    System.out.println("按下回车返回...");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    ctx.writeAndFlush(new GroupApplyQueryRequestMessage(myUserID));

                    ChatClient.wait1();

                    dealRequest(ctx, scanner);
                    break;
                }
                case 4: {
                    System.out.println("请输入要创建的群聊名称：");
                    String s1 = scanner.nextLine();
                    ctx.writeAndFlush(new GroupCreateRequestMessage(myUserID, s1));

                    ChatClient.wait1();

                    break;
                }
                case 5: {
                    System.out.println("请输入你要申请加入的群聊ID：");
                    s0 = scanner.nextLine();
                    while (!StringUtils.isNumber(s0)) {
                        System.out.println("输入不规范，请重新输入你要申请加入的群聊ID：");
                        s0 = scanner.nextLine();
                    }
                    int groupID = Integer.parseInt(s0);
                    /*GroupJoinRequestMessage message=new GroupJoinRequestMessage(myUserID,groupID);*/
                    SendApplyMessage message = new SendApplyMessage(myUserID, groupID, "申请加入群组");
                    message.setPurpose("G");
                    message.setGroupID(groupID);
                    ctx.writeAndFlush(message);
                    break;
                }
                case 6: {
                    query(ctx);
                    System.out.println("请输入你要退出的群组ID[输入0退出该界面]：");
                    s0 = scanner.nextLine();
                    while (!StringUtils.isNumber(s0)) {
                        System.out.println("输入不规范，请重新输入你要退出的群聊ID：");
                        s0 = scanner.nextLine();
                    }
                    int groupID = Integer.parseInt(s0);
                    if (groupID == 0) {
                        break;
                    }
                    GroupQuitRequestMessage message = new GroupQuitRequestMessage(myUserID, groupID, myUserID);
                    //log.info("GroupQuitRequestMessage");
                    ctx.writeAndFlush(message);

                    ChatClient.wait1();

                    break;
                }
                case 7: {
                    System.out.println("请输入你选择的群聊ID：");
                    s0 = scanner.nextLine();
                    while (!StringUtils.isNumber(s0)) {
                        System.out.println("输入不规范，请重新输入你选择的群聊ID：");
                        s0 = scanner.nextLine();
                    }
                    int groupID = Integer.parseInt(s0);
                    ctx.writeAndFlush(new GroupCheckGradeRequestMessage(myUserID, groupID));

                    ChatClient.wait1();

                    if (waitSuccess == 1) {
                        new GroupEnterView(ctx, groupID);
                    }
                    break;
                }
            }
        }
    }

    protected void query(ChannelHandlerContext ctx) {
        GroupQueryRequestMessage message = new GroupQueryRequestMessage(myUserID);
        ctx.writeAndFlush(message);

        ChatClient.wait1();

        for (String s : friendList) {
            System.out.println(s);
        }
        System.out.println("----------------------");
    }

    protected void receiveInformation(ChannelHandlerContext ctx, String s0) {
        String[] s1 = s0.split(" ");
        List<Integer> list = new ArrayList<>();
        for (String s : s1) {
            if (!StringUtils.isNumber(s)) {
                System.out.println("输入不规范");
                return;
            }
            list.add(Integer.parseInt(s));
        }
        ReceiveMessageRequestMessage message = new ReceiveMessageRequestMessage(list);
        ctx.writeAndFlush(message);

        ChatClient.wait1();

    }

    protected void dealRequest(ChannelHandlerContext ctx, Scanner scanner) {
        for (String s : friendList) {
            System.out.println(s);
        }

        System.out.println("输入[1]通过群组邀请请求，[2]拒绝群组邀请请求，[3]确认群组通知\n[4]通过用户入群申请，[0]退出：");
        String s0 = scanner.nextLine();
        while (!StringUtils.isNumber(s0)) {
            System.out.println("输入不规范，请重新输入您的选择：");
            s0 = scanner.nextLine();
        }
        switch (Integer.parseInt(s0)) {
            case 1: {
                System.out.println("请输入要加入的群组ID,若有多个，以空格分隔开：");
                s0 = scanner.nextLine();
                String[] s1 = s0.split(" ");
                List<Integer> list = new ArrayList<>();
                for (String s : s1) {
                    list.add(Integer.parseInt(s));
                }

                GroupJoinRequestMessage message = new GroupJoinRequestMessage(myUserID, list);
                message.setSetList(true);
                ctx.writeAndFlush(message);

                ChatClient.wait1();

                break;
            }
            case 2: {
                System.out.println("请输入要拒绝的群组ID,若有多个，以空格分隔开：");
                s0 = scanner.nextLine();
                receiveInformation(ctx, s0);
                break;
            }
            case 3: {
                System.out.println("请输入要确认消息的ID,若有多个，以空格分隔开：");
                s0 = scanner.nextLine();
                receiveInformation(ctx, s0);
                break;
            }
            case 4: {
                System.out.println("请输入要通过申请的用户ID和其要进入的群组，以空格分隔开：");
                s0 = scanner.nextLine();
                String[] s = s0.split(" ");
                GroupJoinRequestMessage message = new GroupJoinRequestMessage(myUserID, Integer.parseInt(s[1]));
                message.setTalkerID(Integer.parseInt(s[0]));
                message.setTalker_type("F");
                ctx.writeAndFlush(message);

                ChatClient.wait1();

                break;
            }
            case 0:
                break;
        }
    }
}
