package mysql;

import lombok.extern.slf4j.Slf4j;
import message.ResponseMessage;

import java.sql.*;

@Slf4j
public class Test1 {
    private static final String URL="jdbc:mysql://localhost:3306/test";
     private static final String NAME="root";
     private static final String PASSWORD="9264wkn.";
    public static void main(String[] args)throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection= DriverManager.getConnection(URL,NAME,PASSWORD);
        Statement stmt = connection.createStatement();
        long userID=1;
        String password="zhang3";
        String sql="select online from test1 where userID=? and password=? ";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setLong(1,userID);
        statement.setObject(2,password);

        //log.info("statement = "+statement.get);
        ResultSet set= statement.executeQuery();
        if(set==null){
            log.info("set == null,没找到");
        }else{
            log.info("next 不为空！！！！！！！！！！");
        }
        if(set.next()&&set.getString(1).equals("F")){
            log.info("登录成功");
        }else{
            log.info("登录失败");
        }
    }
}
