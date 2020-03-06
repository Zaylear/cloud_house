package com.rbi.wx.wechatpay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "被关联的用户信息")
public class User implements Serializable{
    @ApiModelProperty(value = "姓名",dataType = "String",example = "香瓜")
    private String realName;    //姓名
    @ApiModelProperty(value = "手机号",dataType = "String",example = "13241245235423")
    private String mobilePhone; //手机号
    @ApiModelProperty(value = "性别",dataType = "String",example = "男")
    private String sex;         //性别
    @ApiModelProperty(value = "userId",dataType = "String",example = "男")
    private String userId;

    @ApiModelProperty(value = "身份证",dataType = "String",example = "男")
    private String idNumber;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
