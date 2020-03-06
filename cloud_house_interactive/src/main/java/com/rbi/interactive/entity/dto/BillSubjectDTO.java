package com.rbi.interactive.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BillSubjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String chargeName;
    private String  chargeStandard;
    private Double usageAmount;
    private Integer datedif;
    private Double discount;
    private String billingPeriod;  //计费期间
    private String payableParty; //应缴人
    private String stateOfArrears; //是否欠费
    private Double amountReceivable;
    private Double actualMoneyCollection;
}
