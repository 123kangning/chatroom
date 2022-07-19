package message;

public class GroupNumberRequestMessage extends Message{
    private long userID;
    private long GroupId;
    public GroupNumberRequestMessage(){}
    public GroupNumberRequestMessage(long userID,long GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    @Override
    public int getMessageType() {
        return GroupNumberRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
