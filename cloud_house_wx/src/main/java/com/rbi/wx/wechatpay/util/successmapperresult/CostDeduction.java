package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

public class CostDeduction implements Serializable {
    private String amountDeductedThisTime;  //本次抵扣金额
    private String deductibleMoney;         //可抵扣金额
    private String deductibledMoney;        //已抵扣金额
    private String deductionCode;           //抵扣编号
    private String deductionMethod;         //抵扣方式
    private String surplusDeductibleMoney;  //剩余可抵扣金额
    private String deductionOrderId;        //抵扣订单Id
    private String chargeType;              //收费类型
    private String discount;                //折扣
    private String deductionItem;           //抵扣项目

    public String getDeductionItem() {
        return deductionItem;
    }

    public void setDeductionItem(String deductionItem) {
        this.deductionItem = deductionItem;
    }

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

    public String getDeductibledMoney() {
        return deductibledMoney;
    }

    public void setDeductibledMoney(String deductibledMoney) {
        this.deductibledMoney = deductibledMoney;
    }

    public String getDeductionCode() {
        return deductionCode;
    }

    public void setDeductionCode(String deductionCode) {
        this.deductionCode = deductionCode;
    }

    public String getDeductionMethod() {
        return deductionMethod;
    }

    public void setDeductionMethod(String deductionMethod) {
        this.deductionMethod = deductionMethod;
    }

    public String getSurplusDeductibleMoney() {
        return surplusDeductibleMoney;
    }

    public void setSurplusDeductibleMoney(String surplusDeductibleMoney) {
        this.surplusDeductibleMoney = surplusDeductibleMoney;
    }

    public String getDeductionOrderId() {
        return deductionOrderId;
    }

    public void setDeductionOrderId(String deductionOrderId) {
        this.deductionOrderId = deductionOrderId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
