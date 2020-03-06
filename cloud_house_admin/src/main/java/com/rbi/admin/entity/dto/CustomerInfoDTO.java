package com.rbi.admin.entity.dto;

import lombok.Data;

@Data
public class CustomerInfoDTO {
    private String customerUserId;
    private String surname;
    private String idNumber;
    private Integer sex;
    private String mobilePhone;
    private Integer identity;
    private String remarks;
    private String startTime;
    private String endTime;
    private Integer normalPaymentStatus;
    private String startBillingTime;
    private String realRecyclingHomeTime;
    private Integer loggedOffState;
}
