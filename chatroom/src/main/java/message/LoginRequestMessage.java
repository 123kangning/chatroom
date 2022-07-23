package message;

public class LoginRequestMessage extends Message{
    private int userID;
    private String password;

    public LoginRequestMessage(){}
    public LoginRequestMessage(int userID,String password){
        this.userID=userID;
        this.password=password;
    }
    public int getUserID(){return this.userID;}
    public String getPassword(){return this.password;}

/*    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }*/

    public String toString(){
        return "userID = "+userID+", password = "+password;
    }
}
