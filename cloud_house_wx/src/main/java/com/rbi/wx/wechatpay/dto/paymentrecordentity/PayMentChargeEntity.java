package com.rbi.wx.wechatpay.dto.paymentrecordentity;

import java.io.Serializable;

public class PayMentChargeEntity implements Serializable{
    //通过月数和项目ID获取下面的内容
    private String organizationName;//收费组织名称
    private String organizationId;//收费组织ID
    private double chargeStandard;//收费单价
    private String chargeUnit;//收费单位
    private double discount;//折扣
    private String chargeName;//项目名称
    private Integer datedif;//缴费月数
    private String chargeCode;//项目编号
    private String chargeType;//收费类型
    private Integer areaMax;//面积最大
    private Integer areaMin;//面积最小

    public Integer getAreaMax() {
        return areaMax;
    }

    public void setAreaMax(Integer areaMax) {
        this.areaMax = areaMax;
    }

    public Integer getAreaMin() {
        return areaMin;
    }

    public void setAreaMin(Integer areaMin) {
        this.areaMin = areaMin;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

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


    public Integer getDatedif() {
        return datedif;
    }

    public void setDatedif(Integer datedif) {
        this.datedif = datedif;
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
}
