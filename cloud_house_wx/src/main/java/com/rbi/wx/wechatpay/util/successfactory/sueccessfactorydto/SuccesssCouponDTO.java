package com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto;

import java.io.Serializable;

public class SuccesssCouponDTO implements Serializable {
    private String amountDeductedThisTime;  //本次抵扣金额
    private String deductibleMoney;         //可抵扣金额
    private String id;                      //
    private String deductibledMoney;        //已抵扣金额
    private String surplusDeductibleMoney;  //剩余可抵扣金额
    private String deductionRecord;         //抵扣记录
    private String propertyFee;             //抵扣物业费金额

    public String getAmountDeductedThisTime() {
        return amountDeductedThisTime;
    }

    public void setAmountDeductedThisTime(String amountDeductedThisTime) {
        this.amountDeductedThisTime = amountDeductedThisTime;
    }

    public String getDeductibleMoney() {
        return deductibleMoney;
    }

    public void setDeductibleMoney(String deductibleMoney) {
        this.deductibleMoney = deductibleMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeductibledMoney() {
        return deductibledMoney;
    }

    public void setDeductibledMoney(String deductibledMoney) {
        this.deductibledMoney = deductibledMoney;
    }

    public String getSurplusDeductibleMoney() {
        return surplusDeductibleMoney;
    }

    public void setSurplusDeductibleMoney(String surplusDeductibleMoney) {
        this.surplusDeductibleMoney = surplusDeductibleMoney;
    }

    public String getDeductionRecord() {
        return deductionRecord;
    }

    public void setDeductionRecord(String deductionRecord) {
        this.deductionRecord = deductionRecord;
    }

    public String getPropertyFee() {
        return propertyFee;
    }

    public void setPropertyFee(String propertyFee) {
        this.propertyFee = propertyFee;
    }
}
