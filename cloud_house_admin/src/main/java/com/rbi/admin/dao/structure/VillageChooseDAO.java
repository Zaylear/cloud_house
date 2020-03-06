package com.rbi.admin.dao.structure;

import com.rbi.admin.entity.edo.RoomTreeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VillageChooseDAO {



    @Select("SELECT\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code\n" +
            "FROM\n" +
            "regional_village,regional_region,regional_building,regional_unit,regional_room\n" +
            "WHERE\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code and\n" +
            "regional_unit.unit_code = regional_room.unit_code AND regional_room.room_type =1 AND\n" +
            "regional_village.organization_id = #{organizationId} group by regional_village.village_code,\n" +
            "\tregional_region.region_code,\n" +
            "\tregional_building.building_code,\n" +
            "\tregional_unit.unit_code,\n" +
            "\tregional_room.room_code")
    List<RoomTreeDTO> findAllRoom(@Param("organizationId")String organizationId);


    @Select("SELECT\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code\n" +
            "FROM\n" +
            "regional_village,regional_region,regional_building,regional_unit,regional_room\n" +
            "WHERE\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code AND\n" +
            "regional_unit.unit_code = regional_room.unit_code AND regional_room.room_type =1 AND\n" +
            "regional_village.organization_id = #{organizationId} group by regional_village.village_code,\n" +
            "\tregional_region.region_code,\n" +
            "\tregional_building.building_code,\n" +
            "\tregional_unit.unit_code,\n" +
            "\tregional_room.room_code")
    List<RoomTreeDTO> findRoom(@Param("organizationId")String organizationId);


    @Select("SELECT\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code\n" +
            "FROM\n" +
            "regional_village,regional_region,regional_building,regional_unit,regional_room\n" +
            "WHERE\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code AND\n" +
            "regional_unit.unit_code = regional_room.unit_code AND regional_room.room_type =3 AND\n" +
            "regional_village.organization_id = #{organizationId} group by regional_village.village_code,\n" +
            "\tregional_region.region_code,\n" +
            "\tregional_building.building_code,\n" +
            "\tregional_unit.unit_code,\n" +
            "\tregional_room.room_code")
    List<RoomTreeDTO> findBusinese(@Param("organizationId")String organizationId);


































    //    @Select("select village_code as code, village_name as name from regional_village where organization_id = #{organizationId}")
//    public List<VillageChooseDTO> findVillage(@Param("organizationId") String organizationId);
//
//    @Select("select region_code as code, region_name as name ,village_code as pid from regional_region where village_code in (${villageCodes})")
//    public List<VillageChooseDTO> findRegion(@Param("villageCodes")String villageCodes);
//
//    @Select("select building_code as code, building_name as name,region_code as pid from regional_building where region_code in (${regionCodes})")
//    public List<VillageChooseDTO> findBuilding(@Param("regionCodes")String regionCodes);
//
//    @Select("select unit_code as code, unit_name as name,building_code as pid from regional_unit where building_code in (${buildingCodes})")
//    public List<VillageChooseDTO> findUnit(@Param("buildingCodes")String buildingCodes);
//
//    @Select("select room_code as code,room_code as name,unit_code as pid from regional_room where unit_code in (${unitCodes})")
//    public List<VillageChooseDTO> findRoom(@Param("unitCodes")String unitCodes);



}
