package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
@ApiModel(value = "支付信息实体")
public class ChargePayRequestEntity implements Serializable{
    @ApiModelProperty(value = "openId",dataType = "String",example = "o_Jhq1AqGCADdhWrZLMcrX5NYMnE")
    private String openId;
    @ApiModelProperty(value = "内容",dataType = "String",example = "物业费缴费")
    private String body;
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "TKZC-A3-13D-1DY-1001")
    private String roomCode;
    @ApiModelProperty(value = "缴费月数",dataType = "String",example = "2")
    private String datedif;
    @ApiModelProperty(value = "项目ID",dataType = "String",example = "qwer123")
    private String chargeCode;
    @ApiModelProperty(value = "优惠券ID",dataType = "String",example = "2")
    private String couponId;
    @ApiModelProperty(value = "过期时间",dataType = "String",example = "2")
    private String StartTime;
    @ApiModelProperty(value = "过期时间",dataType = "String",example = "2")
    private String EndTime;
    @ApiModelProperty(value = "组织编号",dataType = "String",example = "2")
    private String organizationId;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getDatedif() {
        return datedif;
    }

    public void setDatedif(String datedif) {
        this.datedif = datedif;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
