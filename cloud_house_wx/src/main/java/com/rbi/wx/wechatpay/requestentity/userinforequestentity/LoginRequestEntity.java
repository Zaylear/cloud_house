package com.rbi.wx.wechatpay.requestentity.userinforequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "用户登录权限的实体")
public class LoginRequestEntity implements Serializable{
    @ApiModelProperty(value = "微信的openid",dataType = "String",example = "o_Jhq1DzwpVxtih4nhwisriXFXhs")
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
