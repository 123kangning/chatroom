package message;

public class GroupQuitRequestMessage extends Message{
    private long userID;
    private long GroupId;
    public GroupQuitRequestMessage(){}
    public GroupQuitRequestMessage(long userID,long GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
