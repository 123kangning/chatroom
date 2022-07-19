package message;

import io.netty.channel.MessageSizeEstimator;

public class FriendChatRequestMessage extends Message {
    private long userID;
    private long FriendId;
    public FriendChatRequestMessage(){}
    public FriendChatRequestMessage(long userID,long FriendId){
        this.userID=userID;
        this.FriendId=FriendId;
    }

    @Override
    public int getMessageType() {
        return FriendChatRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId;
    }
}
