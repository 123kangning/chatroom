package mysql;

import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import lombok.extern.slf4j.Slf4j;
import message.ResponseMessage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Scanner;

import static server.ChatServer.connection;

@Slf4j
public class Test2 {
    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String NAME = "root";
    private static final String PASSWORD = "9264wkn.";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, NAME, PASSWORD);
        String sql = "select content from message where msg_id=6";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet set = ps.executeQuery();
        /*if(set.next()){
            String s=set.getString(1);
            log.info("message={}",s);
        }*/
        if (set.next()) {
            log.info("有的");
            //ObjectInputStream ois=new ObjectInputStream(set.getObject(1));
            byte[] bytes = set.getBytes(1);
           /* Scanner scanner=new Scanner(new BufferedInputStream(bytes));
            while(scanner.hasNext())
                System.out.println(scanner.nextLine());*/
            log.info(new String(bytes));
        } else {
            log.info("没有的");
        }
    }
}
