package message;

public class GroupCheckGradeRequestMessage extends Message {
    int userID;
    int groupID;

    public GroupCheckGradeRequestMessage(int userID, int groupID) {
        this.userID = userID;
        this.groupID = groupID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupID() {
        return groupID;
    }
}
