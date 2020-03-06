package com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto;

import java.io.Serializable;

public class SuccessRoomDTO implements Serializable{
    private String id;
    private Double surplus;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getSurplus() {
        return surplus;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }
}
