package mysql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static server.ChatServer.connection;
@Slf4j
public class Test0 {
    private static final String URL="jdbc:mysql://localhost:3306/chatroom";
    private static final String NAME="root";
    private static final String PASSWORD="9264wkn.";
    public static void main(String[] args)throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection= DriverManager.getConnection(URL,NAME,PASSWORD);
        int userID=1;
        String sql="select  talkerID ,count(1) from message  where userID=? and talker_type='F'";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,userID);
        ResultSet set=ps.executeQuery();
        Map<String, List<String>> noticeMap=new HashMap<>();
        List<String> friend=new ArrayList<>();
        List<String> group=new ArrayList<>();
        while(set.next()){
                friend.add(String.format("\t用户%8d 发来%3d 条消息",set.getInt(1),set.getInt(2)));
                log.info(String.format("\t用户%8d 发来%3d 条消息",set.getInt(1),set.getInt(2)));
        }
        String sql1="select talkerID ,count(1) from message  where userID=? and talker_type='G'";
        PreparedStatement ps1=connection.prepareStatement(sql1);
        ps1.setInt(1,userID);
        ResultSet set1=ps1.executeQuery();
        /*log.info("set.next={}",set1.next());*/
        while(set1.next()){
            if(set1.getInt(2)==0)continue;
            group.add(String.format("\t群组%8d 发来%3d 条消息",set.getInt(1),set.getInt(2)));
            log.info(String.format("\t群组%8d 发来%3d 条消息",set.getInt(1),set.getInt(2)));
        }

    }
}
