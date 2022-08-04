package message;

public class FriendApplyQueryMessage extends Message {
    int userID;

    public FriendApplyQueryMessage(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
