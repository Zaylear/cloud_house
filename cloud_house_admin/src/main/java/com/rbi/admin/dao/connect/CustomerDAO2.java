package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.edo.CustomerInfoDO;
import com.rbi.admin.entity.dto.OwnerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerDAO2 {

    @Select("SELECT id,user_id as userId,password,salt,sex,surname,mobile_phone as mobilePhone," +
           "portrait_path as portraitPath,enabled,login_status as loginStatus," +
            "idt,udt FROM sys_customer_info WHERE user_id IN (${userId})")
    public List<CustomerInfoDO> findByUserIds(@Param("userId") String userId);


    @Select("SELECT\n" +
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
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "WHERE\n" +
            "regional_village.village_code = #{villageCode} and room_customer.identity =3 and room_customer.logged_off_state =0 limit #{pageNo},#{pageSize}")
    public List<OwnerDTO> findVillageByPage(@Param("villageCode") String villageCode,
                                            @Param("pageNo") Integer pageNo,
                                            @Param("pageSize") Integer pageSize);
}
