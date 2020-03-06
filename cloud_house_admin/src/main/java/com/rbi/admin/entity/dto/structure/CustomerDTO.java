package com.rbi.admin.entity.dto.structure;

import lombok.Data;

@Data
public class CustomerDTO {
    private String organizationId;
    private String organizationName;
    private String userId;

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
    private Integer renovationStatus;

    private String deliveryDate;//
    private String contractDeadline;//

    private String renovationStartTime;
    private String renovationDeadline;

    private String surname;
    private Integer sex;
    private String mobilePhone;
    private String remarks;


}
