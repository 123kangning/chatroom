package message;

public class SendFileMessage extends Message{
    private String fileName;
    private int fileLength=0;
    private int start,once;
    private byte[] file;
    public SendFileMessage(String fileName,int start,int once){
        this.fileName=fileName;
        this.start=start;
        this.once=once;
    }//客户端请求服务端发送文件
    public SendFileMessage(int start,byte[] file){//服务端向客户端回复消息
        this.start=start;
        this.file=file;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public int getStart() {
        return start;
    }

    public int getOnce() {
        return once;
    }

    public int getFileLength() {
        return fileLength;
    }
}
