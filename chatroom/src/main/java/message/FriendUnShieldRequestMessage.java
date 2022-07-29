package message;

public class FriendUnShieldRequestMessage extends Message{
    private int userID;
    private int FriendId;
    public FriendUnShieldRequestMessage(){}
    public FriendUnShieldRequestMessage(int userID,int FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }

    public int getFriendId() {
        return FriendId;
    }

    public int getUserID() {
        return userID;
    }
}
