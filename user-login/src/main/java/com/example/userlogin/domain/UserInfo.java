package com.example.userlogin.domain;

import java.io.Serializable;

/**
 * 返回用户信息bean
 */
public class UserInfo implements Serializable {


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int  user_id;
    private String username;

    @Override
    public String toString() {
        return "UserInfo{" + "user_id=" + user_id + ", username='" + username + '\'' + '}';
    }
}
