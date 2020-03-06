package com.rbi.wx.wechatpay.dto.roomcusomer;

import java.io.Serializable;

public class RoomTimeDTO implements Serializable {
    private String realRecyclingHomeTime;
    private String startBillingTime;

    public String getRealRecyclingHomeTime() {
        return realRecyclingHomeTime;
    }

    public void setRealRecyclingHomeTime(String realRecyclingHomeTime) {
        this.realRecyclingHomeTime = realRecyclingHomeTime;
    }

    public String getStartBillingTime() {
        return startBillingTime;
    }

    public void setStartBillingTime(String startBillingTime) {
        this.startBillingTime = startBillingTime;
    }
}
