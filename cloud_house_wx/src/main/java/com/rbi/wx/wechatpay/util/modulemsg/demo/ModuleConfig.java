package com.rbi.wx.wechatpay.util.modulemsg.demo;

import com.rbi.wx.wechatpay.util.Constants;

public class ModuleConfig implements com.rbi.wx.wechatpay.util.modulemsg.ModuleConfig {
    @Override
    public String getAppid() {
        return Constants.APPID;
    }

    @Override
    public String getSecret() {
        return Constants.APPSECRET;
    }

}
