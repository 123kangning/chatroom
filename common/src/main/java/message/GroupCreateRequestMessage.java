package message;

public class GroupCreateRequestMessage extends Message {
    private int userID;
    private String groupName;

    public GroupCreateRequestMessage() {
    }

    public GroupCreateRequestMessage(int userID, String groupName) {
        this.userID = userID;
        this.groupName = groupName;
    }

    public int getUserID() {
        return userID;
    }

    public String getGroupName() {
        return groupName;
    }

    /*
            @Override
            public int getMessageType() {
                return GroupChatRequestMessage;
            }*/
    public String toString() {
        return "userID = " + userID + ", groupName = " + groupName;
    }
}
