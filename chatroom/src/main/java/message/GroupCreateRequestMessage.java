package message;

public class GroupCreateRequestMessage extends Message{
    private int userID;
    private int GroupID;
    public GroupCreateRequestMessage(){}
    public GroupCreateRequestMessage(int userID,int GroupID){
        this.userID=userID;
        this.GroupID=GroupID;
    }
/*
    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }*/
    public String toString(){
        return "userID = "+userID+", GroupID = "+GroupID;
    }
}
