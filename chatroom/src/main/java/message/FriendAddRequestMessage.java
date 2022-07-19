package message;

public class FriendAddRequestMessage extends Message{
    private long userID;
    private long FriendId;
    public FriendAddRequestMessage(){}
    public FriendAddRequestMessage(long userID,long FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }
    public long getUserId(){return this.userID;}
    public long getFriendId(){return this.FriendId;}

    @Override
    public int getMessageType() {
        return FriendAddRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }

}
