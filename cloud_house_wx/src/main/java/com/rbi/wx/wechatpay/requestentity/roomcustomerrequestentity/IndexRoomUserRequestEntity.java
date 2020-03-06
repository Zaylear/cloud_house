package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "实体")
public class IndexRoomUserRequestEntity implements Serializable{
    @ApiModelProperty(value = "pagesize")
    private Integer pageSize;
    @ApiModelProperty(value = "pagenum")
    private Integer pageNum;
    @ApiModelProperty(value = "identity")
    private Integer identity;



    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }
}
