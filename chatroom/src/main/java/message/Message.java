package message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {

    private int messageType;
    private int sequenceId;
/*    public abstract int getMessageType();*/

    public static final int noticeMapMessage=-2;
    public static final int ResponseMessage=-1;
    public static final int LoginRequestMessage=0;//登录
    public static final int LoginResponseMessage=1;
  public static final int LogoutRequestMessage=2;//退出
    public static final int LogoutResponseMessage=3;
  public static final int SignInRequestMessage=4;//注册
    public static final int SignInResponseMessage=5;
  public static final int SignOutRequestMessage=6;//注销
    public static final int SignOutResponseMessage=7;
  public static final int SearchPasswordRequestMessage=8;//找回密码
    public static final int SearchPasswordResponseMessage=9;

  public static final int GroupCreateRequestMessage=10;//创建群组
    public static final int GroupCreateResponseMessage=11;
  public static final int GroupJoinRequestMessage=12;//加入群组
    public static final int GroupJoinResponseMessage=13;
    public static final int GroupQuitRequestMessage=14;//退出群组
    public static final int GroupQuitResponseMessage=15;
  public static final int GroupDeleteRequestMessage=16;//删除群组
    public static final int GroupDeleteResponseMessage=17;
  public static final int GroupChatRequestMessage=18;//群组聊天
    public static final int GroupChatResponseMessage=19;
  public static final int GroupNumberRequestMessage=20;//获得群成员列表
    public static final int GroupNumberResponseMessage=21;

  public static final int FriendChatRequestMessage=22;//好友聊天
    public static final int FriendChatResponseMessage=23;
  public static final int FriendAddRequestMessage=24;//添加好友
    public static final int FriendAddResponseMessage=25;
  public static final int FriendShieldRequestMessage=26;//屏蔽好友
    public static final int FriendShieldResponseMessage=27;
  public static final int FriendDeleteRequestMessage=28;/*删除好友*/
    public static final int FriendDeleteResponseMessage=29;
    public static final int FriendQueryRequestMessage=30;/*查询好友列表*/
    public static final int FriendQueryResponseMessage=31;

    public int getSequenceId(){
        return sequenceId;
    }

}
