package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import java.io.Serializable;
import java.util.List;

public class UpdateTenantDTO implements Serializable{
    private String userId;
    private String userPhone;
    private String sex;
    private String userName;

    private List<AddTenantUserRoomDTO> roomCodes;
    private String idNumber;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<AddTenantUserRoomDTO> getRoomCodes() {
        return roomCodes;
    }

    public void setRoomCodes(List<AddTenantUserRoomDTO> roomCodes) {
        this.roomCodes = roomCodes;
    }
}
