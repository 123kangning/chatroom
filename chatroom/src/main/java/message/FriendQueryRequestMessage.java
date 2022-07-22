package message;

public class FriendQueryRequestMessage extends Message{
    private int userID;
    public FriendQueryRequestMessage(){}
    public FriendQueryRequestMessage(int userID){
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
