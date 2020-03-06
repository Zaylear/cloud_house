package com.rbi.wx.wechatpay.util.chargereal.chargerealproperty;

import java.io.Serializable;

/**
 * 接受房间参数
 */
public class RoomPerproty implements Serializable{
    private double roomSize;
    private String roomCode;
    private Integer roomType;

    public double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(double roomSize) {
        this.roomSize = roomSize;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }
}
