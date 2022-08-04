package message;

public class AuthCodeRequestMessage extends Message{
    private String mail;
    private int AuthCode;
    public AuthCodeRequestMessage(String mail){
        this.mail=mail;
    }

    public void setAuthCode(int authCode) {
        AuthCode = authCode;
    }

    public int getAuthCode() {
        return AuthCode;
    }

    public String getMail() {
        return mail;
    }
}
