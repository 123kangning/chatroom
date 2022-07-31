package message;

import java.util.List;

public class GroupJoinRequestMessage extends Message{
    private int userID;
    private int talkerID=0;
    private String talker_type="G";
    private int GroupId;
    private boolean noAdd=false;
    private boolean setList=false;
    private List<Integer> list;
    public GroupJoinRequestMessage(){}
    public GroupJoinRequestMessage(int userID,int GroupId){
        this.userID=userID;
        this.GroupId=GroupId;
    }
    public GroupJoinRequestMessage(int userID,List<Integer> list){
        this.userID=userID;
        this.list=list;
    }

    public void setNoAdd(boolean noAdd) {
        this.noAdd = noAdd;
    }

    public void setTalker_type(String talker_type) {
        this.talker_type = talker_type;
    }

    public void setTalkerID(int talkerID) {
        this.talkerID = talkerID;
    }

    public void setSetList(boolean setList) {
        this.setList = setList;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public boolean getSetList() {
        return setList;
    }

    public String getTalker_type() {
        return talker_type;
    }

    public int getTalkerID() {
        return talkerID;
    }

    public List<Integer> getList() {
        return list;
    }

    public int getUserID() {
        return userID;
    }
    public boolean getNoAdd(){
        return noAdd;
    }
    public int getGroupId() {
        return GroupId;
    }

    /*    @Override
            public int getMessageType() {
                return GroupJoinRequestMessage;
            }*/
    public String toString(){
        return "userID = "+userID+", GroupId = "+GroupId;
    }
}
