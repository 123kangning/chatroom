package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.FriendQueryRequestMessage;
import message.GroupCreateRequestMessage;
import message.GroupQueryRequestMessage;
import server.session.Group;

import java.io.IOException;
import java.util.Scanner;

import static client.ChatClient.*;
import static client.ChatClient.waitMessage;

public class GroupView {
    public GroupView(ChannelHandlerContext ctx){
        while(true){
            haveNoRead=false;
            System.out.printf("\n\t+----- 您的ID为 %d -----+\n",myUserID);
            System.out.println("\t| 7 -> 开始群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 6 -> 退出群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 5 -> 加入群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 4 -> 创建群聊        |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 2 -> 显示群组列表     |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 1 -> 返回上级目录     |");
            System.out.println("\t+---------------------+");
            Scanner scanner=new Scanner(System.in);

            String s0=scanner.nextLine();
            while(!StringUtils.isNumber(s0)){
                System.out.println("输入不规范，请重新输入您的选择：");
                s0=scanner.nextLine();
            }
            switch(Integer.parseInt(s0)) {
                case 1:return;
                case 2:{
                    GroupQueryRequestMessage message=new GroupQueryRequestMessage(myUserID);
                    ctx.writeAndFlush(message);
                    try{
                        synchronized(waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    for(String s:friendList){
                        System.out.println(s);
                    }
                    System.out.println("----------------------");
                    System.out.println("按下回车返回...");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 4:{
                    System.out.println("请输入要创建的群聊名称：");
                    String s1=scanner.nextLine();
                    ctx.writeAndFlush(new GroupCreateRequestMessage(myUserID,s1));
                    try{
                        synchronized(waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 5:
                case 6:
                case 7:
            }
        }
    }
}
