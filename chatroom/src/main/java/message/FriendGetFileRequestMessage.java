package message;

import java.io.File;
import java.io.PrintStream;

public class FriendGetFileRequestMessage extends Message{
    private int userID;
    private int FriendID;
    private boolean refuse=false;
    private boolean isGroup=false;
    private int groupID;
    public FriendGetFileRequestMessage(){}
    public FriendGetFileRequestMessage(int userID,int FriendID){
        this.userID=userID;
        this.FriendID=FriendID;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setRefuse(boolean refuse) {
        this.refuse = refuse;
    }

    public boolean isRefuse() {
        return refuse;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getUserID() {
        return userID;
    }

    public int getFriendID() {
        return FriendID;
    }


}
