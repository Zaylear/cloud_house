package com.rbi.wx.wechatpay.requestentity.userinforequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "用户名实体")
public class IndexUpdateUserNameEntity implements Serializable{
    @ApiModelProperty(value = "用户名",dataType = "String",example = "瓜")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
