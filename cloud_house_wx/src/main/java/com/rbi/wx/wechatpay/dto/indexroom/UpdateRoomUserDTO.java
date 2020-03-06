package com.rbi.wx.wechatpay.dto.indexroom;

import java.io.Serializable;

public class UpdateRoomUserDTO implements Serializable{
    private String roomCode;
    private String startDate;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
