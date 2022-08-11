package message;

public class GroupUnSayRequestMessage extends Message {
    private int userID;
    private final int unSayID;
    private final int groupID;
    private final String say;

    public GroupUnSayRequestMessage(int userID,int unSayID, int groupID, String say) {
        this.userID=userID;
        this.unSayID = unSayID;
        this.groupID = groupID;
        this.say = say;
    }

    public int getUnSayID() {
        return unSayID;
    }

    public String getSay() {
        return say;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupID() {
        return groupID;
    }
}
