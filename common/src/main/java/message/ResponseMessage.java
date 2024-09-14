package message;

import java.util.List;
import java.util.Map;

public class ResponseMessage extends Message {
    private boolean success;
    private String reason;
    private int readCount = 0;
    private int mailAuthCode;
    private Map<String, List<String>> noticeMap;
    public List<String> friendList;
    private int fileLength;
    int MessageType = ResponseMessage;
    private int gradeInGroup;
    private String haveFile = "";

    public ResponseMessage() {
    }

    public ResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }


    public void setMailAuthCode(int mailAuthCode) {
        this.mailAuthCode = mailAuthCode;
    }

    public void setHaveFile(String haveFile) {
        this.haveFile = haveFile;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
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

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    public void setMessageType(int MessageType) {
        this.MessageType = MessageType;
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

    public int getFileLength() {
        return fileLength;
    }

    public String getHaveFile() {
        return haveFile;
    }

    public List<String> getFriendList() {
        return friendList;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public String getReason() {
        return this.reason;
    }

    public int getMessageType() {
        return this.MessageType;
    }

    public String toString() {
        return "success = " + success + ", reason = " + reason;
    }
}
