package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "用户关联信息实体")
public class DeputyDeleteRequestEntity implements Serializable{
    @ApiModelProperty(value = "需要删除关联的用户ID",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String userId;
    @ApiModelProperty(value = "需要删除关联的房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;
    @ApiModelProperty(value = "需要删除关联的身份",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String identity;

    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

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
