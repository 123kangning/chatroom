package message;

public class FriendShieldRequestMessage extends Message{
    private long userID;
    private long FriendId;
    public FriendShieldRequestMessage(){}
    public FriendShieldRequestMessage(long userID,long FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }

    @Override
    public int getMessageType() {
        return FriendShieldRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }
}
