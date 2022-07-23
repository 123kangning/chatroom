import cn.hutool.core.date.DateTime;

import java.util.Scanner;

import static client.ChatClient.myUserID;

public class Test {
    public static void main(String[] args) {
       /*String s="aaa";
       System.out.println(s instanceof String);*/
        /*System.out.printf(new DateTime(System.currentTimeMillis()).toString());*/
        System.out.printf("\n\t+------------- 您的ID为 %8d --------------+\n",myUserID);
        System.out.println("\t+------------------消息通知栏-------------------+");
        System.out.printf("\t用户%8d 发来%3d条消息\n",108,10);
        System.out.printf("\t群组%8d 发来%3d条消息\n",108,107);

    }
}
