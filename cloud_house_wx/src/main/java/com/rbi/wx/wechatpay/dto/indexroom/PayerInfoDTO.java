package com.rbi.wx.wechatpay.dto.indexroom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 缴费人明细
 * 缴费人的用户信息实体
 */
@ApiModel(value = "缴费人的信息")
public class PayerInfoDTO implements Serializable{
    @ApiModelProperty(value = "用户编号",dataType = "String",example = "155953281305358")
    private String userId;
    @ApiModelProperty(value = "用户名称",dataType = "String",example = "张先生")
    private String userName;
    @ApiModelProperty(value = "用户手机号",dataType = "String",example = "18300851462")
    private String mobilePhone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
