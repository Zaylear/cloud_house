package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IParkingSpaceManagerDAO {

    @Select("SELECT MAX(due_time) FROM parking_space_bill WHERE organization_id = " +
            "#{organizationId} AND room_code = #{roomCode} AND parking_space_code = " +
            "#{parkingSpaceCode} AND mobile_phone = #{mobilePhone}")
    String findParkingSpacceMaxDueTime(@Param("organizationId") String organizationId,
                                              @Param("roomCode") String roomCode,
                                              @Param("parkingSpaceCode") String parkingSpaceCode,
                                              @Param("mobilePhone") String mobilePhone);


    @Select("SELECT * FROM parking_space_bill WHERE organization_id = #{organizationId} ORDER BY idt DESC LIMIT ${pageNo},${pageSize}")
    List<ParkingSpaceBillDO> findByPage(@Param("organizationId") String organizationId, int pageNo, int pageSize);

    @Select("SELECT count(*) FROM parking_space_bill WHERE organization_id = #{organizationId}")
    int findByPageCount(@Param("organizationId") String organizationId);
}
