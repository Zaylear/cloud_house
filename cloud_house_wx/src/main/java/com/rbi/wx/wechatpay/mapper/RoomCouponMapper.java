package com.rbi.wx.wechatpay.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoomCouponMapper {
    @Select("select charge_code,charge_name,state_of_arrears from original_bill" +
            "where room_code in " +
            "(select room_code from room_customer where logged_off_state='0'" +
            "and user_id=#{userID})")
    public void getRoomCode(String userID);
}
