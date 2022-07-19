package message;

public class SearchPasswordRequestMessage extends Message{
    private long userID;

    public SearchPasswordRequestMessage(){}
    public SearchPasswordRequestMessage(long userID){
        this.userID=userID;
    }

    @Override
    public int getMessageType() {
        return SearchPasswordRequestMessage;
    }

    public String toString(){
        return "userID = "+userID;
    }
}
