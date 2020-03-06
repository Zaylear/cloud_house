package com.rbi.wx.wechatpay.dto.parkingrate;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomCode;
    private String organizationId;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
