package com.rbi.wx.wechatpay.dto;

import java.io.Serializable;

public class UpdatePasswordEntity implements Serializable{
    private String salt;
    private String password;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
