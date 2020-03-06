package com.rbi.wx.wechatpay.service;

import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

public interface RoomCouponService {
    JsonEntityUtil getRoomInfo(String userId);
}
