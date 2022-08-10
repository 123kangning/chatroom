import lombok.extern.slf4j.Slf4j;
import message.PingMessage;
import message.*;

@Slf4j
public class Test {
    public static void main(String[] args){
       byte[] bytes=new byte[]{1};
       log.info("length = {},bytes[0] is {}",bytes.length,bytes[0]);
    }
}
