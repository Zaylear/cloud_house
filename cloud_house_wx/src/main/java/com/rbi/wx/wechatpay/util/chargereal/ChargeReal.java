package com.rbi.wx.wechatpay.util.chargereal;

import com.rbi.wx.wechatpay.mapper.ChargeRealMapper;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;

public interface  ChargeReal {
   double getMoney( ChargePayRequestEntity chargePayRequestEntity,ChargeRealMapper chargeRealMapper) throws Exception;
}
