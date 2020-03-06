package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "查询的是业主还是副业主")
public class WxRoomUserRequestEntity implements Serializable{
    @ApiModelProperty(value = "查询是副业主还是业主",dataType = "String",example = "2")
    private String identity;
    @ApiModelProperty(value = "房间编号 /mine/tenantinfo&&/mine/deputyinfo就传null",dataType = "String",example = "null")
    private String roomCode;
    @ApiModelProperty(value = "用户编号 /mine/tenantinfo&&/mine/deputyinfo就传APPKEY的数据",dataType = "String",example = "155953281305361")
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
