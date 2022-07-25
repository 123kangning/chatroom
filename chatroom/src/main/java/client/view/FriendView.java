package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.*;

import java.util.Scanner;

import static client.ChatClient.*;

public class FriendView {
    public FriendView(ChannelHandlerContext ctx){
        while (true){
            System.out.printf("\n\t+----- 您的ID为 %d -----+\n",myUserID);
            System.out.println("\t| 9 -> 好友聊天(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 8 -> 屏蔽好友(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 7 -> 删除好友(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 6 -> 添加好友(通过ID) |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 4 -> 显示好友列表     |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 1 -> 返回上级目录     |");
            System.out.println("\t+---------------------+");
            Scanner scanner=new Scanner(System.in);
            switch(Integer.parseInt(scanner.nextLine())){
                case 1:return;
                case 4:{
                    ctx.writeAndFlush(new FriendQueryRequestMessage(myUserID));
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
                    System.out.println("\t+---------------------+");
                    break;
                }
                case 6:{
                    System.out.println("请输入要添加的好友ID：");
                    String s1=scanner.nextLine();
                    while(!StringUtils.isNumber(s1)){
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1=scanner.nextLine();
                    }
                    int FriendID=Integer.parseInt(s1);
                    if(FriendID==myUserID){
                        System.out.println("哦，愚蠢的土拨鼠，你怎么能添加你自己呢？");
                        break;
                    }
                    ctx.writeAndFlush(new FriendAddRequestMessage(myUserID,FriendID));
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
                    System.out.println("请输入要删除的好友ID：");
                    String s1=scanner.nextLine();
                    while(!StringUtils.isNumber(s1)){
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1=scanner.nextLine();
                    }
                    int FriendID=Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendDeleteRequestMessage(myUserID,FriendID));
                    try{
                        synchronized(waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 8:{
                    System.out.println("请输入要屏蔽的好友ID：");
                    String s1=scanner.nextLine();
                    while(!StringUtils.isNumber(s1)){
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1=scanner.nextLine();
                    }
                    int FriendID=Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendShieldRequestMessage(myUserID,FriendID));
                    try{
                        synchronized(waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 9:{
                    System.out.println("请输入好友ID：");
                    String s1=scanner.nextLine();
                    while(!StringUtils.isNumber(s1)){
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1=scanner.nextLine();
                    }
                    int FriendID=Integer.parseInt(s1);
                    ctx.writeAndFlush(new FriendNoticeMessage(myUserID,FriendID));
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
                    System.out.println("聊天内容(按下回车发送)[输入r退出]：");
                    immediate=true;
                    String chat=scanner.nextLine();
                    while(!chat.equalsIgnoreCase("r")){
                        FriendChatRequestMessage message=new FriendChatRequestMessage(myUserID,FriendID,chat,"S");
                        message.setTalker_type("F");
                        ctx.writeAndFlush(message);
                        try{
                            synchronized(waitMessage){
                                waitMessage.wait();
                            }
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        if(waitSuccess==0){
                            break;
                        }
                        System.out.println("聊天内容(按下回车发送)[输入r退出]：");
                        chat=scanner.nextLine();
                    }
                    if(chat.equalsIgnoreCase("r")){
                        immediate=false;
                    }
                    break;
                }
            }
        }
    }
}
