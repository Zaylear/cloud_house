package com.rbi.wx.wechatpay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户的信息
 */
@ApiModel("身份与租房实体")
public class UserIdentityEntity {
    @ApiModelProperty(value = "身份",dataType = "Integer",example = "2")
    private Integer identity;//业主还是副业主
    @ApiModelProperty(value = "租房日期 如果为副业主则为NULL",dataType = "String",example = "2019-14-13")
    private String date;//开始租房日期
    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
