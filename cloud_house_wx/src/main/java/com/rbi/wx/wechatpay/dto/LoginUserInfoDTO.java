package com.rbi.wx.wechatpay.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 登陆之后界面的用户信息 实体
 */
@ApiModel(value = "主页的用户信息实体")
public class LoginUserInfoDTO{
    @ApiModelProperty(value = "用户姓名",dataType = "String",example = "张先生")
    private String userName;
//    @ApiModelProperty(value = "头像路径",dataType = "String",example = "http://123.249.28.108:88/wx-header/156039483183687.jpg")
//    private String path;
    @ApiModelProperty(value = "手机号",dataType = "String",example = "123912312")
    private String mobilePhone;
    @ApiModelProperty(value = "性别",dataType = "String",example = "男")
    private String sex;
    @ApiModelProperty(value = "用户编号",dataType = "String",example = "154546465464")
    private String userId;
    @ApiModelProperty(value = "最高权限",dataType = "String",example = "2")
    private String maxIdentity;

    public String getMaxIdentity() {
        return maxIdentity;
    }

    public void setMaxIdentity(String maxIdentity) {
        this.maxIdentity = maxIdentity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
