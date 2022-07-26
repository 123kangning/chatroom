package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.*;

import java.io.File;
import java.util.Scanner;

import static client.ChatClient.*;
@Slf4j
public class FriendView {
    public FriendView(ChannelHandlerContext ctx){
        while (true){
            haveNoRead=false;
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
            System.out.println("\t| 3 -> 好友申请列表     |");
            System.out.println("\t+---------------------+");
            System.out.println("\t| 1 -> 返回上级目录     |");
            System.out.println("\t+---------------------+");
            Scanner scanner=new Scanner(System.in);
            String s0=scanner.nextLine();
            while(!StringUtils.isNumber(s0)){
                System.out.println("输入不规范，请重新输入您的选择：");
                s0=scanner.nextLine();
            }
            switch(Integer.parseInt(s0)){
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
                    FriendChatRequestMessage message=new FriendChatRequestMessage(myUserID,FriendID,"我可以和你交个朋友吗","A");
                    message.setTalker_type("F");
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
                    if(waitSuccess==1){
                        for(String s:friendList){
                            System.out.println(s);
                            int i=0,length=s.length(),j=100;
                            while(i<length&&s.charAt(i)=='\t'){
                                i++;
                            }
                            while(i<length&&(s.charAt(i)<='9'&&s.charAt(i)>='0')){
                                i++;
                            }
                            while(i<length&&s.charAt(i)==':'){
                                j=i;
                                i++;
                            }
                            //log.info("i={}, s.charAt(i)={}",i,s.charAt(i));
                            if(i<length&&i>j&&s.charAt(i)=='/'){
                                System.out.println("对方发来一个文件，是否接收？[T->接收][F->忽略]：");
                                String choice=scanner.nextLine();
                                while(!choice.equalsIgnoreCase("T")&&!choice.equalsIgnoreCase("F")){
                                    System.out.println("输入不规范，请重新输入");
                                    choice=scanner.nextLine();
                                }
                                if(choice.equalsIgnoreCase("T")){

                                    FriendGetFileRequestMessage m1=new FriendGetFileRequestMessage(myUserID,FriendID);
                                    ctx.writeAndFlush(m1);
                                    try{
                                        synchronized(waitMessage){
                                            waitMessage.wait();
                                        }
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        break;
                    }

                    System.out.println("聊天内容(按下回车发送)[输入F表示发送文件][输入r退出]：");
                    immediate=true;
                    String chat=scanner.nextLine();
                    while(!chat.equalsIgnoreCase("r")){
                        FriendChatRequestMessage message;
                        if(chat.equalsIgnoreCase("F")){
                            File file;
                            System.out.println("请输入待发送的文件的[路径路径]：");
                            file=new File(scanner.nextLine());
                            while(!file.exists()||!file.isFile()){
                                if(!file.exists()){
                                    System.out.println("文件不存在，请重新输入待发送的文件的[路径路径]");
                                }else{
                                    System.out.println("不是文件，请重新输入待发送的文件的[路径路径]");
                                }
                                file=new File(scanner.nextLine());
                            }
                            message=new FriendChatRequestMessage(myUserID,FriendID,file,"F");
                            message.setTalker_type("F");
                            ctx.writeAndFlush(message);
                            try{
                                synchronized(waitMessage){
                                    waitMessage.wait();
                                }
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }else{
                            message=new FriendChatRequestMessage(myUserID,FriendID,chat,"S");
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

                        }
                        System.out.println("聊天内容(按下回车发送)[输入F表示发送文件][输入r退出]：");
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
