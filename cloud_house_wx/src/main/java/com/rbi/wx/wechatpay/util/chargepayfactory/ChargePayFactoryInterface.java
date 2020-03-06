package com.rbi.wx.wechatpay.util.chargepayfactory;

import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;

public interface ChargePayFactoryInterface {
    ChargePayDTO getChargePayDTO(ChargePayRequestEntity chargePayRequestEntity,PayMentMapper payMentMapper);
}
