package message;

import java.util.List;

public class FriendAddRequestMessage extends Message {
    private int userID;
    private List<Integer> FriendIDList;
    private int FriendId;
    private boolean setList = false;
    private boolean noAdd;
    public FriendAddRequestMessage() {
    }

    public FriendAddRequestMessage(int userID, int FriendId) {
        this.userID = userID;
        this.FriendId = FriendId;
    }

    public FriendAddRequestMessage(int userID, List<Integer> FriendIdList) {
        this.userID = userID;
        this.FriendIDList = FriendIdList;
    }

    public void setSetList(boolean setList) {
        this.setList = setList;
    }

    public void setNoAdd(boolean noAdd) {
        this.noAdd = noAdd;
    }

    public boolean getSetList() {
        return setList;
    }

    public boolean isNoAdd() {
        return noAdd;
    }

    public int getUserId() {
        return this.userID;
    }

    public int getFriendId() {
        return this.FriendId;
    }

    public List<Integer> getFriendIDList() {
        return FriendIDList;
    }

    /*    @Override
        public int getMessageType() {
            return FriendAddRequestMessage;
        }*/
    public String toString() {
        return "userID = " + userID + ", FriendId = " + FriendId;
    }

}
