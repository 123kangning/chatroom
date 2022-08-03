package message;

public class NoticeRequestMessage extends Message {
    int userID;

    public NoticeRequestMessage(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
