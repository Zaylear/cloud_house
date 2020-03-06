package com.rbi.wx.wechatpay.entity;

import java.io.Serializable;

public class ChargeMaxTimeDTO implements Serializable{
    private String chargeStr;
    private String endTime;

    public String getChargeStr() {
        return chargeStr;
    }

    public void setChargeStr(String chargeStr) {
        this.chargeStr = chargeStr;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
