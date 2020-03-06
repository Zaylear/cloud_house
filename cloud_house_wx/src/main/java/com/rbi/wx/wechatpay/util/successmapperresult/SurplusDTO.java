package com.rbi.wx.wechatpay.util.successmapperresult;

import java.io.Serializable;

/**
 * 余额实体
 */
public class SurplusDTO implements Serializable {
    private String id;
    private String surplus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }
}
