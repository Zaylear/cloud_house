package com.rbi.interactive.dao;

import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRoomInfoDAO {

    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code,rrn.region_name,rb.building_code,\n" +
            "rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rr.room_size,sci.user_id,sci.mobile_phone,sci.surname \n" +
            "FROM room_customer AS rc INNER JOIN regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit \n" +
            "AS ru ON rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code \n" +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village AS rv ON \n" +
            "rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.user_id = sci.user_id WHERE \n" +
            "rc.organization_id=#{organizationId} AND rc.identity='1' and rc.logged_off_state = 0 AND sci.mobile_phone = #{mobilePhone}")
    public List<HouseAndProprietorDTO> findHouseInfoByMobilePhone(@Param("organizationId") String organizationId,
                                                                  @Param("mobilePhone") String mobilePhone);

}
