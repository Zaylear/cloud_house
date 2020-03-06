package com.rbi.wx.wechatpay.util.successmapperresult;

/**
 * 三通费的实体
 */
public class ThreeWayDTO {
    private String billDetailedId;              //ID
    private String surplusDeductibleMoney;      //剩余可抵扣金额
    private String deductibledMoney;            //已经抵扣金额
    private String deductibleMoney;             //可抵扣金额
    private String discount;                    //折扣
    private String deductionRecord;             //抵扣记录
    private String chargeType;                  //收费类型
    private String chargeName;                  //收费名称
    public String getBillDetailedId() {
        return billDetailedId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public void setBillDetailedId(String billDetailedId) {
        this.billDetailedId = billDetailedId;
    }

    public String getSurplusDeductibleMoney() {
        return surplusDeductibleMoney;
    }

    public void setSurplusDeductibleMoney(String surplusDeductibleMoney) {
        this.surplusDeductibleMoney = surplusDeductibleMoney;
    }

    public String getDeductibledMoney() {
        return deductibledMoney;
    }

    public void setDeductibledMoney(String deductibledMoney) {
        this.deductibledMoney = deductibledMoney;
    }

    public String getDeductibleMoney() {
        return deductibleMoney;
    }

    public void setDeductibleMoney(String deductibleMoney) {
        this.deductibleMoney = deductibleMoney;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDeductionRecord() {
        return deductionRecord;
    }

    public void setDeductionRecord(String deductionRecord) {
        this.deductionRecord = deductionRecord;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }
}
