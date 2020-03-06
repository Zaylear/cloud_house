package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "分页查询的数据")
public class GetPayMentRecordsRequestEntity implements Serializable{
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
