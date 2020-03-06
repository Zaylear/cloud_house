package com.rbi.wx.wechatpay.util.successfactory;

import com.rbi.wx.wechatpay.mapper.PaySuccessMapper;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;

import java.util.Map;

public interface SuccessInterface {
    void addOriginalBill (Map map, PaySuccessMapper paySuccessMapper,RSAGetStringUtil rsaGetStringUtil);
}
