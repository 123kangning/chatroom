package message;

public class GroupDeleteRequestMessage extends Message{
    private int userID;
    private int GroupId;
    public GroupDeleteRequestMessage(){}
    public GroupDeleteRequestMessage(int userID,int GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupId() {
        return GroupId;
    }

}
