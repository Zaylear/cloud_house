package com.rbi.interactive.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChargeItemBackDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String chargeCode;
    private String chargeName;
    private Integer datedif;
    private String chargeType;
    private Integer chargeWay;
    String chargeStandards;
    Double chargeStandard;
    String parkingSpaceCode;

}
