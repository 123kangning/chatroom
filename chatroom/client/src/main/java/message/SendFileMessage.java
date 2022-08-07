package message;

public class SendFileMessage extends Message{
    private String fileName;
    private int percent;
    private byte[] file;

    public SendFileMessage(String fileName){
        this.fileName=fileName;
    }//客户端请求服务端发送文件
    public SendFileMessage(int percent,byte[] file){//服务端向客户端回复消息
        this.percent=percent;
        this.file=file;
    }

    public int getPercent() {
        return percent;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }
}
