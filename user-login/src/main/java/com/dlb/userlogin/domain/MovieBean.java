package com.dlb.userlogin.domain;

import java.io.Serializable;

public class MovieBean implements Serializable {


    private String name;
    private long cid;
    private long num;


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


}
