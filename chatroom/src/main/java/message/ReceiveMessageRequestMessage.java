package message;

import java.util.List;
import java.util.PrimitiveIterator;

public class ReceiveMessageRequestMessage extends Message{
    private int msg_id;
    private List<Integer> list;

    public ReceiveMessageRequestMessage(int msg_id){
        this.msg_id=msg_id;
    }
    public ReceiveMessageRequestMessage(List<Integer> list){
        this.list=list;
    }

    public List<Integer> getList() {
        return list;
    }

    public int getMsg_id() {
        return msg_id;
    }
}
