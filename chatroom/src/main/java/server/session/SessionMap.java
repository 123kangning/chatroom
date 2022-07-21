package server.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import server.service.User;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class SessionMap {
    private static ConcurrentHashMap<User, Channel> map=new ConcurrentHashMap<>();
    public static void addSession(User user,Channel session){
        map.put(user,session);
    }
    public static Channel getChannel(User user){
        if(map.containsKey(user)){
            return map.get(user);
        }else{
            return null;
        }
    }

}
