package message;

public class LoginResponseMessage extends Message{
    boolean success;
    String reason;
    public LoginResponseMessage(){}
    public LoginResponseMessage(boolean success,String reason){
        this.success=success;
        this.reason=reason;
    }
    public int getMessageType() {
        return LoginResponseMessage;
    }
    public String toString(){
        return "success = "+success+", reason = "+reason;
    }
}
