package com.rbi.wx.wechatpay.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "用户绑定信息实体")
public class UserBindingEntity implements Serializable{
    @ApiModelProperty(value = "用户输入的姓名",dataType = "String",example = "o_Jhq1DzwpVxtih4nhwisriXFXhs")
    private String surName;
    @ApiModelProperty(value = "用户输入的手机号",dataType = "String",example = "18300851462")
    private String phone;
//    @ApiModelProperty(value = "用户输入的密码",dataType = "String",example = "123457678")
//    private String password;
    @ApiModelProperty(value = "验证码",dataType = "String",example = "123457678")
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


}
