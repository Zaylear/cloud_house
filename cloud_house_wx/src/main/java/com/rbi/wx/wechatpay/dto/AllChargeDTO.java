package com.rbi.wx.wechatpay.dto;

import java.io.Serializable;

public class AllChargeDTO implements Serializable{
    private Double actualMoneyCollection;
    private Double amountReceivable;
    private String chargeCode;
    private String chargeName;
    private Double chargeStandard;
    private String chargeType;
    private String chargeUnit;
    private Integer datedif;
    private Double preferentialAmount;

    public Double getActualMoneyCollection() {
        return actualMoneyCollection;
    }

    public void setActualMoneyCollection(Double actualMoneyCollection) {
        this.actualMoneyCollection = actualMoneyCollection;
    }

    public Double getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(Double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Double getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(Double chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(String chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public Integer getDatedif() {
        return datedif;
    }

    public void setDatedif(Integer datedif) {
        this.datedif = datedif;
    }

    public Double getPreferentialAmount() {
        return preferentialAmount;
    }

    public void setPreferentialAmount(Double preferentialAmount) {
        this.preferentialAmount = preferentialAmount;
    }
}
