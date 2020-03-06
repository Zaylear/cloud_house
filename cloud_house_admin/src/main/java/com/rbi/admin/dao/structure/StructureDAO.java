package com.rbi.admin.dao.structure;

import com.rbi.admin.entity.dto.structure.*;
import com.rbi.admin.entity.edo.FloorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StructureDAO {

    @Select("SELECT organization_id,organization_name FROM organization where pid=0")
    public List<OrganizationStructureDTO> findOrganizationId2();

    @Select("SELECT organization_id,organization_name FROM organization where pid=#{pid}")
    public List<OrganizationStructureDTO> findSonOrganizationId(@Param("pid") String pid);

    @Select("SELECT dept_id,dept_name FROM department where pid=0")
    public List<DepartmentDTO> findDepartmentId();

    @Select("SELECT dept_id,dept_name FROM department where pid=#{pid}")
    public List<DepartmentDTO> findSonDepartmentId(@Param("pid") String pid);



    @Select("select village_code,village_name from regional_village where organization_id =#{organizationId}")
    public List<VillageStructureDTO> findByOrganizationId(@Param("organizationId")String organizationId);

    @Select("select village_code from regional_village where organization_id =#{organizationId} and village_code = #{villageCode}")
    public List<VillageStructureDTO> findVillageCode(@Param("organizationId")String organizationId,@Param("villageCode")String villageCode);


    @Select("select village_code,village_name from regional_village")
    public List<VillageStructureDTO> findVillage();

    @Select("SELECT region_code,region_name FROM regional_region WHERE village_code =#{villageCode}")
    public List<RegionStructureDTO> findByVillaegCode(@Param("villageCode")String villageCode);

    @Select("SELECT region_code,region_name FROM regional_region")
    public List<RegionStructureDTO> findRegion();

    @Select("SELECT region_code FROM regional_region where region_code=#{regionCode}")
    public List<RegionStructureDTO> findRegion2(@Param("regionCode")String regionCode);


    @Select("SELECT building_code,building_name FROM regional_building where region_code = #{regionCode}")
    public List<BuildingStructureDTO> findByRegionCode(@Param("regionCode")String regionCode);

    @Select("SELECT building_code,building_name FROM regional_building")
    public List<BuildingStructureDTO> findBuiding();

    @Select("SELECT building_code FROM regional_building where building_code=#{buildingCode}")
    public List<BuildingStructureDTO> findBuiding2(@Param("buildingCode")String buildingCode);


    @Select("SELECT unit_code,unit_name FROM regional_unit WHERE building_code = #{buildingCode}")
    public List<UnitStructureDTO> findByBuidingCode(@Param("buildingCode")String buildingCode);

    @Select("SELECT unit_code,unit_name FROM regional_unit")
    public List<UnitStructureDTO> findUnit();

    @Select("SELECT unit_code FROM regional_unit where unit_code = #{unitCode}")
    public List<UnitStructureDTO> findUnit2(@Param("unitCode")String unitCode);

    @Select("SELECT floor_code FROM regional_floor where floor_code = #{floorCode}")
    public List<FloorDO> findFloor2(@Param("floorCode")String floorCode);





    @Select("SELECT room_code FROM regional_room")
    public List<RoomStructureDTO> findRoom();

    @Select("SELECT room_code FROM regional_room where room_code=#{roomCode}")
    public List<RoomStructureDTO> findRoom2(@Param("roomCode")String roomCode);

    @Select("SELECT room_code FROM regional_room where unit_code = #{unitCode}")
    public List<RoomStructureDTO> findByUnitCode(@Param("unitCode")String unitCode);



    @Select("select organization_id from sys_user_info where user_id =#{userId}")
    public String findOrganizationId(@Param("userId") String userId);

    @Select("select organization.organization_name from sys_user_info,organization where " +
            "sys_user_info.organization_id =organization.organization_id and user_id =#{userId}")
    public String findOrganizationName(@Param("userId") String userId);

    @Select("SELECT mobile_phone FROM sys_customer_info where enabled = 1")
    public List<CustomerStructureDTO> findPhone();

    @Select("SELECT mobile_phone FROM sys_customer_info where enabled = 1 and mobile_phone=#{mobilePhone}")
    public List<CustomerStructureDTO> findPhone2(@Param("mobilePhone") String mobilePhone);

    @Select("SELECT real_name FROM sys_user_info where user_id = #{userId}")
    public String findNameByUserId(@Param("userId")String userId);


    @Select("SELECT province_code,province_name FROM regional_province")
    public List<ProvinceDTO> chooseProvince();

    @Select("SELECT city_code,city_name FROM regional_city where province_code = #{provinceCode}")
    public List<CityDTO> choozeCity(@Param("provinceCode")String provinceCode);

    @Select("SELECT district_code,district_name FROM regional_district where city_code = #{cityCode}")
    public List<DistrictDTO> choozeDistrict(@Param("cityCode")String cityCode);


    @Select("select divison_code from regional_divison")
    List<DivisonCodeDTO> findDivisonCode();

    @Select("select divison_code from regional_divison where id != #{id}")
    List<DivisonCodeDTO> findDivisonCodeNotId(@Param("id")Integer id);



}
