package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

/**
 * 微信支付回调成功
 * 折扣实体
 */
public class BillDetailedDTO implements Serializable {
    private String amountDeductedThisTime;  //本次抵扣金额
    private String chargeCode;
    private String chargeName;
    private String chargeStandard;
    private String chargeType;
    private String chargeUnit="元/平方米/月";
    private String datedif;
    private String deductibleMoney; //可抵扣金额
    private String deductibledMoney; //已抵扣金额
    private String discount;
    private String surplusDeductibleMoney; //剩余可抵扣金额
    private String startTime;
    private String dueTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public BillDetailedDTO(ChargeInfo chargeInfo){
        this.chargeCode=chargeInfo.getChargeCode();
        this.chargeName=chargeInfo.getChargeName();
        this.chargeStandard=chargeInfo.getChargeStandard();
        this.chargeType="1";
        this.datedif=chargeInfo.getDatedif();
        this.discount=chargeInfo.getDiscount();
    }
    public String getAmountDeductedThisTime() {
        return amountDeductedThisTime;
    }

    public void setAmountDeductedThisTime(String amountDeductedThisTime) {
        this.amountDeductedThisTime = amountDeductedThisTime;
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

    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
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

    public String getDatedif() {
        return datedif;
    }

    public void setDatedif(String datedif) {
        this.datedif = datedif;
    }

    public String getDeductibleMoney() {
        return deductibleMoney;
    }

    public void setDeductibleMoney(String deductibleMoney) {
        this.deductibleMoney = deductibleMoney;
    }

    public String getDeductibledMoney() {
        return deductibledMoney;
    }

    public void setDeductibledMoney(String deductibledMoney) {
        this.deductibledMoney = deductibledMoney;
    }


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


    public String getSurplusDeductibleMoney() {
        return surplusDeductibleMoney;
    }

    public void setSurplusDeductibleMoney(String surplusDeductibleMoney) {
        this.surplusDeductibleMoney = surplusDeductibleMoney;
    }
}
