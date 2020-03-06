package com.rbi.wx.wechatpay.util.paylistfactory;

import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

public interface GetPayList {
    JsonEntityUtil getPayList(String roomCode,String organizationId, String chargeCode, PayMentMapper payMentMapper);
}
