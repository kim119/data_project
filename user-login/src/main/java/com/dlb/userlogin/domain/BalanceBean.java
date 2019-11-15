package com.dlb.userlogin.domain;

import com.alibaba.fastjson.annotation.JSONField;

public class BalanceBean {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    private long balance;
    @JSONField(serialize = false)
    private String address;
}
