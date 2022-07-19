package server.session;

import server.service.User;

import java.nio.channels.Channel;
import java.util.List;
import java.util.Set;

public interface GroupSession {
    /*
    * 创建一个群聊
    * username：群主ID
    * groupName：群聊名称
    * 返回值：group对象（创建失败时返回null）
    * */
    Group createGroup(long userID,String groupName);
    /*
    * 加入聊天组
    * username：申请人ID
    * groupId：群聊ID
    * 返回值：group对象（申请失败时返回null）
    * */
    Group JoinGroup(long userID,long groupId);
    /*
    * 从指定聊天组中移除指定成员
    * GroupId：群聊ID
    * memberName：要移除的成员ID
    * 返回值：group对象（移除失败时返回null）
    * */
    Group removeMember(long userID,long groupId);
    /*
    * 解散聊天组
    * groupId：群聊ID
    * username：执行人ID
    * 返回值：group对象（删除失败时返回null）
    * */
    Group removeGroup(long userID,long groupId);
    /*
    * 获取群成员
    * groupID：群聊ID
    * 返回值：成员集合
    * */
    Set<User> getMember(long groupID);
    /*
    * 获取成员的channel集合，只有在线的channel会返回
    * groupID：群聊ID
    * 返回值：channel列表
    * */
    List<Channel> getMembersChannel(long groupID);
}
