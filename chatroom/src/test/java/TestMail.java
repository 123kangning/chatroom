import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.security.GeneralSecurityException;

public class TestMail {
    public static void main(String[] args) {
        SendMailHandler handler=new SendMailHandler();
        try {
            handler.sendMail("kangningwang254@gmail.com",new Address[]{new InternetAddress("3390142252@qq.com")},"找回密码","axgppxpfftgfdbcg","testContent");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
