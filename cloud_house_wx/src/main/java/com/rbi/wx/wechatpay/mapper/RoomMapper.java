package com.rbi.wx.wechatpay.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper {
    @Select("select room_code from room_customer where user_id=#{userId} and logged_off_state = '0'" +
            "and identity = '2'")
    List<String> findRoomByUserId(String userId);
    @Select("select room_code from room_customer where user_id=#{userId} and logged_off_state = '0'" +
            "and identity = 3 and past_due = '0'")
    List<String> findRoomByUserIdTenant(String userId);
}
