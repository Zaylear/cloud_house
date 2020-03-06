package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.dto.OwnerDTO;
import com.rbi.admin.entity.edo.CustomerInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CustomerDAO {
    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.floor_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "regional_village.village_code = #{villageCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6) " +
            "ORDER BY regional_room.room_code,room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findVillageByPage(@Param("villageCode") String villageCode, @Param("roomType") Integer roomType,
                                     @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_village.village_code = #{villageCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6)")
    int findVillageNum(@Param("villageCode") String villageCode,@Param("roomType") Integer roomType);


    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.floor_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "regional_region.region_code = #{regionCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6) " +
            "ORDER BY regional_room.room_code,room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findRegionByPage(@Param("regionCode") String regionCode, @Param("roomType") Integer roomType,
                                    @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_region.region_code = #{regionCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6)")
    int findRegionNum(@Param("regionCode") String regionCode,@Param("roomType") Integer roomType);

    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "regional_building.building_code = #{buildingCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6) " +
            "ORDER BY regional_room.room_code,room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findBuildingByPage(@Param("buildingCode") String buildingCode,@Param("roomType") Integer roomType,
                                      @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_building.building_code = #{buildingCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6)")
    int findBuildingNum(@Param("buildingCode") String buildingCode,@Param("roomType") Integer roomType);


    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "regional_unit.unit_code = #{unitCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6) " +
            "ORDER BY regional_room.room_code,room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findUnitByPage(@Param("unitCode") String unitCode, @Param("roomType") Integer roomType,
                                  @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_unit.unit_code = #{unitCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6)")
    int findUnitNum(@Param("unitCode") String unitCode,@Param("roomType") Integer roomType);


    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "regional_room.room_code = #{roomCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6) " +
            "ORDER BY regional_room.room_code,room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findRoomByPage(@Param("roomCode") String roomCode,@Param("roomType") Integer roomType,
                                  @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_room.room_code = #{roomCode} AND regional_room.room_status = 2 AND regional_room.room_type = #{roomType} AND\n" +
            "room_customer.identity IN (3,6)")
    int findRoomNum(@Param("roomCode") String roomCode,@Param("roomType") Integer roomType);







    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "regional_room.room_code LIKE ${roomCode} AND regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND\n" +
            "room_customer.identity IN (3,6)\n" +
            "ORDER BY room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findCustomerByRoomCode(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId,
                                       @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_room.room_code LIKE ${roomCode} AND regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND\n" +
            " room_customer.identity IN (3,6)")
    int findCustomerByRoomCodeNum(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);


    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "sys_customer_info.mobile_phone like ${mobilePhone} AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND\n" +
            "room_customer.identity IN (3,6)\n" +
            "ORDER BY room_customer.identity limit #{pageNo},#{pageSize}")
    List<OwnerDTO> findCustomerByPhone(@Param("mobilePhone") String mobilePhone, @Param("organizationId") String organizationId,
                                           @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "sys_customer_info.mobile_phone like ${mobilePhone} AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND" +
            " room_customer.identity IN (3,6)")
    int findCustomerByPhoneNum(@Param("mobilePhone") String mobilePhone, @Param("organizationId") String organizationId);

    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "sys_customer_info.id_number like ${idNumber} AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND\n" +
            "room_customer.identity IN (3,6)\n" +
            "ORDER BY room_customer.identity limit #{pageNo},#{pageSize}")
    public List<OwnerDTO> findCustomerByIdNumber(@Param("idNumber") String idNumber, @Param("organizationId") String organizationId,
                                              @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "sys_customer_info.id_number like ${idNumber} AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND" +
            " room_customer.identity IN (3,6)")
    int findCustomerByIdNumberNum(@Param("idNumber") String idNumber, @Param("organizationId") String organizationId);

    @Select("SELECT \n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_type,\n" +
            "regional_room.room_size,\n" +
            "regional_room.room_status,\n" +
            "regional_room.renovation_start_time,\n" +
            "regional_room.renovation_status,\n" +
            "regional_room.renovation_deadline,\n" +

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
            "sys_customer_info.surname like ${surname} AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND\n" +
            "room_customer.identity IN (3,6)\n" +
            "ORDER BY room_customer.identity limit #{pageNo},#{pageSize}")
    public List<OwnerDTO> findCustomerBySurname(@Param("surname") String surname, @Param("organizationId") String organizationId,
                                             @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "sys_customer_info.surname like ${surname} AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_status = 2 AND" +
            " room_customer.identity IN (3,6)")
    int findCustomerBySurnameNum(@Param("surname") String surname, @Param("organizationId") String organizationId);


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
            "regional_room.room_code = #{roomCode} AND room_customer.customer_user_id = #{customerUserId}")
    OwnerDTO findOwnerByCodeAndUserId(@Param("roomCode") String roomCode, @Param("customerUserId") String customerUserId);

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
            "regional_room.renovation_deadline,\n" +
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
            "logged_off_state = 0 AND\n" +
            "room_customer.customer_user_id = #{customerUserId}")
    List<OwnerDTO> findRoomByCustomerUserId(@Param("customerUserId") String customerUserId);

    @Select("SELECT count(*)\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "room_customer.identity = 3 AND regional_room.room_code = #{roomCode}")
    int findCustomerNum(@Param("roomCode") String roomCode);

    @Select("SELECT *\n" +
            "FROM sys_customer_info WHERE\n" +
            "id_number = #{idNumber}")
    CustomerInfoDO findByIdNumber(@Param("idNumber") String idNumber);

    @Select("SELECT room_status FROM regional_room WHERE room_code = #{roomCode}")
    Integer findStatusByRoomCode(@Param("roomCode") String roomCode);

    @Update("update regional_room set room_status = 2 where room_code = #{roomCode}")
    void updateRoomStatus(@Param("roomCode") String roomCode);


}



