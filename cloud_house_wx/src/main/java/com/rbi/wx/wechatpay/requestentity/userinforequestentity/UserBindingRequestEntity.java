package com.rbi.wx.wechatpay.requestentity.userinforequestentity;

import com.rbi.wx.wechatpay.entity.UserBindingEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "用户绑定的实体")
public class UserBindingRequestEntity implements Serializable{
    @ApiModelProperty(value = "微信的openid",dataType = "String",example = "o_Jhq1DzwpVxtih4nhwisriXFXhs")
    private String openId;
    @ApiModelProperty(value = "用户绑定时间",dataType = "String",example = "2019-4-13")
    private UserBindingEntity data;

    public UserBindingEntity getData() {
        return data;
    }

    public void setData(UserBindingEntity data) {
        this.data = data;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
