package message;

public class SendFile1Message extends Message{

    private String serverPath;
    private int start;
    private byte[] file;
    private int fileLength=0;

    public SendFile1Message(String serverPath,byte[] file,int start,int fileLength){//客户端向服务端发文件
        this.serverPath=serverPath;
        this.file=file;
        this.start=start;
        this.fileLength=fileLength;
    }

    public String getServerPath() {
        return serverPath;
    }

    public int getFileLength() {
        return fileLength;
    }

    public byte[] getFile() {
        return file;
    }

    public int getStart() {
        return start;
    }
}
