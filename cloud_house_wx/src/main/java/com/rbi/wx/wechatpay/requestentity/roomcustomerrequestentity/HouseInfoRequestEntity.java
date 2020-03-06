package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "房屋信息详情")
public class HouseInfoRequestEntity {
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;
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
}
