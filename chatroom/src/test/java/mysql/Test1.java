package mysql;

import lombok.extern.slf4j.Slf4j;
import message.ResponseMessage;

import java.sql.*;

import static server.ChatServer.connection;

@Slf4j
public class Test1 {
    private static final String URL="jdbc:mysql://localhost:3306/test";
     private static final String NAME="root";
     private static final String PASSWORD="9264wkn.";
    public static void main(String[] args)throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection= DriverManager.getConnection(URL,NAME,PASSWORD);

    }
}
