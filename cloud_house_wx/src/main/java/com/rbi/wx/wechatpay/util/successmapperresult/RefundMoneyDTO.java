package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

public class RefundMoneyDTO implements Serializable {
    private String id;                      //
    private String surplusDeductibleMoney;  //剩余可抵扣金额
    private String deductibleMoney;         //可抵扣金额
    private String deductibledMoney;        //已抵扣金额
    private String deductionPropertyFee;     //抵扣物业费金额
    private String deductionRecord;         //抵扣记录

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

    public String getDeductionPropertyFee() {
        return deductionPropertyFee;
    }

    public void setDeductionPropertyFee(String deductionPropertyFee) {
        this.deductionPropertyFee = deductionPropertyFee;
    }

    public String getDeductionRecord() {
        return deductionRecord;
    }

    public void setDeductionRecord(String deductionRecord) {
        this.deductionRecord = deductionRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurplusDeductibleMoney() {
        return surplusDeductibleMoney;
    }

    public void setSurplusDeductibleMoney(String surplusDeductibleMoney) {
        this.surplusDeductibleMoney = surplusDeductibleMoney;
    }
}
