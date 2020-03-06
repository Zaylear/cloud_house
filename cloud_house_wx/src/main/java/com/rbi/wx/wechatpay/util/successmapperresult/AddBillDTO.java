package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

public class AddBillDTO implements Serializable {
    private String actualTotalMoneyCollection;
    private String amountTotalReceivable;
    private String orderId;

    public String getActualTotalMoneyCollection() {
        return actualTotalMoneyCollection;
    }

    public void setActualTotalMoneyCollection(String actualTotalMoneyCollection) {
        this.actualTotalMoneyCollection = actualTotalMoneyCollection;
    }

    public String getAmountTotalReceivable() {
        return amountTotalReceivable;
    }

    public void setAmountTotalReceivable(String amountTotalReceivable) {
        this.amountTotalReceivable = amountTotalReceivable;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
