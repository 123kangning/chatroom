package message;

import io.netty.channel.MessageSizeEstimator;

public class FriendChatRequestMessage extends Message {
    private long userID;
    private long FriendId;
    private String message;
    public FriendChatRequestMessage(){}
    public FriendChatRequestMessage(long userID,long FriendId,String message){
        this.userID=userID;
        this.FriendId=FriendId;
        this.message=message;
    }
    public String getMessage(){return this.message;}
    public long getFriendId(){return this.FriendId;}
    public long getUserID(){return this.userID;}
    @Override
    public int getMessageType() {
        return FriendChatRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }
}
