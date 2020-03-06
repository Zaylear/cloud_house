package com.rbi.wx.wechatpay.util.paylistfactory;

import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

public class DecorationGetPayList implements GetPayList{
    @Override
    public JsonEntityUtil getPayList(String roomCode,String organizationId, String chargeCode, PayMentMapper payMentMapper) {
        return null;
    }
}
