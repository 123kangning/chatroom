package message;

public class GroupChatRequestMessage extends Message{
    private int userID;
    private int GroupId;
    //talker默认为G=group
    public GroupChatRequestMessage(){}
    public GroupChatRequestMessage(int userID,int GroupId){
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
