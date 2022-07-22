package message;

public class SignInRequestMessage extends Message{
    private String mail;
    private String username;
    private String password;

    public SignInRequestMessage(){}
    public SignInRequestMessage(String username,String password,String mail){
        this.username=username;
        this.password=password;
        this.mail=mail;
    }
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public String getMail(){return this.mail;}

    @Override
    public int getMessageType() {
        return SignInRequestMessage;
    }

    public String toString(){
        return "username = "+username+", password = "+password+", mail = "+mail;
    }
}
