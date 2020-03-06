package com.rbi.interactive.entity.dto;

import java.io.Serializable;

public class RoomCodeAndMobilePhoneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roomCode;
    private String mobilePhone;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
