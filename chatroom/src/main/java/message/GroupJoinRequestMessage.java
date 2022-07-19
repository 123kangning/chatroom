package message;

public class GroupJoinRequestMessage extends Message{
    private long userID;
    private long GroupId;
    public GroupJoinRequestMessage(){}
    public GroupJoinRequestMessage(long userID,long GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
