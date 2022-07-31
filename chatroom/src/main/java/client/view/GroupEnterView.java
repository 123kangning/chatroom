package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.*;

import java.io.File;
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
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d ------+\n",myUserID);
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
            }
        }
    }
    public void choice2(ChannelHandlerContext ctx){
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d ------+\n",myUserID);
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
            }
        }
    }
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
                case 2:case2(scanner);break;
                case 3:case4(scanner,"T");break;
                case 4:case4(scanner,"F");break;
                case 5:case5(scanner);break;
                case 6:case6(scanner);break;
                case 7:case7(scanner);break;
                case 8:case8(scanner);break;
                case 9:case9(scanner);break;
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
    public void case2(Scanner scanner){
        ctx.writeAndFlush(new GroupNoticeRequestMessage(myUserID,groupID));
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(waitSuccess==1){
            talkWithGroup=groupID;
            int count=0;
            for(String s:friendList){
                System.out.println(s);
                if(haveFile.charAt(count)=='1'){
                    FriendView.receiveFile(s,scanner,ctx,groupID);
                }
                count++;
            }
        }else{
            return;
        }

        System.out.println("聊天内容(按下回车发送)[输入F表示发送文件][输入r退出]：");
        immediateGroup=true;
        String chat=scanner.nextLine();
        while(!chat.equalsIgnoreCase("r")){
            GroupChatRequestMessage message;
            if(chat.equalsIgnoreCase("F")){
                File file;
                System.out.println("请输入待发送的文件的[绝对路径]：");
                file=new File(scanner.nextLine());
                while(!file.exists()||!file.isFile()){
                    if(!file.exists()){
                        System.out.println("文件不存在，请重新输入待发送的文件的[绝对路径]");
                    }else{
                        System.out.println("不是文件，请重新输入待发送的文件的[绝对路径]");
                    }
                    file=new File(scanner.nextLine());
                }
                message=new GroupChatRequestMessage(myUserID,groupID);
                message.setMsg_type("F");
                message.setFile(file);
                ctx.writeAndFlush(message);
                try{
                    synchronized(waitMessage){
                        waitMessage.wait();
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }else if(chat.equalsIgnoreCase("Y")&&isY){
                checkRECV="y";
                synchronized (waitRVFile){
                    try {
                        waitRVFile.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkRECV="n";
                }
            }
            else {
                message=new GroupChatRequestMessage(myUserID,groupID);
                message.setMessage(chat);
                message.setMsg_type("S");
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
        ctx.writeAndFlush(new GroupCutManagerRequestMessage(FriendID,groupID));
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void case8(Scanner scanner){
        System.out.println("输入你要添加的管理员ID：");
        String s0=scanner.nextLine();
        while(!StringUtils.isNumber(s0)){
            System.out.println("输入不规范，请重新输入要添加的管理员ID：");
            s0=scanner.nextLine();
        }
        int FriendID=Integer.parseInt(s0);
        ctx.writeAndFlush(new GroupAddManagerRequestMessage(FriendID,groupID));
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void case9(Scanner scanner){
        ctx.writeAndFlush(new GroupDeleteRequestMessage(myUserID,groupID));
        try{
            synchronized(waitMessage){
                waitMessage.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
