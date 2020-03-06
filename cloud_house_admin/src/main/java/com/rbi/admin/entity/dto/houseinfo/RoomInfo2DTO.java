package com.rbi.admin.entity.dto.houseinfo;

import lombok.Data;

import java.io.Serializable;
@Data
public class RoomInfo2DTO implements Serializable{
    private double roomSize;
    private String roomCode;
    private String unitName;
    private String buildingName;
    private String regionName;
    private String villageName;
    private String userId;
}
