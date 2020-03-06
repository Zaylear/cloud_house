package com.rbi.wx.wechatpay.dto.indexroom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 房屋页面缴费详情用户实体
 */
@ApiModel(value = "房屋页面缴费详情用户实体")
public class PayMentUserDTO implements Serializable{
    @ApiModelProperty(value = "用户ID编号",dataType = "String",example = "155953281305358")
    private String userId;
    @ApiModelProperty(value = "缴费日期",dataType = "String",example = "2019-4-3")
    private String date;
    @ApiModelProperty(value = "缴费项目",dataType = "String",example = "物业费")
    private String chargeName;
    @ApiModelProperty(value = "缴费人姓名",dataType = "String",example = "张先生")
    private String userName;
    @ApiModelProperty(value = "缴费金额",dataType = "String",example = "100.44")
    private double money;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
