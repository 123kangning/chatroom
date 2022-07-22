package client.view;

import client.ChatClient;
import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.LogoutRequestMessage;
import message.SignOutRequestMessage;

import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class MainView {
    public MainView(ChannelHandlerContext ctx){
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d -----+\n",myUserID);
            System.out.println("\t| 12 -> 退出群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 11 -> 加入群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 10 -> 创建群聊        |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 9 -> 好友聊天(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 8 -> 屏蔽好友(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 7 -> 删除好友(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 6 -> 添加好友(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 5 -> 显示群组列表     |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 4 -> 显示好友列表     |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 3 -> 退出            |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 2 -> 注销            |");
            System.out.println("\t+---------------------+");
//            System.out.println("\t|   |");
//            System.out.println("\t+--------------------+");

            Scanner scanner=new Scanner(System.in);
            String s=scanner.nextLine();
            while(!StringUtils.isNumber(s)){
                System.out.println("输入不规范，请重新输入用户ID：");
                s=scanner.nextLine();
            }
            switch (Integer.parseInt(s)){
                /*case 1:
                    new EnterView(ctx);
                    break;*/
                case 2:{
                    System.out.println("确定注销吗？[y/n]");
                    log.info("myUserID = {}",ChatClient.myUserID);
                    String s1=scanner.nextLine();
                    if(s1.compareToIgnoreCase("y")==0){
                        ctx.writeAndFlush(new SignOutRequestMessage(ChatClient.myUserID));
                        try{
                            synchronized(waitMessage){
                                waitMessage.wait();
                            }
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        if(waitSuccess==1){
                            return;
                        }
                    }
                    break;
                }
                case 3:{
                    System.out.println("确定退出吗？[y/n]");
                    log.info("myUserID = {}",ChatClient.myUserID);
                    String s1=scanner.nextLine();
                    log.info("s={}",s1);
                    if(s1.compareToIgnoreCase("y")==0){
                        ctx.writeAndFlush(new LogoutRequestMessage());
                        try{
                            synchronized(waitMessage){
                                waitMessage.wait();
                            }
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        if(waitSuccess==1){
                            return;
                        }
                    }
                    break;
                }
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:{
                    System.out.println("请输入好友ID：");
                    String s1=scanner.nextLine();
                    while(!StringUtils.isNumber(s1)){
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1=scanner.nextLine();
                    }
                    long FriendID=Long.valueOf(s1);
                    System.out.println("聊天内容(按下回车发送)：");
                    String chat=scanner.nextLine();
                    FriendChatRequestMessage message=new FriendChatRequestMessage(myUserID,FriendID,chat);
                    ctx.writeAndFlush(message);
                    try{
                        synchronized(waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 10:
                case 11:
            }

        }
    }
}
