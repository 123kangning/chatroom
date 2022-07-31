package message;

public class GroupCutManagerRequestMessage extends Message{
    int managerID;
    int groupID;
    public GroupCutManagerRequestMessage(int managerID,int groupID){
        this.managerID=managerID;
        this.groupID=groupID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getManagerID() {
        return managerID;
    }
}
