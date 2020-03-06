package com.rbi.wx.wechatpay.service;

import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;

public interface PayService {
    JsonEntityUtil getProperty(Double roomSize);
    JsonEntityUtil chargePay(ChargePayRequestEntity chargePayRequestEntity);
    JsonEntityUtil getPayMentList(String roomCode,String chargeCode,String organizationId);
    JsonEntityUtil getPayCoupon(String roomCode,String chargeCode);
}
