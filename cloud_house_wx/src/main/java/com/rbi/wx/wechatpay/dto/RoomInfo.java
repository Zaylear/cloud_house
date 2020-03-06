package com.rbi.wx.wechatpay.dto;

import com.rbi.wx.wechatpay.dto.indexroom.IndexRoomDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 房屋的副业主或者租客的实体
 */
@ApiModel(value = "用户信息实体")
public class RoomInfo  implements Serializable{
    @ApiModelProperty(value = "房屋坐落",dataType = "String",example = "贵阳市云城尚品小区")
    private String address;
    @ApiModelProperty(value = "绑定日期",dataType = "String",example = "2019-04-13")
    private String date;
    @ApiModelProperty(value = "用户姓名",dataType = "String",example = "张先生")
    private String userName;
    @ApiModelProperty(value = "房间编号",dataType = "String",example = "YCSP-A3-12D-1-1001")
    private String roomCode;
    @ApiModelProperty(value = "用户ID",dataType = "String",example = "155953281305359")
    private String userId;
    @ApiModelProperty(value = "开始租房日期",dataType = "String",example = "2019-03-13")
    private String startDate;
    @ApiModelProperty(value = "电话",dataType = "String",example = "2019-03-13")
    private String userPhone;
    @ApiModelProperty(value = "性别",dataType = "String",example = "2019-03-13")
    private String sex;
    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
