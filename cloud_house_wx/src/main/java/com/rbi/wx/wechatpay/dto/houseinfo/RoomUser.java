package com.rbi.wx.wechatpay.dto.houseinfo;

import java.io.Serializable;

public class RoomUser implements Serializable{
    private String roomCode;
    private String userId;


    public String getRoomCode() {
        return roomCode;

    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
