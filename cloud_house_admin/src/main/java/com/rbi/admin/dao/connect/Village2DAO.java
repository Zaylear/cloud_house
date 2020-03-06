package com.rbi.admin.dao.connect;

import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Query;

@Mapper
public interface Village2DAO {
    @Insert("insert into regional_village (village_code,village_name,organization_id,organization_name," +
            "construction_area,greening_rate,public_area,district_code,district_name,idt,picture_path,enable) " +
            "values (#{villageCode},#{villageName},#{organizationId},#{organizationName}," +
            "#{constructionArea},#{greeningRate},#{publicArea},#{districtCode},#{districtName}," +
            "#{idt},#{picturePath},#{enable})")
    public void addVillage(@Param("villageCode")String villageCode, @Param("villageName")String villageName,
                      @Param("organizationId")String organizationId, @Param("organizationName")String organizationName,
                      @Param("constructionArea")Double constructionArea, @Param("greeningRate")Double greeningRate,
                      @Param("publicArea")Double publicArea, @Param("districtCode")String districtCode,
                      @Param("districtName")String districtName, @Param("idt")String idt, @Param("picturePath")String picturePath,
                      @Param("enable")Integer  enable);

    @Update("update regional_village set village_code = #{villageCode},village_name = #{villageName}," +
            "organization_id = #{organizationId},organization_name = #{organizationName}," +
            "construction_area = #{constructionArea},greening_rate = #{greeningRate}," +
            "public_area = #{publicArea},district_code = #{districtCode}," +
            "district_name = #{districtName},enable = #{enable},idt =#{idt},udt =#{udt}," +
            "picture_path = #{picturePath} where id = #{id}")
    public void update(@Param("villageCode")String villageCode, @Param("villageName")String villageName,
                           @Param("organizationId")String organizationId, @Param("organizationName")String organizationName,
                           @Param("constructionArea")Double constructionArea, @Param("greeningRate")Double greeningRate,
                           @Param("publicArea")Double publicArea, @Param("districtCode")String districtCode,
                           @Param("districtName")String districtName,@Param("enable")String enable,
                           @Param("idt")String idt, @Param("udt")String udt,
                           @Param("picturePath")String picturePath,@Param("id")Integer id);


}
