package server.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import server.service.User;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class SessionMap {
    private static ConcurrentHashMap<Long, Channel> map=new ConcurrentHashMap<>();
    public static void addSession(Long userID,Channel session){
        map.put(userID,session);
    }
    public static Channel getChannel(long userID){
        if(map.containsKey(userID)){
            return map.get(userID);
        }else{
            return null;
        }
    }

}
