package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

/**
 * 微信支付回调成功
 * 收费项目实体
 */
public class ChargeInfo implements Serializable {
    private String organizationId;
    private String chargeCode;
    private String chargeName;
    private String chargeStandard;
    private String datedif;
    private String discount;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String getDatedif() {
        return datedif;
    }

    public void setDatedif(String datedif) {
        this.datedif = datedif;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
