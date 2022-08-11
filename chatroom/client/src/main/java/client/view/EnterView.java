package client.view;

import client.ChatClient;
import com.alibaba.druid.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.*;

import java.util.Scanner;
import java.util.regex.Pattern;

import static client.ChatClient.*;

@Slf4j
public class EnterView {
    public EnterView(ChannelHandlerContext ctx) {

        while (true) {
            ByteBuf buf = ctx.alloc().buffer();
            System.out.println("\n\t+-------------------+");
            System.out.println("\t|   1   登录      \t|");
            System.out.println("\t+-------------------+");
            System.out.println("\t|   2   注册      \t|");
            System.out.println("\t+-------------------+");
            System.out.println("\t|   3   找回密码   \t|");
            System.out.println("\t+-------------------+");
            System.out.println("\t|   0   退出系统  \t|");
            System.out.println("\t+-------------------+\n");
            System.out.println("输入你的选择：");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();

            while (!StringUtils.isNumber(s)) {
                System.out.println("输入不规范，请重新输入您的选择：");
                s = scanner.nextLine();
            }
            switch (Integer.parseInt(s)) {
                case 1: {
                    System.out.println("请输入用户ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int userID = Integer.parseInt(s1);
                    ChatClient.myUserID = userID;
                    System.out.println("请输入密码：");
                    String password = scanner.nextLine();
                    //System.out.println("username="+userID+", password="+password);

                    LoginRequestMessage message = new LoginRequestMessage(userID, password);
                    ctx.writeAndFlush(message);

                    ChatClient.wait1();

                    if (waitSuccess == 1) {
                        login=true;
                        new MainView(ctx);
                    }
                    break;
                }
                case 2: {
                    System.out.println("请输入用户名称(<=50位)：");
                    String username = scanner.nextLine();
                    while (username.length() > 50) {
                        System.out.println("名称过长，请重新输入用户名称：");
                        username = scanner.nextLine();
                    }
                    System.out.println("请输入密码：");
                    String password1 = scanner.nextLine();
                    System.out.println("请输入邮箱地址用于接收验证码(形如:xxx@qq.com、xxx@gmail.com)：");
                    String mail = scanner.nextLine();
                    String pattern="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
                    while (!Pattern.matches(pattern,mail)) {
                        System.out.println("邮箱格式不正确，请重新输入：");
                        mail = scanner.nextLine();
                    }
                    ctx.writeAndFlush(new AuthCodeRequestMessage(mail));

                    ChatClient.wait1();

                    if (waitSuccess == 1) {
                        System.out.println("请输入你收到的验证码：");
                        String s1;
                        for(int i=0;i<2;i++){
                            s1= scanner.nextLine();
                            int AuthCode = Integer.parseInt(s1);
                            if (AuthCode == mailAuthCode) {
                                SignInRequestMessage message1 = new SignInRequestMessage(username, password1, mail);
                                ctx.writeAndFlush(message1);

                                ChatClient.wait1();

                                break;
                            }else{
                                if(i==0){
                                    System.out.println("输入错误，你还有一次机会");
                                }else{
                                    System.out.println("注册失败");
                                }
                            }
                        }

                    }
                    break;
                }
                case 3: {
                    System.out.println("请输入你的ID：");
                    String s1 = scanner.nextLine();
                    while (!StringUtils.isNumber(s1)) {
                        System.out.println("输入不规范，请重新输入用户ID：");
                        s1 = scanner.nextLine();
                    }
                    int userID = Integer.parseInt(s1);
                    ctx.writeAndFlush(new SearchPasswordRequestMessage(userID));

                    ChatClient.wait1();

                    if (waitSuccess == 1) {
                        System.out.println("请输入你收到的验证码：");
                        s1 = scanner.nextLine();
                        int AuthCode = Integer.parseInt(s1);
                        if (AuthCode == mailAuthCode) {
                            System.out.println("请重新设置你的密码：");
                            s1 = scanner.nextLine();
                            while (s1.length() < 3) {
                                System.out.println("密码过短，请重新输入密码：");
                                s1 = scanner.nextLine();
                            }
                            ctx.writeAndFlush(new ChangePasswordRequestMessage(userID, s1));

                            ChatClient.wait1();

                            if (waitSuccess == 1) {
                                System.out.println("密码重置成功");
                            }
                        }
                    }
                    break;
                }
                case 0: {
                    System.out.println("\t-----正在退出系统-----");
                    ctx.channel().close();
                    return;
                }
            }
        }
    }
}
