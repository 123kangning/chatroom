package message;

import java.io.File;

public class FriendChatRequestMessage extends Message {
    private int userID;
    private int FriendId;
    private String msg_type;//S=文本消息、F=文件
    //talker默认为F=friend
    private String message;
    private String talker_type;//G=消息来自群组,F=消息来自个人
    private File file;
    private int Group=0;
    public FriendChatRequestMessage(){}
    public FriendChatRequestMessage(int userID,int FriendId,String message,String msg_type){
        this.userID=userID;
        this.FriendId=FriendId;
        this.message=message;
        this.msg_type=msg_type;
    }
    public FriendChatRequestMessage(int userID, int FriendId, File file, String msg_type){
        this.userID=userID;
        this.FriendId=FriendId;
        this.file=file;
        this.msg_type=msg_type;
    }

    public void setTalker_type(String talker_type) {
        this.talker_type = talker_type;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setGroup(int group) {
        Group = group;
    }

    public String getTalker_type() {
        return talker_type;
    }

    public int getGroup() {
        return Group;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public File getFile() {
        return file;
    }

    public String getMessage(){return this.message;}
    public int getFriendId(){return this.FriendId;}
    public int getUserID(){return this.userID;}
/*    @Override
    public int getMessageType() {
        return FriendChatRequestMessage;
    }*/
    public String toString(){
        return "userID = "+userID+", FriendId = "+FriendId+" message = "+message;
    }
}
