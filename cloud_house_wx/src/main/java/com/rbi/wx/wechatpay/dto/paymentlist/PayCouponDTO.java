package com.rbi.wx.wechatpay.dto.paymentlist;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "优惠券的实体")
public class PayCouponDTO implements Serializable{
    @ApiModelProperty(value = "id",dataType = "Integer",example = "1")
    private Integer id;
    @ApiModelProperty(value = "优惠券名称",dataType = "String",example = "物业十周年")
    private String couponName;
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "TKZC-A3-13D-1DY-1001")
    private String roomCode;
    @ApiModelProperty(value = "剩余金额",dataType = "Double",example = "512.31")
    private Double balanceAmount;
    @ApiModelProperty(value = "总金额",dataType = "Double",example = "6000.00")
    private Double money;
    private String organizationName;
    private String endDate;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
