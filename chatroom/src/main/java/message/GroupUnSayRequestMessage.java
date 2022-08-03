package message;

public class GroupUnSayRequestMessage extends Message {
    private final int unSayID;
    private final int groupID;
    private final String say;

    public GroupUnSayRequestMessage(int unSayID, int groupID, String say) {
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

    public int getGroupID() {
        return groupID;
    }
}
