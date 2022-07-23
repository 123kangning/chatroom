package message;

public class LogoutRequestMessage extends Message{
    private int userID;

    public LogoutRequestMessage(){}
    public LogoutRequestMessage(int userID){
        this.userID=userID;
    }
    public int getUserID(){return this.userID;}

/*    @Override
    public int getMessageType() {
        return LogoutRequestMessage;
    }*/

    public String toString(){
        return "userID = "+userID;
    }
}
