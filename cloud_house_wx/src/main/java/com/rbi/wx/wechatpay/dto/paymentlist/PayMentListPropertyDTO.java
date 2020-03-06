package com.rbi.wx.wechatpay.dto.paymentlist;

import java.io.Serializable;

/**
 * 物业费的九宫格列表
 */
public class PayMentListPropertyDTO extends PayMentListDTO implements Serializable{
    private double areaMin;
    private double areaMax;

    @Override
    public Integer getDatedif() {
        return super.getDatedif();
    }

    @Override
    public void setDatedif(Integer datedif) {
        super.setDatedif(datedif);
    }

    @Override
    public double getDiscount() {
        return super.getDiscount();
    }

    @Override
    public void setDiscount(double discount) {
        super.setDiscount(discount);
    }

    @Override
    public String getChargeCode() {
        return super.getChargeCode();
    }

    @Override
    public void setChargeCode(String chargeCode) {
        super.setChargeCode(chargeCode);
    }

    @Override
    public Integer getChargeType() {
        return super.getChargeType();
    }

    @Override
    public void setChargeType(Integer chargeType) {
        super.setChargeType(chargeType);
    }

    @Override
    public double getChangeStandard() {
        return super.getChangeStandard();
    }

    @Override
    public void setChangeStandard(double changeStandard) {
        super.setChangeStandard(changeStandard);
    }

    public double getAreaMin() {
        return areaMin;
    }

    public void setAreaMin(double areaMin) {
        this.areaMin = areaMin;
    }

    public double getAreaMax() {
        return areaMax;
    }

    public void setAreaMax(double areaMax) {
        this.areaMax = areaMax;
    }
}
