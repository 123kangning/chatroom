package message;

public class CheckHaveMessageRequestMessage extends Message{
    private int userID;
    private String msg_type;
    private int talkerID;
    private String talker_type;
    private int groupID;
    private String content;
    private String isAccept;
    public CheckHaveMessageRequestMessage(int userID,String msg_type,int talkerID,String talker_type,int groupID,String content,String isAccept){
        this.userID=userID;
        this.groupID=groupID;
        this.isAccept=isAccept;
        this.msg_type=msg_type;
        this.talkerID=talkerID;
        this.content=content;
        this.talker_type=talker_type;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getTalker_type() {
        return talker_type;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public int getTalkerID() {
        return talkerID;
    }

    public String getContent() {
        return content;
    }

    public String getIsAccept() {
        return isAccept;
    }
}
