package message;

public class FriendQueryRequestMessage extends Message {
    private int userID;

    public FriendQueryRequestMessage() {
    }

    public FriendQueryRequestMessage(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }


    public String toString() {
        return "userID = " + userID;
    }
}
