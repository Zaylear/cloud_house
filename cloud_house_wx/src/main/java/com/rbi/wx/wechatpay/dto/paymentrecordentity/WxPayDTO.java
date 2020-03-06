package com.rbi.wx.wechatpay.dto.paymentrecordentity;

import com.rbi.wx.wechatpay.util.Constants;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
@ApiModel(value = "微信返回参数")
public class WxPayDTO implements Serializable{
    private String appId= Constants.APPID;
    private String nonceStr;
    private String packagedto;
    private String paySign;
    private String signType;
    private String timeStamp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackagedto() {
        return packagedto;
    }

    public void setPackagedto(String packagedto) {
        this.packagedto = packagedto;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
