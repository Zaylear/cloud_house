package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "接收参数的实体")
public class PayMentListRequestEntity implements Serializable{
    @ApiModelProperty(value = "roomCode",dataType = "String",example = "TKZC-A-1D-1DY-100")
    private String roomCode;
    @ApiModelProperty(value = "chargeCode",dataType = "String",example = "qwer124")
    private String chargeCode;
    private String organizationId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

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
}
