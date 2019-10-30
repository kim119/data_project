package com.dlb.userlogin.domain;

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
    private String dataType;
    //0.代表1个月  ,1代表 半年 ,2代表一年
    private int dataCondition;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getDataCondition() {
        return dataCondition;
    }

    public void setDataCondition(int dataCondition) {
        this.dataCondition = dataCondition;
    }



    @Override
    public String toString() {
        return "UserInfo{" + "user_id=" + user_id + ", username='" + username + '\'' + '}';
    }
}
