package com.dlb.userlogin.domain.dlb;

import com.alibaba.fastjson.annotation.JSONField;

public class DlB_User {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JSONField(serialize = false)
    private int id;
    private String username;
    @JSONField(serialize = false)
    private String password;
    private String token;
}
