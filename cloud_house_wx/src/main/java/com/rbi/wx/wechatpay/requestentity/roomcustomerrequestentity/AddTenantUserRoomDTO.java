package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import java.io.Serializable;

public class AddTenantUserRoomDTO implements Serializable{
    private String roomCode;
    private String startTime;
    private String endTime;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
