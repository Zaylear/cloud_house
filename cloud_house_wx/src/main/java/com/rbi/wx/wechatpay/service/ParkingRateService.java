package com.rbi.wx.wechatpay.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

public interface ParkingRateService {
    JsonEntityUtil getInfoByLicensePlate(JSONObject jsonObject);
    JsonEntityUtil wxPay(JSONObject jsonObject);
    JsonEntityUtil parkingRatewxPay(JSONObject jsonObject);
    JsonEntityUtil getRoomCode(JSONObject jsonObject);
    JsonEntityUtil getParkingSpaceCode(JSONObject jsonObject);
    JsonEntityUtil getInfoByParkingSpaceCode(JSONObject jsonObject);
}
