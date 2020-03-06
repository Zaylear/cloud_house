package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import java.io.Serializable;
import java.util.List;

public class AddTenantUserDTO implements Serializable{
    private String userName;
    private String sex;
    private String userPhone;
    private String idNumber;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    private List<AddTenantUserRoomDTO> roomCodes;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<AddTenantUserRoomDTO> getRoomCodes() {
        return roomCodes;
    }

    public void setRoomCodes(List<AddTenantUserRoomDTO> roomCodes) {
        this.roomCodes = roomCodes;
    }
}
