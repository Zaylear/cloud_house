package com.rbi.wx.wechatpay.dto.roomcusomer;

import java.io.Serializable;

public class GetRoomCodesDTO implements Serializable {
    private String roomCode;
    private String organizationId;
    private String organizationName;

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
