package com.rbi.admin.entity.dto;

import lombok.Data;

@Data
public class RoomInfoDTO {
    private String villageName;
    private String regionName;
    private String buildingName;
    private String unitName;
    private String roomCode;
    private Integer roomType;
    private Double roomSize;
    private Integer roomStatus;
    private Integer renovationStatus;
    private String renovationStartTime;
    private String renovationDeadline;

    private Integer floor;
    private String floorCode;

    private String startBillingTime;
    private String realRecyclingHomeTime;

    private String rentStatus;

}
