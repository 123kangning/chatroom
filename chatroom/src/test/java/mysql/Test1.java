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
        long userID=1;
        String phoneNumber="l4";
        String sql2="select userID from test1 where phoneNumber=?";
        PreparedStatement statement2=connection.prepareStatement(sql2);
        statement2.setString(1,phoneNumber);
        ResultSet set1=statement2.executeQuery();
        log.info("{}",set1.next());
        userID=set1.getLong(1);
    }
}
