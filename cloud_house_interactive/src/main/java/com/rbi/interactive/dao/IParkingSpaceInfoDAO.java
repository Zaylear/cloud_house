package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceInfoDO;
import com.rbi.interactive.entity.dto.RegionCodeDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IParkingSpaceInfoDAO {

    @Select("select count(*) from regional_village where village_name = #{villageName}")
    int findVillageNumByVillageName(@Param("villageName")String villageName);

    @Select("select count(*) from regional_region where region_name = #{regionName} and village_code = #{villageCode}")
    int findRegionNumByRegionName(@Param("regionName")String regionName,@Param("villageCode")String villageCode);

    @Update("update parking_space_info set parking_space_nature = 1 where parking_space_code = #{parkingSpaceCode}")
    void updateNature(@Param("parkingSpaceCode")String parkingSpaceCode);

    @Update("update parking_space_info set parking_space_nature = 2 where parking_space_code = #{parkingSpaceCode}")
    void updateRentNature(@Param("parkingSpaceCode")String parkingSpaceCode);



    @Select("SELECT * FROM parking_space_info WHERE village_code = #{villageCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceInfoDO> findVillage(@Param("villageCode") String villageCode,
                                         @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_info WHERE village_code = #{villageCode}")
    Integer findVillageNum(@Param("villageCode") String villageCode);


    @Select("SELECT * FROM parking_space_info WHERE region_code = #{regionCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceInfoDO> findRegion(@Param("regionCode") String regionCode,
                                         @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_info WHERE region_code = #{regionCode}")
    Integer findRegionNum(@Param("regionCode") String regionCode);


    @Select("SELECT * FROM parking_space_info WHERE building_code = #{buildingCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceInfoDO> findBuilding(@Param("buildingCode") String buildingCode,
                                         @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_info WHERE building_code = #{buildingCode}")
    Integer findBuildingNum(@Param("buildingCode") String buildingCode);


    @Select("SELECT * FROM parking_space_info WHERE parking_space_code = #{parkingSpaceCode} LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceInfoDO> findParkingSpaceCode(@Param("parkingSpaceCode") String parkingSpaceCode,
                                         @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM parking_space_info WHERE parking_space_code = #{parkingSpaceCode}")
    Integer findParkingCodeNum(@Param("parkingSpaceCode") String parkingSpaceCode);


    @Update("UPDATE parking_space_statistics SET region_number = region_number+1 where region_code = #{regionCode}")
    void regionNuberAdd(@Param("regionCode") String regionCode);

    @Update("UPDATE parking_space_statistics SET region_number = region_number-1 where region_code = #{regionCode}")
    void regionNuberLess(@Param("regionCode") String regionCode);

    @Update("UPDATE parking_space_statistics SET region_proper_number = region_proper_number+1 where region_code = #{regionCode}")
    void properNuberAdd(@Param("regionCode") String regionCode);

    @Update("UPDATE parking_space_statistics SET region_proper_number = region_proper_number-1 where region_code = #{regionCode}")
    void properNuberLess(@Param("regionCode") String regionCode);

    @Update("UPDATE parking_space_statistics SET region_rent_number = region_rent_number+1 where region_code = #{regionCode}")
    void rentNuberAdd(@Param("regionCode") String regionCode);

    @Update("UPDATE parking_space_statistics SET region_rent_number = region_rent_number-1 where region_code = #{regionCode}")
    void rentNuberLess(@Param("regionCode") String regionCode);

    @Select("select parking_space_nature from parking_space_info where parking_space_code = #{parkingSpaceCode}")
    String findNatureByCode(@Param("parkingSpaceCode")String parkingSpaceCode);

    @Select("select count(*) from parking_space_statistics where region_code = #{regionCode}")
    Integer findNumByRegionCode(@Param("regionCode") String regionCode);

    @Insert("insert into parking_space_statistics(organization_id,region_code) values (#{organizationId},#{regionCode})")
    void addRegionCode(@Param("organizationId") String organizationId,@Param("regionCode") String regionCode);

}
