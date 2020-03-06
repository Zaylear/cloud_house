package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "分页查询的数据")
public class PayMentUserRquestEntity {
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;

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
