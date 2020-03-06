package com.rbi.admin.entity.edo;


import lombok.Data;

@Data
public class RoomTreeDTO {
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
    private String roomCode;
}
