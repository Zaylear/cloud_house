package com.rbi.wx.wechatpay.requestentity.entity;

import java.io.Serializable;

public class MoneyMonth implements Serializable{
    private Double oldMoney;
    private Double nowMoney;
    private Double discount;

    public MoneyMonth(Double oldMoney, Double nowMoney, Double discount) {
        this.oldMoney = oldMoney;
        this.nowMoney = nowMoney;
        this.discount = discount;
    }

    public Double getOldMoney() {
        return oldMoney;

    }

    public void setOldMoney(Double oldMoney) {
        this.oldMoney = oldMoney;
    }

    public Double getNowMoney() {
        return nowMoney;
    }

    public void setNowMoney(Double nowMoney) {
        this.nowMoney = nowMoney;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
