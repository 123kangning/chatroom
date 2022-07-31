package message;

import java.io.File;

public class GroupChatRequestMessage extends Message{
    private int userID;
    private int GroupId;
    //talker默认为G=group
    private String msg_type;//S=文本消息、F=文件
    private String message;
    private File file;
    public GroupChatRequestMessage(){}
    public GroupChatRequestMessage(int userID,int GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public String getMessage() {
        return message;
    }

    public File getFile() {
        return file;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupId() {
        return GroupId;
    }

    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
