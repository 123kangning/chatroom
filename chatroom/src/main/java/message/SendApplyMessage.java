package message;

public class SendApplyMessage extends Message{
    private int userID;
    private int friendID;
    private int groupID=0;
    private String message;
    public String Talker_type="F";
    public SendApplyMessage(int userID,int friendID,String message){
        this.userID=userID;
        this.friendID=friendID;
        this.message=message;
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
