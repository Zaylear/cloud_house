package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.parkingrate.ParkingManagement;
import com.rbi.wx.wechatpay.dto.parkingrate.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ParkingRateMapper {
    @Select("select organization_id as organizationId,room_code as roomCode where customer_user_id=#{userId} and " +
            "logged_off_state = '0' and (past_due = '0' or past_due is null)")
 List<Room> getRoomInfoByUserId(String userId);

    @Select("select organization_id as organizationId,CONCAT(village_name,'-',region_name,'-',village_code,'-',building_name) as address," +
            "parking_space_code as parkingSpaceCode,contract_number as contractNumber," +
            "license_plate_number as licensePlateNumber," +
            "surname from parking_space_management where " +
            "authorized_person_id_number =#{IdCard} and logged_off_state='0' ")
    List<ParkingManagement> getParkingManagementInfo(String IdCard);


    @Select("select id_number from sys_customer_info where user_id=#{userId}")
    String getIdCard(String userId);
}
