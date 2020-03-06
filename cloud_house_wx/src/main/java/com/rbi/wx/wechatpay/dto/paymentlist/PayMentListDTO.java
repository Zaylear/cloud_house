package com.rbi.wx.wechatpay.dto.paymentlist;

import java.io.Serializable;

/**
 * 九宫格的列表实体
 */
public class PayMentListDTO implements Serializable{
    private Integer datedif;
    private double discount;
    private String chargeCode;
    private Integer chargeType;
    private double changeStandard;

    public Integer getDatedif() {
        return datedif;
    }

    public void setDatedif(Integer datedif) {
        this.datedif = datedif;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public double getChangeStandard() {
        return changeStandard;
    }

    public void setChangeStandard(double changeStandard) {
        this.changeStandard = changeStandard;
    }
}
