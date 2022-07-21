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
        long userID=9;
        long FriendID=18;
        String chat="HaHaHa";
        String sql="select * from friend where (toID =? and fromID=?) or (toID =? and fromID=?)";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setLong(1,userID);
        ps.setLong(2,FriendID);
        ps.setLong(3,FriendID);
        ps.setLong(4,userID);
        ResultSet set=ps.executeQuery();
        ResponseMessage message;
        if(set.next()){//找到添加的朋友
            long u1=set.getLong(1);
            long u2=set.getLong(2);
            String sql1="select online from test1 where userID=?";
            PreparedStatement ps1=connection.prepareStatement(sql1);

            if(u1==userID){//u2是朋友
                ps1.setLong(1,u2);
            }else{//u1是朋友
                ps1.setLong(1,u1);
            }
            ResultSet set1=ps1.executeQuery();
            if(set1.next()){
                if(set1.getString(1).equals("T")){
                    log.info("new ResponseMessage(true,\"\")");
                    message=new ResponseMessage(true,"");
                }else{
                    log.info("new ResponseMessage(true,\"但是朋友不在线\")");
                    message=new ResponseMessage(true,"但是朋友不在线");
                }
            }
        }else{//未找到
            log.info("new ResponseMessage(false,\"找不到该朋友\")");
            message=new ResponseMessage(false,"找不到该朋友");
        }
    }
}
