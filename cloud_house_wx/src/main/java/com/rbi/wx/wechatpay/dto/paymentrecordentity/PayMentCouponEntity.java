package com.rbi.wx.wechatpay.dto.paymentrecordentity;

import java.io.Serializable;

public class PayMentCouponEntity implements Serializable{
    private Double balanceAmount;
    private String couponName;
    private Integer id;

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
