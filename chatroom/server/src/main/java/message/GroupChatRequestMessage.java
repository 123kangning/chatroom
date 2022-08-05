package message;

import java.io.File;

public class GroupChatRequestMessage extends Message {
    private int userID;
    private int GroupId;
    //talker默认为G=group
    private String msg_type;//S=文本消息、F=文件
    private String message;
    private byte[] file;
    private int fileSize;
    private String path;
    private String fileName;
    public GroupChatRequestMessage() {
    }

    public GroupChatRequestMessage(int userID, int GroupId) {
        this.userID = userID;
        this.GroupId = GroupId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getFile() {
        return file;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupId() {
        return GroupId;
    }

    public String toString() {
        return "userID = " + userID + ", GroupId = " + GroupId;
    }
}
