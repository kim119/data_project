package com.dlb.userlogin.domain;

import java.io.Serializable;

/**
 * 视屏类型
 */
public class MovieTypeBean implements Serializable {

    private String name;
    private long num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }






}
