package com.rbi.wx.wechatpay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 我的缴费明细实体
 */
@ApiModel(value = "缴费明细返回实体")
public class PayMentRecordsDTO implements Serializable{
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;
    @ApiModelProperty(value = "缴费时间",dataType = "String",example = "2019-6-11")
    private String date;
    @ApiModelProperty(value = "所缴费项目名称",dataType = "String",example = "物业费")
    private String chargeName;
    @ApiModelProperty(value = "缴费人",dataType = "String",example = "张先生")
    private String userName;
    @ApiModelProperty(value = "缴费金额",dataType = "double",example = "23.43")
    private double money;
    @ApiModelProperty(value = "缴费人ID",dataType = "String",example = "张先生")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
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
