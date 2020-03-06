package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "缴费人历史信息")
public class RoomIndexPayListRquestEntity implements Serializable{
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;
    @ApiModelProperty(value = "用户id",dataType = "String",example = "155953281305362")
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
    @ApiModelProperty(value = "查询第几页",dataType = "Integer",example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "单页有几笔数据",dataType = "Integer",example = "1")
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
