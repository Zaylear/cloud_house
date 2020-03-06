package com.rbi.interactive.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChargeItemCostDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    private String chargeCode;
    private String chargeName;
    private String chargeType;
    private String chargeUnit;
    private Double chargeStandard;
    private List<Double> chargeStandards;
    private Integer displayModeStatus;  //0、显示数值 1、下拉框
    private Integer datedif;
    private Double discount;
    private String parkingSpacePlace; //车位地点 1、地面 2、地下
    private String parkingSpaceType;

    private Double amountReceivable;
    private Double actualMoneyCollection;
    private Double preferentialAmount;
    //水电费
    private Double usageAmount;
    private Double currentReadings;
    private Double lastReading;

    private String startTime;
    private String dueTime;

}
