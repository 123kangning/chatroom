package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.FriendChatRequestMessage;

import java.util.Scanner;

import static client.ChatClient.myUserID;
import static client.ChatClient.waitMessage;

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
                case 4:
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
                    int FriendID=Integer.parseInt(s1);
                    System.out.println("聊天内容(按下回车发送)：");
                    String chat=scanner.nextLine();
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
                    break;
                }
            }
        }
    }
}
