package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import com.rbi.wx.wechatpay.dto.User;
import com.rbi.wx.wechatpay.dto.UserIdentityEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "接收关联用户信息的实体")
public class AddUserRequestEntity {
    @ApiModelProperty(value = "需要关联的房间编号列",dataType = "String",example = "TKZC-13D-1DY-1001")
    private List<UpdateRoomCustomerRequestEntity> roomList;
    @ApiModelProperty(value = "用户信息实体",dataType = "User")
    private User user;
    private UserIdentityEntity userIdentityEntity;
    //验证码
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }


    public List<UpdateRoomCustomerRequestEntity> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<UpdateRoomCustomerRequestEntity> roomList) {
        this.roomList = roomList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserIdentityEntity getUserIdentityEntity() {
        return userIdentityEntity;
    }

    public void setUserIdentityEntity(UserIdentityEntity userIdentityEntity) {
        this.userIdentityEntity = userIdentityEntity;
    }
}
