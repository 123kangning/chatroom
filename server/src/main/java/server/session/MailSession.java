package server.session;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSession {
    public static void email(String email, Integer authcode) throws Exception {
        MailSSLSocketFactory sf = null;

        sf = new MailSSLSocketFactory();

        sf.setTrustAllHosts(true);
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");//发送邮件协议
        properties.setProperty("mail.smtp.auth", "true");//需要验证
        properties.setProperty("mail.debug", "true");//设置debug模式 后台输出邮件发送的过程

        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(properties);
        session.setDebug(true);//debug模式
        //邮件信息
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("3390142252@qq.com"));//设置发送人
        message.setText("你的验证码为：" + authcode + "，请按照页面提示提交验证码，切勿将验证码提供给其他人。");//设置邮件内容
        message.setSubject("邮箱验证");//设置邮件主题
        //发送邮件
        Transport tran = session.getTransport();
        tran.connect("smtp.qq.com", 465, "3390142252", "yjysoegmqmytcgjc");//连接到qq邮箱服务器
        // tran.connect("smtp.qq.com",587, "Michael8@qq.vip.com", "xxxx");//连接到QQ邮箱服务器
        tran.sendMessage(message, new Address[]{new InternetAddress(email)});//设置邮件接收人
        tran.close();
    }
}
