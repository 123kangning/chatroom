package message;

public class SendApplyMessage extends Message {
    private final int userID;
    private final int friendID;
    private int groupID = 0;
    private final String message;
    private String Talker_type = "F";
    private String purpose = "F";//F为了添加朋友而发申请，G为了进入群组而发申请(这两个设置都是在Talker_type=F的前提下)

    public SendApplyMessage(int userID, int friendID, String message) {
        this.userID = userID;
        this.friendID = friendID;
        this.message = message;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setTalker_type(String talker_type) {
        Talker_type = talker_type;
    }

    public String getTalker_type() {
        return Talker_type;
    }

    public int getUserID() {
        return userID;
    }

    public String getPurpose() {
        return purpose;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getFriendID() {
        return friendID;
    }

    public String getMessage() {
        return message;
    }
}
