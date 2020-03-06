package com.rbi.admin.dao.connect;


import com.rbi.admin.entity.edo.RoomDO;
import com.rbi.admin.entity.dto.RoomDTO;
import com.rbi.admin.entity.dto.RoomUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;


@Mapper
public interface RoomDAO{
    @Select(value = "SELECT id,room_code as roomCode,room_size as roomSize,room_type as roomType,room_status as roomStatus," +
            "contract_deadline as contractDeadline,renovation_status as renovationStatus,unit_code as unitCode,idt,udt " +
            "FROM regional_room WHERE room_code IN (${roomCodes})")
    List<RoomDO> findByRoomCodes(@Param("roomCodes") String roomCodes);

    @Select("SELECT regional_room.room_code as roomCode,regional_room.room_size as roomSize,\n" +
            "sys_user_info.user_id as userId,sys_user_info.username,\n" +
            "regional_unit.unit_name as unitName,regional_building.building_name as buildingName,\n" +
            "regional_region.region_name as regionName,regional_village.village_name as villageName,\n" +
            "regional_district.district_name as districtName\n" +
            "FROM\n" +
            "regional_room INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "INNER JOIN regional_district ON regional_village.district_code = regional_district.district_code,\n" +
            "sys_user_info\n" +
            "where regional_room.room_code=#{roomCode} and sys_user_info.user_id = #{userId}")
    RoomUserDTO findRoomUser(@Param("roomCode") String roomCode,@Param("userId") String userId);

    @Select("SELECT \n" +
            "v.organization_id as organizationId,o.organization_name as organizationName,v.village_code as villageCode," +
            "v.village_name as villageName,rg.region_code as regionCode,\n" +
            "rg.region_name as regionName,b.building_code as buildingCode,b.building_name as buildingName,\n" +
            "u.unit_code as unitCode,u.unit_name AS unitName,r.room_code as roomCode,r.room_size as roomSize,\n" +
            "r.room_type as roomType,r.room_status as roomStatus,r.renovation_status as renovationStatus,\n" +
            "r.delivery_date as deliveryDate,r.contract_deadline as contractDeadline,r.idt,r.udt\n" +
            "FROM regional_village AS v,regional_region AS rg,regional_building AS b,\n" +
            "regional_unit AS u,regional_room AS r,organization AS o\n" +
            "\n" +
            "WHERE v.village_code =rg.village_code AND v.organization_id = o.organization_id\n" +
            "AND rg.region_code = b.region_code \n" +
            "AND b.building_code = u.building_code AND u.unit_code = r.unit_code \n" +
            "AND r.room_code IN (${roomCodes})")
    List<RoomDTO> findMessageByRoomCodes(@Param("roomCodes") String roomCodes);

}
