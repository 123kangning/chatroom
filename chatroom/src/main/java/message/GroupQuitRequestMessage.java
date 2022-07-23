package message;

public class GroupQuitRequestMessage extends Message{
    private int userID;
    private int GroupId;
    public GroupQuitRequestMessage(){}
    public GroupQuitRequestMessage(int userID,int GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

/*    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }*/
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
