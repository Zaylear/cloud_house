package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

/**
 * 微信支付回调成功
 * 优惠券的实体
 */
public class CouponDTO implements Serializable {
    private String id;
    private String surplusDeductibleMoney;

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
