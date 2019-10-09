package com.example.userlogin.domain;

import java.io.Serializable;

/**
 * 返回用户信息bean
 */
public class UserInfo implements Serializable {


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;



}
