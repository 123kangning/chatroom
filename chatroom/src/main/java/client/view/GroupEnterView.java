package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.GroupMemberRequestMessage;
import message.GroupQuitRequestMessage;
import message.GroupUnSayRequestMessage;
import message.SendApplyMessage;

import java.io.IOException;
import java.util.Scanner;

import static client.ChatClient.*;

public class GroupEnterView {
    private ChannelHandlerContext ctx;
    private int groupID;
    public GroupEnterView(ChannelHandlerContext ctx,int groupID){
        this.groupID=groupID;
        this.ctx=ctx;
        switch (gradeInGroup){
            case 0:choice1(ctx);break;//普通成员
            case 1:choice2(ctx);break;//管理员
            case 9:choice3(ctx);break;//群主
        }
    }
    public void choice1(ChannelHandlerContext ctx){

    }
    public void choice2(ChannelHandlerContext ctx){}
    public void choice3(ChannelHandlerContext ctx){
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d ------+\n",myUserID);
            System.out.println("\t|   9 -> 解散群聊        |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 8 -> 添加管理员(通过ID) |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 7 -> 撤除管理员(通过ID) |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 6 -> 邀请成员(通过ID)  |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 5 -> 踢出成员(通过ID)  |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 4 -> 禁言(通过ID)     |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 3 -> 解除禁言(通过ID)  |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 2 -> 进入聊天界面      |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 1 -> 查看群成员列表    |");
            System.out.println("\t+----------------------+");
            System.out.println("\t| 0 -> 返回上一级目录    |");
            System.out.println("\t+----------------------+");
            if(haveNoRead>0){
                System.out.println("主人，您有未查看的信息，请注意查看...");
            }
            Scanner scanner=new Scanner(System.in);

            String s0=scanner.nextLine();
            while(!StringUtils.isNumber(s0)){
                System.out.println("输入不规范，请重新输入您的选择：");
                s0=scanner.nextLine();
            }
            switch(Integer.parseInt(s0)){
                case 0:return;
                case 1:case1();break;
                case 2:
                case 3:case4(scanner,"T");break;
                case 4:case4(scanner,"F");break;
                case 5:case5(scanner);break;
                case 6:case6(scanner);break;
                case 7:
                case 8:
                case 9:
            }

        }
    }
    public void case1(){
        ctx.writeAndFlush(new GroupMemberRequestMessage(groupID));
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
        System.out.println("按下回车返回...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void case4(Scanner scanner,String say){
        System.out.println("输入要禁言的成员ID：");
        String s0=scanner.nextLine();
        while(!StringUtils.isNumber(s0)){
            System.out.println("输入不规范，请重新输入要禁言的成员ID：");
            s0=scanner.nextLine();
        }
        int unSayID=Integer.parseInt(s0);
        ctx.writeAndFlush(new GroupUnSayRequestMessage(unSayID,groupID,say));
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void case5(Scanner scanner){
        System.out.println("输入要踢出的成员ID：");
        String s0=scanner.nextLine();
        while(!StringUtils.isNumber(s0)){
            System.out.println("输入不规范，请重新输入要踢出的成员ID：");
            s0=scanner.nextLine();
        }
        int delID=Integer.parseInt(s0);
        ctx.writeAndFlush(new GroupQuitRequestMessage(delID,groupID,myUserID));
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void case6(Scanner scanner){
        System.out.println("输入要邀请的用户ID：");
        String s0=scanner.nextLine();
        while(!StringUtils.isNumber(s0)){
            System.out.println("输入不规范，请重新输入要邀请的用户ID：");
            s0=scanner.nextLine();
        }
        int FriendID=Integer.parseInt(s0);
        SendApplyMessage message=new SendApplyMessage(myUserID,FriendID,"邀请你加入群组 ");
        message.setTalker_type("G");
        message.setGroupID(groupID);
        ctx.writeAndFlush(message);
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void case7(Scanner scanner){
        System.out.println("输入你要撤除的管理员ID：");
        String s0=scanner.nextLine();
        while(!StringUtils.isNumber(s0)){
            System.out.println("输入不规范，请重新输入要撤销的管理员ID：");
            s0=scanner.nextLine();
        }
        int FriendID=Integer.parseInt(s0);

    }
}
