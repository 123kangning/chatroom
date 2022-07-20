package client.view;

import client.ChatClient;
import io.netty.channel.ChannelHandlerContext;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import message.LogoutRequestMessage;

import java.util.Scanner;

import static client.ChatClient.waitMessage;
import static client.ChatClient.waitSuccess;

@Slf4j
public class MainView {
    public MainView(ChannelHandlerContext ctx){
        while(true){

            System.out.println("\n\t+---------------------+");
            System.out.println("\t| 11 -> 退出群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 10 -> 加入群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 9 -> 创建群聊        |");
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
            switch (Integer.parseInt(scanner.nextLine())){
                /*case 1:
                    new EnterView(ctx);
                    break;*/
                case 2:
                    break;
                case 3:
                    System.out.println("确定退出吗？[y/n]");
                    log.info("myUserID = {}",ChatClient.myUserID);
                    String s1=scanner.nextLine();
                    log.info("s={}",s1);
                    log.info("ctx= {}",ctx);
                    if(s1.compareToIgnoreCase("y")==0){
                        ctx.writeAndFlush(new LogoutRequestMessage(ChatClient.myUserID));
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
                case 4:
                case 5:
            }

        }
    }
}
