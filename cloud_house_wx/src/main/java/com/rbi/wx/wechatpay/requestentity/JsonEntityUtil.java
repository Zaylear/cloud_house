package com.rbi.wx.wechatpay.requestentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class JsonEntityUtil <T> extends JsonUtil{
    @ApiModelProperty(value = "code为1000时查询到有对象 否则为null")
    private T entity;
    public JsonEntityUtil(String code, String msg) {
        super(code, msg);
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
