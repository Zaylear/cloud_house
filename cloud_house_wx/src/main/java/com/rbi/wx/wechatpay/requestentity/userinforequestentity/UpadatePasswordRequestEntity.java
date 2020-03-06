package com.rbi.wx.wechatpay.requestentity.userinforequestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel(value = "密码接收实体")
public class UpadatePasswordRequestEntity implements Serializable{
    @ApiModelProperty(value ="旧密码",dataType = "String",example = "11111")
    private String oldPsw;
    @ApiModelProperty(value ="新密码",dataType = "String",example = "15555")
    private String newPsw;
    public String getOldPsw() {
        return oldPsw;
    }

    public void setOldPsw(String oldPsw) {
        this.oldPsw = oldPsw;
    }

    public String getNewPsw() {
        return newPsw;
    }

    public void setNewPsw(String newPsw) {
        this.newPsw = newPsw;
    }
}
