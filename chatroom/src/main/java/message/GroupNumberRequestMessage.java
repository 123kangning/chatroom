package message;

public class GroupNumberRequestMessage extends Message{
    private int userID;
    private int GroupId;
    public GroupNumberRequestMessage(){}
    public GroupNumberRequestMessage(int userID,int GroupId){
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
