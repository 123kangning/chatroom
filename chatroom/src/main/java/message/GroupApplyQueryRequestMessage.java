package message;

public class GroupApplyQueryRequestMessage extends Message{
    int userID;
    public GroupApplyQueryRequestMessage(int userID){
        this.userID=userID;
    }

    public int getUserID() {
        return userID;
    }
}
