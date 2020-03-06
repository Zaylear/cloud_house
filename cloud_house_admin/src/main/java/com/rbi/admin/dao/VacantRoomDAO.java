package com.rbi.admin.dao;

import com.rbi.admin.entity.dto.RoomDTO;
import com.rbi.admin.entity.dto.RoomInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VacantRoomDAO {

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
                "regional_room.idt,\n" +
                "regional_room.udt\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_room.room_code LIKE ${roomCode} AND regional_room.room_status = 1 limit #{pageNo},#{pageSize}")
        List<RoomDTO> findRoomByRoomCode(@Param("roomCode") String roomCode,@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

        @Select("SELECT count(*)\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_room.room_code LIKE ${roomCode} AND regional_room.room_status = 1")
        int findRoomByRoomCodeNum(@Param("roomCode") String roomCode);



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
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.idt,\n" +
                "regional_room.udt\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_village.village_code = #{villageCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1 limit #{pageNo},#{pageSize}")
        List<RoomDTO> findVillageByPage(@Param("villageCode") String villageCode,@Param("roomType") String roomType,
                                        @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

        @Select("SELECT count(*)\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_village.village_code = #{villageCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1")
        int findVillageNum(@Param("villageCode") String villageCode,@Param("roomType") String roomType);

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
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.idt,\n" +
                "regional_room.udt\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_region.region_code = #{regionCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1 limit #{pageNo},#{pageSize}")
        List<RoomDTO> findRegionByPage(@Param("regionCode") String regionCode,@Param("roomType") String roomType,
                                       @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

        @Select("SELECT count(*)\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_region.region_code = #{regionCode} AND room_type = #{roomType} and regional_room.room_status = 1")
        int findRegionNum(@Param("regionCode") String regionCode,@Param("roomType") String roomType);

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
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.idt,\n" +
                "regional_room.udt\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_building.building_code = #{buildingCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1 limit #{pageNo},#{pageSize}")
        List<RoomDTO> findBuildingByPage(@Param("buildingCode") String buildingCode,@Param("roomType") String roomType,
                                         @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

        @Select("SELECT count(*)\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_building.building_code = #{buildingCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1")
        int findBuildingNum(@Param("buildingCode") String buildingCode,@Param("roomType") String roomType);


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
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.idt,\n" +
                "regional_room.udt\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_unit.unit_code = #{unitCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1 limit #{pageNo},#{pageSize}")
        List<RoomDTO> findUnitByPage(@Param("unitCode") String unitCode,@Param("roomType") String roomType,
                                     @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

        @Select("SELECT count(*)\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_unit.unit_code = #{unitCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1")
        int findUnitNum(@Param("unitCode") String unitCode,@Param("roomType") String roomType);


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
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.idt,\n" +
                "regional_room.udt\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_room.room_code = #{roomCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1 limit #{pageNo},#{pageSize}")
        List<RoomDTO> findRoomByPage(@Param("roomCode") String roomCode,@Param("roomType") String roomType,
                                     @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

        @Select("SELECT count(*)\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_room.room_code = #{roomCode} AND room_type = #{roomType} and\n" +
                "regional_room.room_status = 1")
        int findRoomNum(@Param("roomCode") String roomCode,@Param("roomType") String roomType);


        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_room.room_code = #{roomCode} and room_status = 1 order by room_type")
        List<RoomInfoDTO> findRoom(@Param("roomCode") String roomCode);

        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_unit.unit_code = #{unitCode} and room_status = 1 order by room_type")
        List<RoomInfoDTO> findUnit(@Param("unitCode") String unitCode);

        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_building.building_code = #{buildingCode} and room_status = 1 order by room_type")
        List<RoomInfoDTO> findBuilding(@Param("buildingCode") String buildingCode);

        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_region.region_code = #{regionCode} and room_status = 1 order by room_type")
        List<RoomInfoDTO> findRegion(@Param("regionCode") String regionCode);

        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_village.village_code = #{regionalCode} and room_status = 1 order by room_type")
        List<RoomInfoDTO> findVillage(@Param("regionalCode") String regionalCode);




/**业主/租客信息导出
 * */
    @Select("SELECT \n" +
        "regional_village.village_name,\n" +
        "regional_region.region_name,\n" +
        "regional_building.building_name,\n" +
        "regional_unit.unit_name,\n" +
        "regional_room.floor_code,\n" +
        "regional_room.room_code,\n" +
        "regional_room.room_type,\n" +
        "regional_room.room_size,\n" +
        "regional_room.room_status,\n" +
        "regional_room.renovation_start_time,\n" +
        "regional_room.renovation_status,\n" +
        "regional_room.start_billing_time,\n" +
        "regional_room.real_recycling_home_time,\n" +
        "regional_room.renovation_deadline\n" +
        "FROM\n" +
        "regional_room\n" +
        "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
        "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
        "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
        "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
        "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
        "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
        "WHERE\n" +
        "regional_village.village_code = #{villageCode} and room_status = 2 and room_customer.identity = #{identity} GROUP BY regional_room.room_code order by room_type")
        List<RoomInfoDTO> findVillageByIdentity(@Param("villageCode") String villageCode,@Param("identity") Integer identity);


        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
                "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_region.region_code = #{regionCode} and room_status = 2 and room_customer.identity =#{identity} GROUP BY regional_room.room_code order by room_type")
        List<RoomInfoDTO> findRegionByIdentity(@Param("regionCode") String regionCode,@Param("identity") Integer identity);



        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
                "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_building.building_code = #{buildingCode} and room_status = 2 and room_customer.identity =#{identity} order by room_type")
        List<RoomInfoDTO> findBuildingByIdentity(@Param("buildingCode") String buildingCode,@Param("identity") Integer identity);


        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
                "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_unit.unit_code = #{unitCode} and room_status = 2 and room_customer.identity =#{identity} GROUP BY regional_room.room_code order by room_type")
        List<RoomInfoDTO> findUnitByIdentity(@Param("unitCode") String unitCode,@Param("identity") Integer identity);


        @Select("SELECT \n" +
                "regional_village.village_name,\n" +
                "regional_region.region_name,\n" +
                "regional_building.building_name,\n" +
                "regional_unit.unit_name,\n" +
                "regional_room.floor_code,\n" +
                "regional_room.room_code,\n" +
                "regional_room.room_type,\n" +
                "regional_room.room_size,\n" +
                "regional_room.room_status,\n" +
                "regional_room.renovation_start_time,\n" +
                "regional_room.renovation_status,\n" +
                "regional_room.start_billing_time,\n" +
                "regional_room.real_recycling_home_time,\n" +
                "regional_room.renovation_deadline\n" +
                "FROM\n" +
                "regional_room\n" +
                "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
                "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
                "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
                "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
                "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
                "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
                "WHERE\n" +
                "regional_room.room_code = #{roomCode} and room_status = 2 and room_customer.identity =#{identity} GROUP BY regional_room.room_code order by room_type")
        List<RoomInfoDTO> findRoomByIdentity(@Param("roomCode") String roomCode,@Param("identity") Integer identity);

}
