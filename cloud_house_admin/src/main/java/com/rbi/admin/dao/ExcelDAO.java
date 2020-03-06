package com.rbi.admin.dao;


import com.rbi.admin.entity.dto.Owner2DTO;
import com.rbi.admin.entity.dto.RoomInfoDTO;
import com.rbi.admin.entity.dto.structure.ChargeCodeDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExcelDAO {

    @Insert("insert into regional_region (village_code,region_code,region_name) values (#{villageCode},#{regionCode},#{regionName})")
    void addRegion(@Param("villageCode") String villageCode,
                          @Param("regionCode") String regionCode,
                          @Param("regionName") String regionName);

    @Insert("insert into regional_building (region_code,building_code,building_name)values(#{regionCode},#{buildingCode},#{buildingName})")
    void addBuilding(@Param("regionCode") String regionCode,
                            @Param("buildingCode") String buildingCode,
                            @Param("buildingName") String buildingName);

    @Insert("insert into regional_unit (building_code,unit_code,unit_name)values(#{buildingCode},#{unitCode},#{unitName})")
    void addUnit(@Param("buildingCode") String buildingCode,
                            @Param("unitCode") String unitCode,
                            @Param("unitName") String unitName);

    @Insert("insert into regional_floor (floor_code,floor,unit_code)values(#{floorCode},#{floor},#{unitCode})")
    void addFloor(@Param("floorCode") String floorCode,
                 @Param("floor") Integer floor,
                 @Param("unitCode") String unitCode);

    @Insert("insert into regional_floor (floor_code,unit_code)values(#{floorCode},#{unitCode})")
    void addFloor2(@Param("floorCode") String floorCode,@Param("unitCode") String unitCode);


    @Insert("insert into regional_room (unit_code,room_code,room_size,room_type,room_status,renovation_status,renovation_start_time," +
            "renovation_deadline,start_billing_time,real_recycling_home_time,floor_code)" +
            "values(#{unitCode},#{roomCode},#{roomSize},#{roomType},#{roomStatus},#{renovationStatus},#{renovationStartTime}," +
            "#{renovationDeadline},#{startBillingTime},#{realRecyclingHomeTime},#{floorCode})")
    void addRoom(@Param("unitCode") String unitCode, @Param("roomCode") String roomCode,
                        @Param("roomSize") Double roomSize, @Param("roomType") Integer roomType,
                        @Param("roomStatus") Integer roomStatus, @Param("renovationStatus") Integer renovationStatus,
                        @Param("renovationStartTime") String renovationStartTime, @Param("renovationDeadline") String renovationDeadline,
                        @Param("startBillingTime") String startBillingTime, @Param("realRecyclingHomeTime") String realRecyclingHomeTime,
                        @Param("floorCode") String floorCode);


    @Insert("insert into sys_customer_info (user_id,surname,id_number,sex,mobile_phone,remarks)" +
            "values(#{userId},#{surname},#{idNumber},#{sex},#{mobilePhone},#{remarks})")
    void addCustomer(@Param("userId") String userId, @Param("surname") String surname,@Param("idNumber") String idNumber,
                            @Param("sex") Integer sex,@Param("mobilePhone") String mobilePhone,
                            @Param("remarks") String remarks);


    @Update("update sys_customer_info set mobile_phone = #{mobilePhone},surname=#{surname},id_number = #{idNumber},sex=#{sex},remarks =#{remarks} where user_id = #{userId}")
    void updateCustomer(@Param("mobilePhone")String mobilePhone,@Param("surname")String surname,
                               @Param("idNumber")String idNumber, @Param("sex")Integer sex,
                               @Param("remarks")String remarks, @Param("userId")String userId);

    @Update("update room_customer set identity=#{identity},normal_payment_status =#{normalPaymentStatus}," +
            "start_billing_time = #{startBillingTime},real_recycling_home_time=#{realRecyclingHomeTime}" +
            "where room_code=#{roomCode} and customer_user_id=#{customerUserId}")
    void updateRC(@Param("identity")Integer identity,@Param("normalPaymentStatus")Integer normalPaymentStatus,
                         @Param("startBillingTime")String startBillingTime,@Param("realRecyclingHomeTime")String realRecyclingHomeTime,
                         @Param("roomCode")String roomCode, @Param("customerUserId")String customerUserId);

    @Update("update room_customer set identity=#{identity},normal_payment_status =#{normalPaymentStatus}," +
            "start_time = #{startTime},end_time=#{endTime}" +
            "where room_code=#{roomCode} and customer_user_id=#{customerUserId}")
    void updateRCC(@Param("identity")Integer identity,@Param("normalPaymentStatus")Integer normalPaymentStatus,
                         @Param("startTime")String startBillingTime,@Param("endTime")String endTime,
                         @Param("roomCode")String roomCode, @Param("customerUserId")String customerUserId);

    @Insert("insert into room_customer (customer_user_id,room_code,start_billing_time,organization_id,logged_off_state,normal_payment_status,identity,surplus,real_recycling_home_time)values(#{customerUserId},#{roomCode},#{startBillingTime},#{organizationId},0,#{normalPaymentStatus},#{identity},#{surplus},#{realRecyclingHomeTime})")
    void addRoomCustomer(@Param("customerUserId")String customerUserId,@Param("roomCode")String roomCode,
                                @Param("startBillingTime")String startBillingTime,@Param("organizationId")String organizationId,
                                @Param("normalPaymentStatus")Integer normalPaymentStatus,@Param("identity")Integer identity,
                                @Param("surplus")Float surplus,@Param("realRecyclingHomeTime")String realRecyclingHomeTime);


    @Insert("insert into room_customer (customer_user_id,room_code,start_time,organization_id,logged_off_state," +
            "normal_payment_status,identity,surplus,end_time)values(#{customerUserId},#{roomCode},#{startTime}," +
            "#{organizationId},0,#{normalPaymentStatus},#{identity},#{surplus},#{endTime})")
    void addRoomCustomerC(@Param("customerUserId")String customerUserId,@Param("roomCode")String roomCode,
                                @Param("startTime")String startTime,@Param("organizationId")String organizationId,
                                @Param("normalPaymentStatus")Integer normalPaymentStatus,@Param("identity")Integer identity,
                                @Param("surplus")Float surplus,@Param("endTime")String endTime);



    @Update("update regional_room set room_size=#{roomSize},room_type =#{roomType}," +
            "room_status = #{roomStatus},renovation_status = #{renovationStatus}," +
            "renovation_start_time = #{renovationStartTime}," +
            "renovation_deadline= #{renovationDeadline}," +
            "start_billing_time=#{startBillingTime}," +
            "real_recycling_home_time=#{realRecyclingHomeTime},floor_code = #{floorCode} " +
            "where room_code=#{roomCode}")
    void updateRoom(@Param("roomSize")Double roomSize,@Param("roomType")Integer roomType,
                         @Param("roomStatus")Integer roomStatus,@Param("renovationStatus")Integer renovationStatus,
                         @Param("renovationStartTime")String renovationStartTime,@Param("renovationDeadline")String renovationDeadline,
                         @Param("startBillingTime") String startBillingTime, @Param("realRecyclingHomeTime") String realRecyclingHomeTime,
                         @Param("floorCode") String floorCode,@Param("roomCode")String roomCode);

    @Select("select count(*) from room_charge_items where room_code = #{roomCode} and charge_code = #{chargeCode}")
    int findRoomItemNum(@Param("roomCode") String roomCode,@Param("chargeCode") String chargeCode);

    @Select("select charge_code,charge_type from charge_item where organization_id = #{organizationId} and must_pay = 1")
    List<ChargeCodeDTO> findChargeItem(@Param("organizationId") String organizationId);


    @Insert("insert into room_charge_items (room_code,charge_code,surplus,village_code,village_name," +
            "region_code,region_name,building_code,building_name,unit_code,unit_name,idt,organization_id,organization_name)" +
            "values(#{roomCode},#{chargeCode},#{surplus},#{villageCode},#{villageName},#{regionCode}," +
            "#{regionName},#{buildingCode},#{buildingName},#{unitCode},#{unitnName},#{idt},#{organizationId},#{organizationName})")
    void addRoomItem2(@Param("roomCode") String roomCode,@Param("chargeCode") String chargeCode,
                             @Param("surplus") Double surplus,@Param("villageCode") String villageCode,
                             @Param("villageName") String villageName,@Param("regionCode") String regionCode,
                             @Param("regionName") String regionName,@Param("buildingCode") String buildingCode,
                             @Param("buildingName") String buildingName,@Param("unitCode") String unitCode,
                             @Param("unitnName") String unitnName,@Param("idt") String idt,@Param("organizationId") String organizationId,
                             @Param("organizationName") String organizationName);

    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_region.region_name,\n" +
            "regional_building.building_name,\n" +
            "regional_unit.unit_name,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_room.room_code = #{roomCode}")
    List<RoomInfoDTO> findRoom(@Param("roomCode") String roomCode);

    @Select("SELECT \n" +
            "sys_customer_info.surname,\n" +
            "sys_customer_info.id_number,\n" +
            "sys_customer_info.sex,\n" +
            "sys_customer_info.mobile_phone,\n" +
            "sys_customer_info.remarks,\n" +

            "room_customer.start_time,\n" +
            "room_customer.end_time,\n" +

            "room_customer.customer_user_id,\n" +
            "room_customer.identity,\n" +
            "room_customer.logged_off_state,\n" +
            "room_customer.start_billing_time,\n" +
            "room_customer.normal_payment_status,\n" +
            "room_customer.real_recycling_home_time\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_room.room_code =#{roomCode} AND\n" +
            "room_customer.identity = #{identity}")
    List<Owner2DTO> findByRoomCode(@Param("roomCode") String roomCode,@Param("identity") int identity);

    @Select("select floor from regional_floor where floor_code = #{floorCode}")
    Integer findFloorByFloorCode(@Param("floorCode")String floorCode);




}
