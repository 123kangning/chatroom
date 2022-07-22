package server.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import server.service.User;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class SessionMap {
    private static ConcurrentHashMap<Integer, Channel> mapToChannel=new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Channel,Integer> mapToUser=new ConcurrentHashMap<>();
    public static void add(int userID,Channel session){
        mapToChannel.put(userID,session);
        mapToUser.put(session,userID);
    }
    public static Channel getChannel(int userID){
        if(mapToChannel.containsKey(userID)){
            return mapToChannel.get(userID);
        }else{
            return null;
        }
    }
    public static int getUser(Channel channel){
        if(mapToUser.containsKey(channel)){
            return mapToUser.get(channel);
        }else{
            return 0;
        }
    }

}
