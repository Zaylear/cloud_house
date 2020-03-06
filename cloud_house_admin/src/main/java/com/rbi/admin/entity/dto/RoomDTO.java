package com.rbi.admin.entity.dto;


import lombok.Data;

@Data
public class RoomDTO{
    private String organizationId;
    private String organizationName;
    private String villageCode;
    private String villageName;
    private String regionCode;
    private String regionName;
    private String buildingCode;
    private String buildingName;
    private String unitCode;
    private String unitName;
    private String level;
    private String roomCode;
    private Double roomSize;
    private Integer roomType;
    private Integer roomStatus;
    private Integer renovationStatus;
    private String renovationStartTime;
    private String renovationDeadline;
    private String deliveryDate;
    private String contractDeadline;
    private String floorCode;
    private Integer floor;
    private String startBillingTime;
    private String realRecyclingHomeTime;
    private String idt;
    private String udt;
}
