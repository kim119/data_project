package com.dlb.userlogin.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class User implements Serializable {


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }



    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getIsSet() {
        return isSet;
    }

    public void setIsSet(int isSet) {
        this.isSet = isSet;
    }


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getPay_password() {
        return pay_password;
    }
    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    private Integer user_id;
    @JSONField(serialize = false)
    private String password;
  /*  @JSONField(serialize = false)
    private String newPassword;*/
    private String username;
    private String address;
    @JSONField(serialize = false)
    private String privateKey;
    private String publicKey;
    private long balance;
    private int isSet;
    private String pay_password;
    private String create_time;
    private String newPassword;


    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", balance=" + balance +
                ", isSet=" + isSet +
                ", pay_password='" + pay_password + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}