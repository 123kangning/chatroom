package message;

public class GroupDeleteRequestMessage extends Message{
    private int userID;
    private int GroupId;
    public GroupDeleteRequestMessage(){}
    public GroupDeleteRequestMessage(int userID,int GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

/*    @Override
    public int getMessageType() {
        return GroupDeleteRequestMessage;
    }*/
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
