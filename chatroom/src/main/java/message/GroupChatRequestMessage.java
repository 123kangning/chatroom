package message;

public class GroupChatRequestMessage extends Message{
    private long userID;
    private long GroupId;
    //talker默认为G=group
    public GroupChatRequestMessage(){}
    public GroupChatRequestMessage(long userID,long GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
