package message;

public class GroupQuitRequestMessage extends Message{
    private int delID;
    private int GroupId;
    public GroupQuitRequestMessage(){}
    public GroupQuitRequestMessage(int userID,int GroupId){
        this.delID=userID;
        this.GroupId=GroupId;
    }

    public int getDelID() {
        return delID;
    }

    public int getGroupId() {
        return GroupId;
    }

    /*    @Override
            public int getMessageType() {
                return GroupQuitRequestMessage;
            }*/
}
