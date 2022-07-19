package message;

public class FriendQueryRequestMessage extends Message{
    private long userID;
    public FriendQueryRequestMessage(){}
    public FriendQueryRequestMessage(long userID){
        this.userID=userID;
    }

    @Override
    public int getMessageType() {
        return FriendQueryRequestMessage;
    }
    public String toString(){
        return "userID = "+userID;
    }
}
