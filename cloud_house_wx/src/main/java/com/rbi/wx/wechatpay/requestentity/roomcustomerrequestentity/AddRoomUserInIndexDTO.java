package com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "参数")
public class AddRoomUserInIndexDTO {
    private String identity;
    private List<String> roomCodes;
    private String userId;
    private String startDate;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public List<String> getRoomCodes() {
        return roomCodes;
    }

    public void setRoomCodes(List<String> roomCodes) {
        this.roomCodes = roomCodes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
