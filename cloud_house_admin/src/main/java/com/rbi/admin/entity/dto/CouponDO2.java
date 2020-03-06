package com.rbi.admin.entity.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CouponDO2 {
    private Integer id;
    private String couponCode;
    private String couponType;
    private String couponName;
    private String organizationId;
    private String distributor;
    private Double money;
    private Integer enable;
    private String effectiveTime;
    private String chargeCode;
    private String idt;
    private String udt;
}
