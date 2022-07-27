package message;

public class GroupUnSayRequestMessage extends Message{
    private int unSayID;
    private int groupID;
    private String say;
    public GroupUnSayRequestMessage(int unSayID,int groupID,String say){
        this.unSayID=unSayID;
        this.groupID=groupID;
        this.say=say;
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
