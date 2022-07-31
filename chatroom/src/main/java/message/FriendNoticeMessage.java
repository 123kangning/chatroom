package message;

public class FriendNoticeMessage extends Message{
    int userID;
    int FriendID;
    int count=50;
    public FriendNoticeMessage(int  usrID,int FriendID){
        this.FriendID=FriendID;
        this.userID=usrID;
    }

    public int getUserID() {
        return userID;
    }

    public int getCount() {
        return count;
    }

    public int getFriendID() {
        return FriendID;
    }
}
