package com.rbi.admin.entity.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class RoomUserDTO implements Serializable{
    private String roomCode;
    private String roomSize;
    private String userId;
    private String username;
    private String unitName;
    private String buildingName;
    private String regionName;
    private String villageName;
    private String districtName;
}
