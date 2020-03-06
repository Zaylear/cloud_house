package com.rbi.wx.wechatpay.service;

import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

import javax.servlet.http.HttpServletRequest;

public interface RoomService {
    JsonEntityUtil findRoomByUserId(HttpServletRequest request);
}
