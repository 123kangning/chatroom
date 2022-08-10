package message;

public class SendFileMessage extends Message{
    private String fileName;
    private String serverPath;
    private int percent;
    private int fileLength=0;
    private int start;
    private byte[] file;
    public SendFileMessage(String serverPath,byte[] file,int start,int fileLength){//客户端向服务端发文件
        this.serverPath=serverPath;
        this.file=file;
        this.start=start;
        this.fileLength=fileLength;
    }
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

    public int getStart() {
        return start;
    }

    public String getServerPath() {
        return serverPath;
    }

    public int getFileLength() {
        return fileLength;
    }
}
