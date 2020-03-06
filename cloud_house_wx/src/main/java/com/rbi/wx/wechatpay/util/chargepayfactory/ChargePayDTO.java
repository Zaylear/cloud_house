package com.rbi.wx.wechatpay.util.chargepayfactory;

import java.io.Serializable;

public class ChargePayDTO implements Serializable{
    private String roomCode;
    private String datedif;
    private String couponMoney;
    private String money;       //余额
    private String threeWayFee;
    private String returnMoney; //退还的费用
    private String newMoney;
    private String oldMoney;
    private String StartTime;        //时间段
    private String EndTime;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getThreeWayFee() {
        return threeWayFee;
    }

    public void setThreeWayFee(String threeWayFee) {
        this.threeWayFee = threeWayFee;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getDatedif() {
        return datedif;
    }

    public void setDatedif(String datedif) {
        this.datedif = datedif;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNewMoney() {
        return newMoney;
    }

    public void setNewMoney(String newMoney) {
        this.newMoney = newMoney;
    }

    public String getOldMoney() {
        return oldMoney;
    }

    public void setOldMoney(String oldMoney) {
        this.oldMoney = oldMoney;
    }
}
