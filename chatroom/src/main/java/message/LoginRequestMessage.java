package message;

public class LoginRequestMessage extends Message{
    private long userID;
    private String password;

    public LoginRequestMessage(){}
    public LoginRequestMessage(long userID,String password){
        this.userID=userID;
        this.password=password;
    }
    public long getUserID(){return this.userID;}
    public String getPassword(){return this.password;}

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }

    public String toString(){
        return "userID = "+userID+", password = "+password;
    }
}
