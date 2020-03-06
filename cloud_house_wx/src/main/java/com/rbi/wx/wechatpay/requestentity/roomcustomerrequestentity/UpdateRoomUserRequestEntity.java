package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import com.rbi.wx.wechatpay.dto.indexroom.UpdateRoomUserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "参数实体")
public class UpdateRoomUserRequestEntity implements Serializable{
    @ApiModelProperty(value = "用户名",example = "zxg")
    private String userName;
    @ApiModelProperty(value = "性别 1为男 2为女",example = "1")
    private String sex;
    @ApiModelProperty(value = "用户电话",example = "22312312321321")
    private String userPhone;
    @ApiModelProperty(value = "userId",example = "22312312321321")
    private String userId;
    @ApiModelProperty(value = "房间编号列")
    private List<UpdateRoomUserDTO> roomCodes;
    @ApiModelProperty(value = "身份",example = "2")
    private String identity;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UpdateRoomUserDTO> getRoomCodes() {
        return roomCodes;
    }

    public void setRoomCodes(List<UpdateRoomUserDTO> roomCodes) {
        this.roomCodes = roomCodes;
    }
}
