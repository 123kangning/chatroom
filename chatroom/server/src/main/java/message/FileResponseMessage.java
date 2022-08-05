package message;

public class FileResponseMessage extends Message{
    int length;//读取了多少长度
    public FileResponseMessage(int length){
        this.length=length;
    }

    public int getLength() {
        return length;
    }
}
