package com.rbi.interactive.entity.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class RefundAndApplicationDTO implements Serializable {

    public final static long serialVersionUID = 1L;

    private Integer id;

    private String orderId;

    private Double refundableAmount;

    private Double actualMoneyCollection;

    private Double mortgageAmount;

    private Double deductionPropertyFeeSurplus;

    private Double transferCardAmount;

    private Double deductionPropertyFee;

    private Integer auditStatus;

    private String organizationId;

    private String remark;

    private String idt;

    private String udt;

//    private String orderId;

    private String tollCollectorId;

    private String payerPhone;

    private String payerName;

    private String paymentType;

    private String paymentMethod;

    private Integer refundStatus;

    private Integer invalidState;

    private String villageCode;

    private String villageName;

    private String regionCode;

    private String regionName;

    private String buildingCode;

    private String buildingName;

    private String unitCode;

    private String unitName;

    private String customerUserId;

    private String surname;

    private String mobilePhone;

    private String roomCode;

    private Double roomSize;

//    private String organizationId;

    private String organizationName;

    private String chargeCode;

    private String chargeName;

//    private Double actualMoneyCollection;
//
//    private Double mortgageAmount;

    private String reasonForDeduction;

//    private Double refundableAmount;

    private String chargeUnit;

    private String startTime;

    private String dueTime;

    private Integer delayTime;

    private String delayReason;

    private String personLiable;

    private String personLiablePhone;

    private String responsibleAgencies;

    private Double deductibleMoney;

    private Double deductibledMoney;

    private Double surplusDeductibleMoney;

    private Double amountDeductedThisTime;

    private String deductionRecord;
}
