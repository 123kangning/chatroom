package message;

public class SearchPasswordRequestMessage extends Message{
    private int userID;

    public SearchPasswordRequestMessage(){}
    public SearchPasswordRequestMessage(int userID){
        this.userID=userID;
    }

    public int getUserID() {
        return userID;
    }

    public String toString(){
        return "userID = "+userID;
    }
}
