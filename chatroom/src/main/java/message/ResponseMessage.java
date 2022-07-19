package message;

public class ResponseMessage extends Message{
    boolean success;
    String reason;
    public ResponseMessage(){}
    public ResponseMessage(boolean success,String reason){
        this.success=success;
        this.reason=reason;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public String getReason(){
        return this.reason;
    }
    public int getMessageType() {
        return ResponseMessage;
    }
    public String toString(){
        return "success = "+success+", reason = "+reason;
    }
}
