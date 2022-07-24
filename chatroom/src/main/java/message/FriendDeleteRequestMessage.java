package message;

public class FriendDeleteRequestMessage extends Message{
    private int userID;
    private int FriendId;
    public FriendDeleteRequestMessage(){}
    public FriendDeleteRequestMessage(int userID,int FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }

    public int getUserID() {
        return userID;
    }

    public int getFriendId() {
        return FriendId;
    }

    /*    @Override
            public int getMessageType() {
                return FriendDeleteRequestMessage;
            }*/
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }
}
