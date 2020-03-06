package com.rbi.wx.wechatpay.util.chargepayfactory.chargedAllDTO;

import java.io.Serializable;

public class ChargeElectricityDTO implements Serializable{
    private String roomCode;      //房间编号
    private Double currentReadings; //当前读数
    private Double lastReading;        //上次读数
    private Integer id;                //唯一标识
    private Double chargeStandard;     //收费单价

    public Double getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(Double chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Double getCurrentReadings() {
        return currentReadings;
    }

    public void setCurrentReadings(Double currentReadings) {
        this.currentReadings = currentReadings;
    }

    public Double getLastReading() {
        return lastReading;
    }

    public void setLastReading(Double lastReading) {
        this.lastReading = lastReading;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
