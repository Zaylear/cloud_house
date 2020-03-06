package com.rbi.wx.wechatpay.dto.indexroom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 缴费人明细 历史账单实体
 */
@ApiModel(value = "缴费人历史账单")
public class PayerPayListDTO implements Serializable{
    @ApiModelProperty(value = "缴费日期",dataType = "String",example = "2019-9-11")
    private String date;
    @ApiModelProperty(value = "缴费项目名称",dataType = "String",example = "物业费")
    private String chargeName;
    @ApiModelProperty(value = "缴费金额",dataType = "String",example = "100.45")
    private double money;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
