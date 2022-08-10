package client.view;

import client.ChatClient;
import client.SendFile;
import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class FriendView {
    public FriendView(ChannelHandlerContext ctx) {
        while (true) {

            System.out.printf("\n\t+------------------ 您的ID为 %d ------------------+\n", myUserID);
            System.out.println("\t| 9 -> 好友聊天(通过ID)\t| 5 -> 解除屏蔽(通过ID)\t|");
            System.out.println("\t+-----------------------+-----------------------+");
            System.out.println("\t| 8 -> 屏蔽好友(通过ID)\t| 4 -> 显示好友列表    \t|");
            System.out.println("\t+-----------------------+-----------------------+");
            System.out.println("\t| 7 -> 删除好友(通过ID)\t| 3 -> 好友申请列表    \t|");
            System.out.println("\t+-----------------------+-----------------------+");
            System.out.println("\t| 6 -> 添加好友(通过ID)\t| 0 -> 返回上级目录    \t|");
            System.out.println("\t+-----------------------------------------------+");

            System.out.println("\thaveNoRead = " + haveNoRead);
            if (haveNoRead > 0) {
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
                case 3:
                    new FriendApplyView(ctx);
                    break;
                case 4: {
                    ctx.writeAndFlush(new FriendQueryRequestMessage(myUserID));

                    ChatClient.wait1();

                    for (String s : friendList) {
                        System.out.println(s);
                    }
                    System.out.println("\t+---------------------+");
                    System.out.println("按下回车返回...");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 5: {
                    System.out.println("请输入要解除屏蔽的好友ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int FriendID = Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendUnShieldRequestMessage(myUserID, FriendID));
                    //log.info("1111");

                    ChatClient.wait1();

                    break;
                }
                case 6: {
                    System.out.println("请输入要添加的好友ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int FriendID = Integer.parseInt(s1);
                    if (FriendID == myUserID) {
                        System.out.println("哦，愚蠢的土拨鼠，你怎么能添加你自己呢？");
                        break;
                    }
                    SendApplyMessage message = new SendApplyMessage(myUserID, FriendID, "请求添加你为好友");
                    message.setPurpose("F");
                    ctx.writeAndFlush(message);

                    ChatClient.wait1();

                    break;
                }
                case 7: {
                    System.out.println("请输入要删除的好友ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int FriendID = Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendDeleteRequestMessage(myUserID, FriendID));

                    ChatClient.wait1();

                    break;
                }
                case 8: {
                    System.out.println("请输入要屏蔽的好友ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int FriendID = Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendShieldRequestMessage(myUserID, FriendID));

                    ChatClient.wait1();

                    break;
                }
                case 9: {
                    System.out.println("请输入好友ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int FriendID = Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendNoticeMessage(myUserID, FriendID));

                    ChatClient.wait1();

                    if (waitSuccess == 1) {
                        talkWith = FriendID;
                        int count = 0;
                        for (String s : friendList) {
                            System.out.println(s);
                            if (haveFile.charAt(count) == '1') {
                                FriendView.receiveFile(s, scanner, ctx, FriendID, false, 0);
                            }
                            count++;
                        }
                    } else {
                        break;
                    }

                    System.out.println("聊天内容(按下回车发送)[输入F表示发送文件][输入r退出]：");
                    immediate = true;
                    String chat = scanner.nextLine();
                    while (!chat.equalsIgnoreCase("r")) {
                        FriendChatRequestMessage message;
                        if (chat.equalsIgnoreCase("F")) {
                            File file;
                            System.out.println("请输入待发送的文件的[绝对路径](不支持1G以上文件！)：");
                            file = new File(scanner.nextLine());
                            while (!file.exists() || !file.isFile()) {
                                if (!file.exists()) {
                                    System.out.println("文件不存在，请重新输入待发送的文件的[绝对路径]");
                                } else {
                                    System.out.println("不是文件，请重新输入待发送的文件的[绝对路径]");
                                }
                                file = new File(scanner.nextLine());
                            }
                            message = new FriendChatRequestMessage(myUserID, FriendID, (byte[]) null, "F");
                            message.setTalker_type("F");
                            new SendFile(ctx,file,message);//调用发送文件类

                        } else if (chat.equalsIgnoreCase("Y") && isY) {
                            checkRECV = "y";
                            synchronized (waitRVFile) {
                                try {
                                    waitRVFile.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                checkRECV = "n";
                            }
                        }else if(chat.equalsIgnoreCase("N") && isY){
                            checkRECV = "Q";
                            synchronized (waitRVFile) {
                                try {
                                    waitRVFile.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                checkRECV = "n";
                            }
                        }else if(chat.length()>0){
                            message = new FriendChatRequestMessage(myUserID, FriendID, chat, "S");
                            message.setTalker_type("F");
                            ctx.writeAndFlush(message);

                            ChatClient.wait1();

                            if (waitSuccess == 0) {
                                break;
                            }
                        }
                        System.out.println("聊天内容(按下回车发送)[输入F表示发送文件][输入r退出]：");
                        chat = scanner.nextLine();
                    }
                    if (chat.equalsIgnoreCase("r")) {
                        immediate = false;
                        talkWith=0;
                    }
                    break;
                }
            }
        }
    }

    public static void receiveFile(String s, Scanner scanner, ChannelHandlerContext ctx, int FriendID, boolean isGroup, int groupID) {
        //log.info("enter receiveFile!!!");

        System.out.println("对方发来一个文件，是否接收？[T->接收][F->拒绝][P->暂不处理]：");
        String choice = scanner.nextLine();
        //log.info("choice ={},length={}", choice, choice.length());
        while (!choice.equalsIgnoreCase("T") && !choice.equalsIgnoreCase("F") && !choice.equalsIgnoreCase("P")) {
            System.out.println("输入不规范，请重新输入");
            choice = scanner.nextLine();
        }
        if (choice.equalsIgnoreCase("T")) {

            FriendGetFileRequestMessage m1 = new FriendGetFileRequestMessage(myUserID, FriendID);
            if (isGroup) {
                //log.info("isGroup ==true groupID={}", groupID);
                m1.setGroup(isGroup);
                m1.setGroupID(groupID);
            }
            ctx.writeAndFlush(m1);
            ChatClient.wait1();

        } else if (choice.equalsIgnoreCase("F")) {
            FriendGetFileRequestMessage m1 = new FriendGetFileRequestMessage(myUserID, FriendID);
            m1.setRefuse(true);
            if (isGroup) {
                //log.info("isGroup ==true groupID={}", groupID);
                m1.setGroup(isGroup);
                m1.setGroupID(groupID);
            }
            ctx.writeAndFlush(m1);
            ChatClient.wait1();
        }
    }
}
