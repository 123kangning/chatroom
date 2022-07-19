package message;

public class FriendDeleteRequestMessage extends Message{
    private long userID;
    private long FriendId;
    public FriendDeleteRequestMessage(){}
    public FriendDeleteRequestMessage(long userID,long FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }

    @Override
    public int getMessageType() {
        return FriendDeleteRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }
}
