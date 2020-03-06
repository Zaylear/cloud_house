package com.rbi.wx.wechatpay.requestentity.paymentrequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "实体")
public class PayChargeRequestEntity implements Serializable{
    @ApiModelProperty(value = "房间编号")
    private String roomCode;
    @ApiModelProperty(value = "项目编号")
    private String chargeCode;
    @ApiModelProperty(value = "优惠券ID 没有的时候穿-1")
    private String couponId;
}
