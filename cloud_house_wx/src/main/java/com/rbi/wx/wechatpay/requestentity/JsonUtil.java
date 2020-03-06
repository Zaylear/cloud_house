package com.rbi.wx.wechatpay.requestentity;

import java.io.Serializable;

public class JsonUtil implements Serializable{
    private String code;
    private String msg;

    public JsonUtil(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
