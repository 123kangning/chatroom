package message;

public class ChangePasswordRequestMessage extends Message {
    private final int userID;
    private final String password;

    public ChangePasswordRequestMessage(int userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }
}
