package message;

import java.util.List;
import java.util.Map;

public class ResponseMessage extends Message{
    private boolean success;
    private String reason;
    private Map<String, List<String>> noticeMap;
    public  List<String> friendList;
    int MessageType=ResponseMessage;
    public ResponseMessage(){}
    public ResponseMessage(boolean success,String reason){
        this.success=success;
        this.reason=reason;
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
