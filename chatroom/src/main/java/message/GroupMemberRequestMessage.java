package message;

public class GroupMemberRequestMessage extends Message {
    private int GroupId;

    public GroupMemberRequestMessage() {
    }

    public GroupMemberRequestMessage(int GroupId) {
        this.GroupId = GroupId;
    }

    public int getGroupId() {
        return GroupId;
    }
/*    @Override
    public int getMessageType() {
        return GroupNumberRequestMessage;
    }*/

}
