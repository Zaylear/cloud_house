package com.rbi.interactive.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IRoomAndCustomerDAO {

    @Select("select surplus from room_customer WHERE room_code = #{roomCode} AND customer_user_id = #{customerUserId} and logged_off_state = 0")
    public Double findByRoomCodeAndUserIdSurplus(@Param("roomCode") String roomCode,@Param("customerUserId") String customerUserId);

    @Update("UPDATE room_customer SET surplus = #{surplus} WHERE room_code = #{roomCode} AND customer_user_id = #{customerUserId} and logged_off_state = 0")
    public void updateSurplus(@Param("surplus") Double surplus,@Param("roomCode") String roomCode,@Param("customerUserId") String customerUserId);

}
