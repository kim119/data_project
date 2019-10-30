package com.dlb.userlogin.domain.record;

import java.io.Serializable;

public class Top10MovieRecord implements Serializable {



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    private String name;
    private long cid;
    private long num;
    private int user_id;
}
