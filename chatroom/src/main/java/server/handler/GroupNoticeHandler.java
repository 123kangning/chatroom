package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.GroupNoticeRequestMessage;
import message.Message;
import message.ResponseMessage;

import static server.ChatServer.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
public class GroupNoticeHandler extends SimpleChannelInboundHandler<GroupNoticeRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupNoticeRequestMessage msg) throws Exception {
        int userID=msg.getUserID();
        int groupID=msg.getGroupID();
        int count=msg.getCount();
        String sql="select talkerID,talker_type,content,isAccept,msg_type from message where (msg_type='S' or msg_type='F') and groupID=? and talker_type='G' and userID=? order by msg_id desc limit ?";
        PreparedStatement ps= connection.prepareStatement(sql);
        ps.setInt(1,groupID);
        ps.setInt(2,userID);
        ps.setInt(3,count);
        ResultSet set=ps.executeQuery();
        List<String> list=new ArrayList<>();
        int count1=0;
        StringBuilder ans=new StringBuilder();
        while(set.next()){
            int talkerID=set.getInt(1);
            if(set.getString(4).equals("F")&&talkerID!=userID){//统计未读消息条数
                count1++;
            }
            if(set.getString(5).equals("F")&&set.getString(4).equals("F")){//标记未处理文件
                ans.append("1");
            }else{
                ans.append("0");
            }
            String people=Id2Type(talkerID,groupID);
            String s;
            if(talkerID==userID){
                s=String.format("\t\t\t%50s:%d",set.getString(3),userID);
            }else{
                s=String.format("%3s %d:%s",people,talkerID,set.getString(3));
            }
            log.info(s);
            list.add(s);
        }
        Collections.reverse(list);
        ans.reverse();
        ResponseMessage message=new ResponseMessage(true,"");
        message.setFriendList(list);
        message.setMessageType(Message.FriendQueryRequestMessage);
        message.setHaveFile(String.valueOf(ans));
        log.info("finally ans = {}",ans);
        sql="update message set isAccept ='T' where talker_type='G' and (msg_type='F' or msg_type='S') and userID=? and groupID=?";
        ps= connection.prepareStatement(sql);
        ps.setInt(1,userID);
        ps.setInt(2,groupID);
        ps.executeUpdate();

        log.info("count in GroupNoticeHandler = {}",count1);
        message.setReadCount(count1);
        ctx.writeAndFlush(message);
    }
    public String Id2Type(int userID,int groupID)throws Exception{
        String sql="select user_type from group2 where userID=? and groupID=?";
        PreparedStatement ps= connection.prepareStatement(sql);
        ps.setInt(1,userID);
        ps.setInt(2,groupID);
        ResultSet set=ps.executeQuery();
        if(set.next()){
            String ans= set.getString(1);
            if(ans.equals("1")){
                return "管理员";
            }else if(ans.equals("9")){
                return "群主";
            }else{
                return "成员";
            }
        }
        return "成员";
    }
}
