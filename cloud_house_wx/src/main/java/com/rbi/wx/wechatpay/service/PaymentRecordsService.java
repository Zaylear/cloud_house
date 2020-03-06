package com.rbi.wx.wechatpay.service;

import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

public interface PaymentRecordsService {
    JsonEntityUtil payMent(String roomCode,String chargeCode);
    JsonEntityUtil getPayment(String roomCode,String organizationId);
    JsonEntityUtil getPaymentRecords(String userId,Integer pageSize,Integer pageNum);
    JsonEntityUtil getPayMentUser(Integer pageSize,Integer pageNum,String roomCode);
    JsonEntityUtil getPayerPayList(Integer pageSize, Integer pageNum, String roomCode,String userId);
    JsonEntityUtil getPayRoom(String roomCode);
    JsonEntityUtil getPayerInfo(String userId);
    JsonEntityUtil getPayMentCoupon(String userId,String roomCode,String chargeCode,Integer payMonth);
}
