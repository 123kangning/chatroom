package message;

import server.session.MailSession;

public class ChangePasswordRequestMessage extends Message {
    private int userID;
    private String password;
    public ChangePasswordRequestMessage(int userID,String password){
        this.userID=userID;
        this.password=password;
    }

    public int getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }
}
