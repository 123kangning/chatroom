package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.Message;
import message.ResponseMessage;
import message.SendFileMessage;

import java.io.File;
import java.util.Scanner;

import static client.ChatClient.*;
import static message.Message.GroupCreateResponseMessage;

@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        boolean success = msg.getSuccess();
        String reason = msg.getReason();
        int ResponseMessageType = msg.getMessageType();
        if (!success) {
            System.out.print("操作失败 " + reason);
            waitSuccess = 0;
        } else {
            waitSuccess = 1;
            haveNoRead -= msg.getReadCount();
            mailAuthCode = msg.getMailAuthCode();
            if (msg.getMessageType() == Message.CheckGradeInGroup) {
                gradeInGroup = msg.getGradeInGroup();
            } else if (msg.getMessageType() == Message.FriendGetFileRequestMessage) {
                System.out.println("请选择一个目录将其保存下来[使用绝对路径]");
                setPath(msg.getReason());
                fileLength=msg.getFileLength();
                new StartReceiveFile(ctx,msg.getReason());
                return;
            } else if (msg.getMessageType() == Message.FriendQueryRequestMessage) {
                haveFile = msg.getHaveFile();
                friendList = msg.getFriendList();
                //log.info("friendList=msg.getFriendList()");
            } else if (msg.getMessageType() == Message.noticeMapMessage) {
                noticeMap = msg.getNoticeMap();
            }else if(msg.getMessageType()==Message.SignInResponseMessage){
                System.out.println("\t"+msg.getReason());
            }else if(msg.getMessageType()==Message.GroupCreateResponseMessage){
                System.out.println("\n\t"+msg.getReason());
            }
            else {
                // 操作成功时默认不输出任何内容
            }
        }
        synchronized (waitMessage) {
            waitMessage.notifyAll();
        }
    }

    public void setPath(String fileName) {
        Scanner scanner = new Scanner(System.in);
        String addFile = scanner.nextLine();
        File tempFile1 = new File(addFile);
        while (!tempFile1.isDirectory()) {
            System.out.println("不是目录，请重新输入：");
            addFile = scanner.nextLine();
            tempFile1 = new File(addFile);
        }
        if (addFile.charAt(addFile.length() - 1) != '/') {
            addFile = addFile.concat("/");
        }
        path=addFile.concat(fileName);
    }
}
