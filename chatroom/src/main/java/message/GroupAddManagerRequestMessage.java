package message;

public class GroupAddManagerRequestMessage extends Message{
    int managerID;
    int groupID;
    public GroupAddManagerRequestMessage(int managerID,int groupID){
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
