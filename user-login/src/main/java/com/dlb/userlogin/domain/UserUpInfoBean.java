package com.dlb.userlogin.domain;

import java.io.Serializable;

/**
 * 用户上传信息bean
 */
public class UserUpInfoBean implements Serializable {

    private Integer user_id;
    private String file_name;
    private String data_type;
    private String file_format;
    private long file_size;
    private String create_time;



    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getFile_format() {
        return file_format;
    }

    public void setFile_format(String file_format) {
        this.file_format = file_format;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "UserUpInfoBean{" + "user_id=" + user_id + ", file_name='" + file_name + '\'' + ", data_type='" + data_type + '\'' + ", file_format='" + file_format + '\'' + ", file_size='" + file_size + '\'' + ", create_time='" + create_time + '\'' + '}';
    }
}
