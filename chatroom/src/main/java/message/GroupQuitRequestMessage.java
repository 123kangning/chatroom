package message;

public class GroupQuitRequestMessage extends Message{
    private int delID;
    private int userID;
    private int GroupId;
    public GroupQuitRequestMessage(){}
    public GroupQuitRequestMessage(int delID,int GroupId,int userID){
        this.delID=delID;
        this.GroupId=GroupId;
        this.userID=userID;
    }

    public int getDelID() {
        return delID;
    }

    public int getGroupId() {
        return GroupId;
    }

    public int getUserID() {
        return userID;
    }
    /*    @Override
            public int getMessageType() {
                return GroupQuitRequestMessage;
            }*/
}
