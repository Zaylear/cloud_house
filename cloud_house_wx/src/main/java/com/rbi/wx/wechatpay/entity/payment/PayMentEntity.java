package com.rbi.wx.wechatpay.entity.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 费用明细的实体
 */
@ApiModel(value = "费用明细的实体")
public class PayMentEntity implements Serializable{
    @ApiModelProperty(value = "项目编号",dataType = "String",example = "qwer123")
    private String chargeCode;
    @ApiModelProperty(value = "欠费状态",dataType = "String",example = "欠费")
    private String stateOfArrears;
    @ApiModelProperty(value = "项目名称",dataType = "String",example = "物业")
    private String ChargeName;
    @ApiModelProperty(value = "按钮颜色",dataType = "String",example = "Red")
    private String color;
    @ApiModelProperty(value = "是否可以跳转",dataType = "boolean",example = "true")
    private String status;
    @ApiModelProperty(value = "是否为物业费",dataType = "boolean",example = "true")
    private String type;
    @ApiModelProperty(value = "欠费月数",dataType = "boolean",example = "true")
    private String monthlyArrears;
    @ApiModelProperty(value = "欠费金额",dataType = "boolean",example = "true")
    private String amountOfArrears;
    @ApiModelProperty(value = "到期时间",dataType = "boolean",example = "true")
    private String dueTime;

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getMonthlyArrears() {
        return monthlyArrears;
    }

    public void setMonthlyArrears(String monthlyArrears) {
        this.monthlyArrears = monthlyArrears;
    }

    public String getAmountOfArrears() {
        return amountOfArrears;
    }

    public void setAmountOfArrears(String amountOfArrears) {
        this.amountOfArrears = amountOfArrears;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getStateOfArrears() {
        return stateOfArrears;
    }

    public void setStateOfArrears(String stateOfArrears) {
        this.stateOfArrears = stateOfArrears;
    }

    public String getChargeName() {
        return ChargeName;
    }

    public void setChargeName(String chargeName) {
        ChargeName = chargeName;
    }
}
