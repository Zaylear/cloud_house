package com.rbi.wx.wechatpay.entity;

import javax.persistence.*;
import java.io.Serializable;


public class RoomAndCustomerDO implements Serializable {


    private Integer id;

    private String userId;


    private String roomCode;

    private Integer identity;

    private Integer pastDue;//过期状态

    private Integer loggedOffState;//注销状态

    private String  startTime;

    private String endTime;

    private String idt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Integer getPastDue() {
        return pastDue;
    }

    public void setPastDue(Integer pastDue) {
        this.pastDue = pastDue;
    }

    public Integer getLoggedOffState() {
        return loggedOffState;
    }

    public void setLoggedOffState(Integer loggedOffState) {
        this.loggedOffState = loggedOffState;
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

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }

    private String udt;
}
