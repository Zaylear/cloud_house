package com.rbi.wx.wechatpay.dto.paymentlist;

import com.rbi.wx.wechatpay.dto.indexroom.PayerPayListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "缴费人的用户信息")
public class PayerInfoDTO implements Serializable{
    @ApiModelProperty(value = "用户手机号",dataType = "String",example = "18300851461")
    private String mobilePhone;
    @ApiModelProperty(value = "用户称呼",dataType = "String",example = "张先生")
    private String surName;
    @ApiModelProperty(value = "缴费账单",dataType = "String",example = "2019-9-11")
    private List<PayerPayListDTO> payList;

    public List<PayerPayListDTO> getPayList() {
        return payList;
    }

    public void setPayList(List<PayerPayListDTO> payList) {
        this.payList = payList;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

}
