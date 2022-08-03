import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.GeneralSecurityException;
import java.util.Properties;


public class SendMailHandler {
    public void sendMail(String sendEmail, Address[] toEmilAddress, String title, String sendEmailPwd, String content) throws GeneralSecurityException, MessagingException {
        Properties props = new Properties();
        // 开启debug调试，以便在控制台查看
        props.setProperty("mail.debug", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = null;

        sf = new MailSSLSocketFactory();

        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);
        Message msg = new MimeMessage(session);
        // 发送的邮箱地址
        msg.setFrom(new InternetAddress(sendEmail));
        // 设置标题
        msg.setSubject(title);
        // 设置内容
        msg.setContent(content, "text/html;charset=utf-8;");
        Transport transport = null;
        transport = session.getTransport();

        // 设置服务器以及账号和密码
        // 这里端口改成465
        transport.connect("smtp.qq.com", 465, sendEmail, sendEmailPwd);

        if (transport != null) {
            // 发送到的邮箱地址
            transport.sendMessage(msg, toEmilAddress);
            transport.close();
        }
    }
}
