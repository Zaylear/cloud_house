package com.rbi.wx.wechatpay.dto.paymentrecordentity;

import java.io.Serializable;

public class PayMentPayerEntity implements Serializable{
    //通过userId 获取下面的内容
    private String userName;//用户名称
    private String userPhone;//用户电话
    private String userId;//用户Id
    private String surname;//姓

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
