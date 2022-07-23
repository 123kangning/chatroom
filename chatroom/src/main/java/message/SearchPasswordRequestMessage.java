package message;

public class SearchPasswordRequestMessage extends Message{
    private int userID;

    public SearchPasswordRequestMessage(){}
    public SearchPasswordRequestMessage(int userID){
        this.userID=userID;
    }

/*    @Override
    public int getMessageType() {
        return SearchPasswordRequestMessage;
    }*/

    public String toString(){
        return "userID = "+userID;
    }
}
