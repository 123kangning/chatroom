package message;

public class LogoutRequestMessage extends Message{
    private long userID;

    public LogoutRequestMessage(){}
    public LogoutRequestMessage(long userID){
        this.userID=userID;
    }
    public long getUserID(){return this.userID;}

    @Override
    public int getMessageType() {
        return LogoutRequestMessage;
    }

    public String toString(){
        return "userID = "+userID;
    }
}
