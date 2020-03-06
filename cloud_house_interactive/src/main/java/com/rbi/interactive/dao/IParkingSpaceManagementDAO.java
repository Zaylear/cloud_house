package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import com.rbi.interactive.entity.dto.ParkingSpaceManagementDTO;
import com.rbi.interactive.entity.dto.RoomDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IParkingSpaceManagementDAO {
    @Select("select village_code,village_name,region_code,region_name,building_code,building_name,parking_space_code " +
            "from parking_space_info where organization_id = #{organizationId} order by village_code,region_code,building_code,parking_space_code")
    List<ParkingSpaceManagementDTO> findAllByOrganizationId(@Param("organizationId")String organizationId);

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
            "regional_room.room_size\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_room.room_code = #{roomCode}")
    RoomDTO findRoomByRoomCode(@Param("roomCode") String roomCode);

    @Select("SELECT count(*) " +
            "from parking_space_info " +
            "where parking_space_code = #{parkingSpaceCode}")
    int findParkingSpaceCode2(@Param("parkingSpaceCode") String parkingSpaceCode);

    @Delete("delete from parking_space_management where parking_space_management_id = #{parkingSpaceManagementId}")
    void deleteById(@Param("parkingSpaceManagementId") Integer parkingSpaceManagementId);


    @Select("SELECT * FROM parking_space_management WHERE village_code = #{villageCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceManagementDO> findVillage(@Param("villageCode") String villageCode,
                                         @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_management WHERE village_code = #{villageCode}")
    Integer findVillageNum(@Param("villageCode") String villageCode);


    @Select("SELECT * FROM parking_space_management WHERE region_code = #{regionCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceManagementDO> findRegion(@Param("regionCode") String regionCode,
                                        @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_management WHERE region_code = #{regionCode}")
    Integer findRegionNum(@Param("regionCode") String regionCode);


    @Select("SELECT * FROM parking_space_management WHERE building_code = #{buildingCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceManagementDO> findBuilding(@Param("buildingCode") String buildingCode,
                                          @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_management WHERE building_code = #{buildingCode}")
    Integer findBuildingNum(@Param("buildingCode") String buildingCode);


    @Select("SELECT * FROM parking_space_management WHERE parking_space_code = #{parkingSpaceCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceManagementDO> findParkingSpaceCode(@Param("parkingSpaceCode") String parkingSpaceCode,
                                                        @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_management WHERE parking_space_code = #{parkingSpaceCode}")
    Integer findParkingCodeNum(@Param("parkingSpaceCode") String parkingSpaceCode);


    @Insert("insert into sys_customer_info (user_id,surname,id_number,sex,mobile_phone)" +
            "values(#{userId},#{surname},#{idNumber},#{sex},#{mobilePhone})")
    void addCustomer(@Param("userId") String userId, @Param("surname") String surname,@Param("idNumber") String idNumber,
                     @Param("sex") Integer sex,@Param("mobilePhone") String mobilePhone);

    @Select("SELECT count(*) FROM parking_space_management WHERE organization_id = #{organizationId} and DATEDIFF(idt,NOW())=0")
    int countTodayNum(@Param("organizationId") String organizationId);
}
