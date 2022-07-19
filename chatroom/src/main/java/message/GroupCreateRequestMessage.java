package message;

public class GroupCreateRequestMessage extends Message{
    private long userID;
    private long GroupID;
    public GroupCreateRequestMessage(){}
    public GroupCreateRequestMessage(long userID,long GroupID){
        this.userID=userID;
        this.GroupID=GroupID;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", GroupID = "+GroupID;
    }
}
