package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static client.ChatClient.*;
import static client.ChatClient.waitMessage;

public class GroupView {
    public GroupView(ChannelHandlerContext ctx){
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d -----+\n",myUserID);
            System.out.println("\t| 7 -> 选择群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            /*System.out.println("\t| 6 -> 退出群聊(通过ID)|");
            System.out.println("\t+---------------------+");*/
            System.out.println("\t| 5 -> 加入群聊(通过ID)|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 4 -> 创建群聊        |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 3 -> 群处理通知      |");//别的群邀请你加入或将你踢出
            System.out.println("\t+---------------------+");
            System.out.println("\t| 2 -> 显示加入的群组列表|");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 1 -> 返回上级目录     |");
            System.out.println("\t+---------------------+");
            if(haveNoRead>0){
                System.out.println("主人，您有未查看的信息，请注意查看...");
            }
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
                case 3:{
                    ctx.writeAndFlush(new GroupApplyQueryRequestMessage(myUserID));
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

                    while(true){
                        System.out.println("输入[1]通过群组邀请请求，[2]拒绝群组邀请请求，[3]接受群组踢出通知，[0]退出：");
                        s0=scanner.nextLine();
                        while(!StringUtils.isNumber(s0)){
                            System.out.println("输入不规范，请重新输入您的选择：");
                            s0=scanner.nextLine();
                        }
                        switch(Integer.parseInt(s0)){
                            case 1:{
                                System.out.println("请输入要加入的群组ID,若有多个，以空格分隔开：");
                                s0=scanner.nextLine();
                                String[] s1=s0.split(" ");
                                List<Integer> list=new ArrayList<>();
                                for(String s:s1){
                                    list.add(Integer.parseInt(s));
                                }
                                GroupJoinRequestMessage message=new GroupJoinRequestMessage(myUserID,list);
                                message.setSetList(true);
                                ctx.writeAndFlush(message);
                                try {
                                    synchronized (waitMessage){
                                        waitMessage.wait();
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case 2:{
                                System.out.println("请输入要拒绝的群组ID,若有多个，以空格分隔开：");
                                s0=scanner.nextLine();
                                String[] s1=s0.split(" ");
                                List<Integer> list=new ArrayList<>();
                                for(String s:s1){
                                    list.add(Integer.parseInt(s));
                                }
                                GroupJoinRequestMessage message=new GroupJoinRequestMessage(myUserID,list);
                                message.setSetList(true);
                                message.setNoAdd(true);
                                ctx.writeAndFlush(message);
                                try {
                                    synchronized (waitMessage){
                                        waitMessage.wait();
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case 0: return;
                            case 3:break;
                        }
                    }
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
                case 5:{
                    System.out.println("请输入你要申请加入的群聊ID：");
                    s0=scanner.nextLine();
                    while(!StringUtils.isNumber(s0)){
                        System.out.println("输入不规范，请重新输入你要申请加入的群聊ID：");
                        s0=scanner.nextLine();
                    }
                    int groupID=Integer.parseInt(s0);
                    GroupJoinRequestMessage message=new GroupJoinRequestMessage(myUserID,groupID);
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
                case 7:{
                    System.out.println("请输入你选择的群聊ID：");
                    s0=scanner.nextLine();
                    while(!StringUtils.isNumber(s0)){
                        System.out.println("输入不规范，请重新输入你选择的群聊ID：");
                        s0=scanner.nextLine();
                    }
                    int groupID=Integer.parseInt(s0);
                    ctx.writeAndFlush(new GroupCheckGradeRequestMessage(myUserID,groupID));
                    try{
                        synchronized(waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(waitSuccess==1){
                        new GroupEnterView(ctx,groupID);
                    }
                    break;
                }
            }
        }
    }
}
