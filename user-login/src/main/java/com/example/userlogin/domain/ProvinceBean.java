package com.example.userlogin.domain;

import java.io.Serializable;

/**
 * 电影topn  bean
 */
public class ProvinceBean implements Serializable{


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getCount() {
        return num;
    }

    public void setCount(long num) {
        this.num = num;
    }

    private String province;

    private long num;




}
