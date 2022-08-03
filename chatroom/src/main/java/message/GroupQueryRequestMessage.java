package message;

public class GroupQueryRequestMessage extends Message {
    int userID;

    public GroupQueryRequestMessage(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
