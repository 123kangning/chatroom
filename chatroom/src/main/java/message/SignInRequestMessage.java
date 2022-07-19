package message;

public class SignInRequestMessage extends Message{
    private String phoneNumber;
    private String username;
    private String password;

    public SignInRequestMessage(){}
    public SignInRequestMessage(String username,String password,String phoneNumber){
        this.username=username;
        this.password=password;
        this.phoneNumber=phoneNumber;
    }

    @Override
    public int getMessageType() {
        return SignInRequestMessage;
    }

    public String toString(){
        return "username = "+username+", password = "+password+", phoneNumber = "+phoneNumber;
    }
}
