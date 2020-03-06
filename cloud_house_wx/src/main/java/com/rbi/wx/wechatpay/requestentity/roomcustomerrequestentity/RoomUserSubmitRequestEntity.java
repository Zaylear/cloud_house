package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class RoomUserSubmitRequestEntity implements Serializable {
    private String userName;
    private String userPhone;
    private String sex;
    private String userId;
    private String verificationCode;
    private List<UpdateRoomCustomerRequestEntity> roomCodes;
    private String identity;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public List<UpdateRoomCustomerRequestEntity> getRoomCodes() {
        return roomCodes;
    }

    public void setRoomCodes(List<UpdateRoomCustomerRequestEntity> roomCodes) {
        this.roomCodes = roomCodes;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
