package message;

public class GroupJoinRequestMessage extends Message{
    private int userID;
    private int GroupId;
    public GroupJoinRequestMessage(){}
    public GroupJoinRequestMessage(int userID,int GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

/*    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }*/
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
