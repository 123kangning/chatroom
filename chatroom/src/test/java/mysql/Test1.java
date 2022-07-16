package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test1 {
    private static final String URL="jdbc:mysql://localhost:3306/chatroom";
     private static final String NAME="root";
     private static final String PASSWORD="9264wkn.";
    public static void main(String[] args)throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection= DriverManager.getConnection(URL,NAME,PASSWORD);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from account");//选择import java.sql.ResultSet;
        while(rs.next()){//如果对象中有数据，就会循环打印出来
            System.out.println(rs.getString("name")+","+rs.getString("password")+", "
                    +rs.getString("sex")+", "+rs.getDate("birth"));
        }
    }
}
