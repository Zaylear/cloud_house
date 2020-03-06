package com.rbi.interactive.entity.dto;

import java.io.Serializable;

public class RoomDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer id;

    private String villageCode;
    private String villageName;

    private String regionCode;
    private String regionName;
    private String buildingCode;
    private String buildingName;
    private String unitCode;
    private String unitName;

    private String roomCode;

    private Double roomSize;

    private Integer roomType;

    private Integer roomStatus;

    private String contractDeadline;

    private Integer renovationStatus;

    private String idt;

    private String udt;

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

    public Double getRoomSize() {
        return roomSize;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
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

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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

    @Override
    public String toString() {
        return "RoomDTO{" +
                "id=" + id +
                ", roomCode='" + roomCode + '\'' +
                ", roomSize=" + roomSize +
                ", roomType=" + roomType +
                ", roomStatus=" + roomStatus +
                ", contractDeadline='" + contractDeadline + '\'' +
                ", renovationStatus=" + renovationStatus +
                ", unitCode='" + unitCode + '\'' +
                ", idt='" + idt + '\'' +
                ", udt='" + udt + '\'' +
                '}';
    }
}
