package com.rbi.wx.wechatpay.dto.indexroom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 房屋主页的实体
 */
@ApiModel(value = "房屋主页的房屋信息")
public class IndexRoomDTO implements Serializable{
    //请求数据 List<String> roomCodes  返回以下数据
    @ApiModelProperty(value = "房屋坐落",dataType = "String",example = "未来城13栋A3组团小区w")
    private String address; //房屋坐落
    @ApiModelProperty(value = "房屋编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode; //roomcode
    @ApiModelProperty(value = "房屋详情",dataType = "String",example = "白云区-云城尚品-A3组团-12栋-1单元")
    private String roomInfo;
    /**
     * 组织ID
     */
    private String organizationId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }

    @ApiModelProperty(value = "欠费状态",dataType = "String",example = "已欠费")
    private String status; //欠费状态  这个我自己查
    @ApiModelProperty(value = "图片路径",dataType = "String",example = "http://2m2766a493.iok.la/photo/X%7DH%7BEJZA%7B6NSP)O2NAM~JYH.jpg")
    private String photoPath;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
