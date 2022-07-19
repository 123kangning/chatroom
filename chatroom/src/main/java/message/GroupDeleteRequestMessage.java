package message;

public class GroupDeleteRequestMessage extends Message{
    private long userID;
    private long GroupId;
    public GroupDeleteRequestMessage(){}
    public GroupDeleteRequestMessage(long userID,long GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    @Override
    public int getMessageType() {
        return GroupDeleteRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
