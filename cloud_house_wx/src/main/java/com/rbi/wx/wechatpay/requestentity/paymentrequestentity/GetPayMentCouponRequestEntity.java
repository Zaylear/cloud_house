package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "获取优惠的实体")
public class GetPayMentCouponRequestEntity implements Serializable{
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "TKZC-13D-A3-1DY-1101")
    private String roomCode;
    @ApiModelProperty(value = "项目编号",dataType = "String" ,example = "qwer1234")
    private String chargeCode;
    @ApiModelProperty(value = "缴费的月数",dataType = "Integer",example = "1")
    private Integer payMonth;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(Integer payMonth) {
        this.payMonth = payMonth;
    }
}
