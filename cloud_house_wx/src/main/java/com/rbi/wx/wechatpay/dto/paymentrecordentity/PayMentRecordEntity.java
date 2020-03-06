package com.rbi.wx.wechatpay.dto.paymentrecordentity;

import java.io.Serializable;

public class PayMentRecordEntity implements Serializable{
    private Double actualMoneyCollection;//实收金额
    private Double amountReceivable;//应收金额
    private String orderId;//订单ID
    private Double preferentialAmount;//优惠金额

    public Double getActualMoneyCollection() {
        return actualMoneyCollection;
    }

    public void setActualMoneyCollection(Double actualMoneyCollection) {
        this.actualMoneyCollection = actualMoneyCollection;
    }

    public Double getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(Double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPreferentialAmount() {
        return preferentialAmount;
    }

    public void setPreferentialAmount(Double preferentialAmount) {
        this.preferentialAmount = preferentialAmount;
    }
}
