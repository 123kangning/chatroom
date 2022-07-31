package message;

public class GroupNoticeRequestMessage extends Message{
    private int userID;
    private int groupID;
    private int count=50;
    public GroupNoticeRequestMessage(int userID,int groupID){
        this.userID=userID;
        this.groupID=groupID;
    }

    public int getUserID() {
        return userID;
    }

    public int getCount() {
        return count;
    }

    public int getGroupID() {
        return groupID;
    }
}
