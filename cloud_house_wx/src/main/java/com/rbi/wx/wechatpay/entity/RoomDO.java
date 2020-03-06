package com.rbi.wx.wechatpay.entity;


import javax.persistence.*;
import java.io.Serializable;


public class RoomDO implements Serializable {



    private Integer id;


    private String roomCode;


    private String unitCode;


    private Double roomSize;

    private Integer room_type;


    private Integer roomStatus;


    private String contractDeadline;

    private Integer renovationStatus;

    private String userId;

    private String ownerName;

    private String ownerSex;


    private String mobilePhone;

    private String idt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public Integer getRoom_type() {
        return room_type;
    }

    public void setRoom_type(Integer room_type) {
        this.room_type = room_type;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getContractDeadline() {
        return contractDeadline;
    }

    public void setContractDeadline(String contractDeadline) {
        this.contractDeadline = contractDeadline;
    }

    public Integer getRenovationStatus() {
        return renovationStatus;
    }

    public void setRenovationStatus(Integer renovationStatus) {
        this.renovationStatus = renovationStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSex() {
        return ownerSex;
    }

    public void setOwnerSex(String ownerSex) {
        this.ownerSex = ownerSex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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
