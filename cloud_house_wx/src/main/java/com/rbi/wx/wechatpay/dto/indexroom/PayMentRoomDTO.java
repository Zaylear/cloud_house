package com.rbi.wx.wechatpay.dto.indexroom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 房屋页面缴费明细
 * 房屋信息的实体
 */
@ApiModel(value = "房屋页面缴费明细的房屋信息的实体")
public class PayMentRoomDTO implements Serializable{
    @ApiModelProperty(value = "小区名称",dataType = "String",example = "云城尚品")
    private String villageName;
    @ApiModelProperty(value = "地块名称",dataType = "String",example = "A3组团")
    private String regionName;
    @ApiModelProperty(value = "楼宇名称",dataType = "String",example = "12栋")
    private String buildingName;
    @ApiModelProperty(value = "单元名称",dataType = "String",example = "1单元")
    private String unitName;
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;
    @ApiModelProperty(value = "房间大小",dataType = "String",example = "100.00")
    private String roomSize;
    @ApiModelProperty(value = "业主编号",dataType = "String",example = "155953281305358")
    private String userId;
    @ApiModelProperty(value = "业主姓名",dataType = "String",example = "张先生")
    private String userName;
    @ApiModelProperty(value = "业主手机号",dataType = "String",example = "18300851461")
    private String mobilePhone;

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
