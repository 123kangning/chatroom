package message;

public class FriendShieldRequestMessage extends Message{
    private int userID;
    private int FriendId;
    public FriendShieldRequestMessage(){}
    public FriendShieldRequestMessage(int userID,int FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }

/*    @Override
    public int getMessageType() {
        return FriendShieldRequestMessage;
    }*/
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }
}
