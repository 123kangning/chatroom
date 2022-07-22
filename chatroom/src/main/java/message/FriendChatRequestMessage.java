package message;

import io.netty.channel.MessageSizeEstimator;

public class FriendChatRequestMessage extends Message {
    private int userID;
    private int FriendId;
    private int chatType=0;//0=文本消息、1=文件
    //talker默认为F=friend
    private String message;
    public FriendChatRequestMessage(){}
    public FriendChatRequestMessage(int userID,int FriendId,String message){
        this.userID=userID;
        this.FriendId=FriendId;
        this.message=message;
    }
    public String getMessage(){return this.message;}
    public int getFriendId(){return this.FriendId;}
    public int getUserID(){return this.userID;}
    @Override
    public int getMessageType() {
        return FriendChatRequestMessage;
    }
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId+" message = "+message;
    }
}
