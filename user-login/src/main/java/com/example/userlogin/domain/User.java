package com.example.userlogin.domain;

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

    public void setPassword(String passsword) {
        this.password = passsword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", password='" + password + '\'' + ", username='" + username + '\'' + ", datatype='" + datatype + '\'' + ", secretkey='" + secretkey + '\'' + '}';
    }

    private Integer id;
    private String password;
    private String username;
    private String datatype;
    private String secretkey;


}