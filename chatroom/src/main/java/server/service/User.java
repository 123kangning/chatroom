package server.service;

import server.session.Group;

import java.util.Map;
import java.util.Set;

public class User {
    //20位
    private String username;
    private long userID;
    private String password;
    private String phoneNumber;
    //用Integer表示在群组中的等级，0=普通成员、1=管理员、9=群主
    //在线状态
    private boolean onLine;
    public User(String username,String password){
        this.username=username;
        this.password=password;
    }
    public User(String username,String password,String phoneNumber){
        this.username=username;
        this.password=password;
        this.phoneNumber=phoneNumber;
    }
    public void setName(String username){this.username=username;}
    public void setPassword(String password){this.password=password;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public void setOnLine(boolean Online){this.onLine=onLine;}

    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public String getPhoneNumber(){return this.phoneNumber;}
    public boolean getOnLine(){return this.onLine;}
}
