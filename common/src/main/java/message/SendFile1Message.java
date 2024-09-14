package message;

public class SendFile1Message extends Message{

    private String serverPath;
    private int start;
    private byte[] file;
    private int fileLength=0;
    private byte ChatType=0;//0->朋友聊天, 1->群组聊天

    public SendFile1Message(String serverPath,byte[] file,int start,int fileLength){//客户端向服务端发文件
        this.serverPath=serverPath;
        this.file=file;
        this.start=start;
        this.fileLength=fileLength;
    }

    public void setChatType(byte chatType) {
        ChatType = chatType;
    }

    public String getServerPath() {
        return serverPath;
    }

    public int getFileLength() {
        return fileLength;
    }

    public byte getChatType() {
        return ChatType;
    }

    public byte[] getFile() {
        return file;
    }

    public int getStart() {
        return start;
    }
}
