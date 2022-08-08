import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Scanner;

@Slf4j
public class TestBreakPoint {
    private static int fileLength=1024;
    private static String breakPointFilePath=System.getProperty("user.dir")+"/client/src/test/java/breakPoint";
    private static String sendFilePath;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner=new Scanner(System.in);
        RandomAccessFile breakPoint=new RandomAccessFile(breakPointFilePath,"rw");

        System.out.println("输入1查看breakPoint文件,输入0设置breakPoint文件");
        if(Integer.parseInt(scanner.nextLine())==1){
            while(breakPoint.read()!=-1){
                breakPoint.seek(breakPoint.getFilePointer()-1);
                sendFilePath=breakPoint.readUTF();
                RandomAccessFile file=new RandomAccessFile(sendFilePath,"rw");
                int sendLength=breakPoint.readInt(),sumLength=breakPoint.readInt();
                log.info("Path = {}, sendLength = {} ,sumLength = {},hashCode = {}",sendFilePath,sendLength,sumLength,file.hashCode());
            }
        }else{
            breakPoint.seek(breakPoint.length());
            System.out.println("请输入sendPath：");
            sendFilePath=scanner.nextLine();
            System.out.println("请输入writeLength:");
            int writeLength=Integer.parseInt(scanner.nextLine());
            breakPoint.writeUTF(sendFilePath);
            breakPoint.writeInt(writeLength);
            breakPoint.writeInt(1024);
        }
        breakPoint.close();
    }
}