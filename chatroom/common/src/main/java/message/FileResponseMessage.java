package message;

public class FileResponseMessage extends Message{
    String serverPath;
    int length;//读取了多少长度
    public FileResponseMessage(int length,String serverPath){
        this.length=length;
        this.serverPath=serverPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public int getLength() {
        return length;
    }
}
