package server.service;

import server.session.Group;

import java.util.Map;
import java.util.Set;

public class User {
    //20位
    private String username;
    private Integer userID;
    private String password;
    private String mail;

    //在线状态
    private boolean onLine;
    public User(String username,String password){
        this.username=username;
        this.password=password;
    }
    public User(int userID){
        this.userID=userID;
    }
    public void setUserID(int userID){this.userID=userID;}
    public void setMail(String mail){this.mail=mail;}

    public void setUsername(String username){this.username=username;}
    public void setPassword(String password){this.password=password;}
    public void setOnLine(boolean Online){this.onLine=onLine;}
    public long getUserID(){return this.userID;}
    public String getMail(String mail){return this.mail=mail;}
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public boolean getOnLine(){return this.onLine;}
}
