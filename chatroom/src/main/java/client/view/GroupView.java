package client.view;

import io.netty.channel.ChannelHandlerContext;
import server.session.Group;

import java.util.Scanner;

import static client.ChatClient.myUserID;

public class GroupView {
    public GroupView(ChannelHandlerContext ctx){
        while(true){
            System.out.printf("\n\t+----- 您的ID为 %d -----+\n",myUserID);
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
            switch(Integer.parseInt(scanner.nextLine())) {
                case 1:return;
                case 2:
                case 4:
                case 5:
                case 6:
            }
        }
    }
}
