package com.dlb.userlogin.domain;

import java.io.Serializable;

public class User implements Serializable {


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private Integer id;
    private String password;
    private String username;

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", password='" + password + '\'' + ", username='" + username + '\'' + '}';
    }
}