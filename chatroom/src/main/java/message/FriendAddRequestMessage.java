package message;

public class FriendAddRequestMessage extends Message{
    private String username;
    private String FriendId;
    public FriendAddRequestMessage(){}
    public FriendAddRequestMessage(String username,String FriendId){
        this.username=username;
        this.FriendId=FriendId;
    }

    @Override
    public int getMessageType() {
        return FriendAddRequestMessage;
    }
    public String toString(){
        return "username = "+username+", FriendId = "+FriendId;
    }

}
