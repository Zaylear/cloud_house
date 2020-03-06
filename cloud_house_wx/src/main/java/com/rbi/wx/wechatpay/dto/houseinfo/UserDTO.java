package com.rbi.wx.wechatpay.dto.houseinfo;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "用户信息实体")
public class UserDTO implements Serializable{
    @ApiModelProperty(value = "用户Id",dataType = "String",example = "155953281305358")
    private String userId;
    @ApiModelProperty(value = "用户手机号码",dataType = "String",example = "132434555321")
    private String mobilePhone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ApiModelProperty(value = "用户姓名",dataType = "String",example = "张先生")
    private String userName;
}
