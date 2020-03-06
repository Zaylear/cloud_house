package com.rbi.wx.wechatpay.util.chargereal.chargerealproperty;

import java.io.Serializable;

/**
 * 查询月数折扣
 * 查询房间的收费面积范围大小
 */
public class ChargeDetail implements Serializable{
    private double areaMax;//面积最大值
    private double areaMin;//面积最小值
    private String chargeCode;//项目编号
    private Integer datedif;//缴费月数
    private double discount;//折扣

    public double getAreaMax() {
        return areaMax;
    }

    public void setAreaMax(double areaMax) {
        this.areaMax = areaMax;
    }

    public double getAreaMin() {
        return areaMin;
    }

    public void setAreaMin(double areaMin) {
        this.areaMin = areaMin;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

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
}
