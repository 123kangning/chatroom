package client.view;

import com.alibaba.druid.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import message.NoticeRequestMessage;

import java.util.List;
import java.util.Scanner;

import static client.ChatClient.*;

public class NoticeView {
    public NoticeView(ChannelHandlerContext ctx){
        System.out.printf("\n\t+------------- 您的ID为 %8d --------------+\n",myUserID);
        System.out.println("\t+------------------消息通知栏-------------------+");
        System.out.println("\t|           0 -> 查看  |  1 -> 退出             |");
        System.out.println("\t+---------------------------------------------+");
        while(true){
            Scanner scanner=new Scanner(System.in);
            String s=scanner.nextLine();
            while(!StringUtils.isNumber(s)){
                System.out.println("输入不规范，请重新输入用户ID：");
                s=scanner.nextLine();
            }
            switch (Integer.parseInt(s)){
                case 0:{
                    ctx.writeAndFlush(new NoticeRequestMessage(myUserID));
                    try{
                        synchronized (waitMessage){
                            waitMessage.wait();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    List<String> friend=noticeMap.get("F");
                    List<String> group=noticeMap.get("G");
                    for(String s1:friend){
                        System.out.println(s1);
                    }
                    for(String s1:group){
                        System.out.println(s1);
                    }
                    break;
                }
                case 1:return;
            }




        }
    }
}
