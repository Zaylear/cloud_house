package com.rbi.wx.wechatpay.service.impl;


import com.rbi.wx.wechatpay.mapper.IBillDAO;
import com.rbi.wx.wechatpay.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class ThisSystemOrderIdServiceImpl {

//    @Autowired(required = false)
//    IBillDAO iBillDAO;
//
//    @Override
//    public String thisSystemOrderId(String organizationId) {
//        String orderId = "";
//        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
//        Integer orderIdsCount = iBillDAO.findOrderIdsByorganizationId(organizationId,time);
//        orderId = time+String.format("%08d", orderIdsCount);
//        return orderId;
//    }
}
