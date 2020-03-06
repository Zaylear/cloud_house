package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.paymentrecordentity.PayMentChargeEntity;
import com.rbi.wx.wechatpay.dto.paymentrecordentity.PayMentPayerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WeChatPayMapper {
    @Select("select user_id as userId,username as userName,mobile_phone as userPhone,surname from sys_customer_info " +
            "where user_id=#{userId}")
    PayMentPayerEntity findPayerInfo(String userId);
}
