package com.rbi.admin.entity.dto;

import lombok.Data;


@Data
public class OwnerDTO {
    private String customerUserId;
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
    private String renovationStartTime;
    private String renovationDeadline;

    private String surname;//CUSTOMER
    private String idNumber;
    private Integer sex;
    private String mobilePhone;
    private Integer identity;//room_customer
    private String remarks;

    private String startTime;
    private String endTime;

    private Integer normalPaymentStatus;//正常缴费状态：1、正常 0、不正常 不正常说明可能存在空置费等问题
    private String startBillingTime;//开始计物业费时间
    private String realRecyclingHomeTime;//实际收房时间

    private Integer loggedOffState;//注销状态：1、已注销 0、未注销 指注销房间

    private String rentStatus;


}
