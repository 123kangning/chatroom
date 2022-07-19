package message;

public class SignOutRequestMessage extends Message{
    private long userID;

    public SignOutRequestMessage(){}
    public SignOutRequestMessage(long userID){
        this.userID=userID;
    }

    @Override
    public int getMessageType() {
        return SignOutRequestMessage;
    }

    public String toString(){
        return "userID = "+userID;
    }
}
