package message;

public class SignOutRequestMessage extends Message{
    private int userID;

    public SignOutRequestMessage(){}
    public SignOutRequestMessage(int userID){
        this.userID=userID;
    }
    public int getUserID(){return this.userID;}
    @Override
    public int getMessageType() {
        return SignOutRequestMessage;
    }

    public String toString(){
        return "userID = "+userID;
    }
}
