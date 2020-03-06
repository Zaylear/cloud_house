package com.rbi.wx.wechatpay.util.chargereal.chargerealproperty;

import java.io.Serializable;

public class Charge implements Serializable{
    private String organizationName;//收费组织名称
    private String organizationId;//收费组织ID
    private double chargeStandard;//收费单价
    private String chargeUnit;//收费单位
    private double discount;//折扣
    private String chargeName;//项目名称
    private Integer datedif;//缴费月数
    private String chargeCode;//项目编号
    private String chargeType;//收费类型

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public double getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(double chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public String getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(String chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Integer getDatedif() {
        return datedif;
    }

    public void setDatedif(Integer datedif) {
        this.datedif = datedif;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }


}
