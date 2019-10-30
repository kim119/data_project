package com.dlb.userlogin.domain.record;

import java.io.Serializable;

public class ProvinceDataRecord implements Serializable {


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    private String province;
    private long num;
    private int user_id;
}
