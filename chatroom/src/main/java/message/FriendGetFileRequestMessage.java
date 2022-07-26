package message;

import java.io.File;
import java.io.PrintStream;

public class FriendGetFileRequestMessage extends Message{
    private int userID;
    private int FriendID;
    public FriendGetFileRequestMessage(){}
    public FriendGetFileRequestMessage(int userID,int FriendID){
        this.userID=userID;
        this.FriendID=FriendID;
    }

    public int getUserID() {
        return userID;
    }

    public int getFriendID() {
        return FriendID;
    }


}
