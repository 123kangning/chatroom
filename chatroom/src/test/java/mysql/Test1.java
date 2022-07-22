package mysql;

import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import lombok.extern.slf4j.Slf4j;
import message.ResponseMessage;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Scanner;

import static server.ChatServer.connection;

@Slf4j
public class Test1 {
    private static final String URL="jdbc:mysql://localhost:3306/test";
     private static final String NAME="root";
     private static final String PASSWORD="9264wkn.";
    public static void main(String[] args)throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection= DriverManager.getConnection(URL,NAME,PASSWORD);
        String sql="insert into message(msg_type,is_send,create_date,content,talker) values(?,?,?,?,?)";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,"S");
        ps.setString(2,"S");
        ps.setDate(3,new Date(System.currentTimeMillis()));

        //ps.setString(4,"只是测试");
        byte[] bytes= FileCopyUtils.copyToByteArray(new File("src/description.txt"));
        ps.setBytes(4,bytes);
        ps.setString(5,"F");
        int row=ps.executeUpdate();
        log.info("row = "+row);

    }
}
