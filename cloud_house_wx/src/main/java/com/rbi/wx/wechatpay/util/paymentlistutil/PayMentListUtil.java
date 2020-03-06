package com.rbi.wx.wechatpay.util.paymentlistutil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel("九宫格实体")
public class PayMentListUtil implements Serializable{
    @ApiModelProperty(value = "缴费月数",dataType = "Integer",example = "1")
    private Integer datedif;
    @ApiModelProperty(value = "享受折扣前",dataType = "Double",example = "12.00")
    private double oldMoney;
    @ApiModelProperty(value = "享受折扣后",dataType = "Double",example = "10.00")
    private double newMoney;
    @ApiModelProperty(value = "享受的折扣",dataType = "Double",example = "9.6")
    private double discount;

    public PayMentListUtil(Integer datedif, double oldMoney, double newMoney, double discount) {
        this.datedif = datedif;
        this.oldMoney = oldMoney;
        this.newMoney = newMoney;
        this.discount = discount;
    }

    public Integer getDatedif() {
        return datedif;
    }

    public void setDatedif(Integer datedif) {
        this.datedif = datedif;
    }

    public double getOldMoney() {
        return oldMoney;
    }

    public void setOldMoney(double oldMoney) {
        this.oldMoney = oldMoney;
    }

    public double getNewMoney() {
        return newMoney;
    }

    public void setNewMoney(double newMoney) {
        this.newMoney = newMoney;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
