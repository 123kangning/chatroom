package client.view;

import client.ChatClient;
import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import message.FriendChatRequestMessage;
import message.LogoutRequestMessage;
import message.NoticeRequestMessage;
import message.SignOutRequestMessage;

import java.util.Scanner;

import static client.ChatClient.*;

@Slf4j
public class MainView {
    public MainView(ChannelHandlerContext ctx){
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d -----+\n",myUserID);
            System.out.println("\t+---------------------+");
            System.out.println("\t| 6 -> 进入消息通知栏   |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 5 -> 进入群组管理界面  |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 4 -> 进入好友管理界面  |");
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
                case 4:new FriendView(ctx);break;
                case 5:new GroupView(ctx);break;
                case 6:new NoticeView(ctx);break;
            }

        }
    }
}
