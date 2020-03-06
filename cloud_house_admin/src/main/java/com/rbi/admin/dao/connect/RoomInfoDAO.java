package com.rbi.admin.dao.connect;


import org.apache.ibatis.annotations.*;


@Mapper
public interface RoomInfoDAO {

    //顾客注销
    @Update("update room_customer set logged_off_state = 1 where room_code = #{roomCode} and customer_user_id = #{customerUserId}")
    void logoutRC(@Param("roomCode")String roomCode,@Param("customerUserId")String customerUserId);
    //身份改变注销
    @Delete("update room_customer set identity = #{identity} where room_code = #{roomCode} and customer_user_id = #{customerUserId}")
    void ChangeCustomerIdentity(@Param("identity")Integer identity,@Param("roomCode")String roomCode,@Param("customerUserId")String customerUserId);


    //超级删除
    //删除房屋
    @Delete("delete from regional_room where room_code = #{roomCode}")
    void deleteRoom(@Param("roomCode")String roomCode);
    //删除顾客
    @Delete("delete from room_customer where room_code = #{roomCode}")
    void deleteCustomer(@Param("roomCode")String roomCode);
    //删除收费项目
    @Delete("delete from room_charge_items where room_code = #{roomCode}")
    void deleteRoomItem(@Param("roomCode")String roomCode);

    @Delete("delete from room_customer where room_code = #{roomCode} and customer_user_id = #{customerUserId}")
    void deleteSingleCustomer(@Param("roomCode")String roomCode,@Param("customerUserId")String customerUserId);


}



