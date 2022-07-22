package message;

public class FriendAddRequestMessage extends Message{
    private int userID;
    private int FriendId;
    public FriendAddRequestMessage(){}
    public FriendAddRequestMessage(int userID,int FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }
    public int getUserId(){return this.userID;}
    public int getFriendId(){return this.FriendId;}

    @Override
    public int getMessageType() {
        return FriendAddRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }

}
