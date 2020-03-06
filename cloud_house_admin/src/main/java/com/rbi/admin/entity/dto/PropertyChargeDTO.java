package com.rbi.admin.entity.dto;

import lombok.Data;


@Data
public class PropertyChargeDTO {
    private String organizationId;
    private String organizationName;
    private String chargeCode;
    private String chargeName;
    private String chargeUnit;
    private Double chargeStandard;
    private Double discount;
    private Integer chargeWay;
}
