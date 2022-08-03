package message;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ResponseMessage extends Message{
    private boolean success;
    private String reason;
    private int readCount=0;
    private int mailAuthCode;
    private Map<String, List<String>> noticeMap;
    public  List<String> friendList;
    int MessageType=ResponseMessage;
    private int gradeInGroup;
    private String haveFile="";
    private File file;//接收一个file实例化对象回来
    public ResponseMessage(){}
    public ResponseMessage(boolean success,String reason){
        this.success=success;
        this.reason=reason;
    }

    public void setMailAuthCode(int mailAuthCode) {
        this.mailAuthCode = mailAuthCode;
    }

    public void setHaveFile(String haveFile) {
        this.haveFile = haveFile;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public void setGradeInGroup(int gradeInGroup) {
        this.gradeInGroup = gradeInGroup;
    }

    public void setNoticeMap(Map<String, List<String>> noticeMap) {
        this.noticeMap = noticeMap;
    }

    public Map<String, List<String>> getNoticeMap() {
        return noticeMap;
    }

    public  void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    public void setMessageType(int MessageType){
        this.MessageType=MessageType;
    }

    public File getFile() {
        return file;
    }

    public int getMailAuthCode() {
        return mailAuthCode;
    }

    public int getGradeInGroup() {
        return gradeInGroup;
    }

    public int getReadCount() {
        return readCount;
    }

    public String getHaveFile() {
        return haveFile;
    }

    public  List<String> getFriendList() {
        return friendList;
    }

    public boolean getSuccess(){
        return this.success;
    }
    public String getReason(){
        return this.reason;
    }
    public int getMessageType() {
        return this.MessageType;
    }
    public String toString(){
        return "success = "+success+", reason = "+reason;
    }
}
