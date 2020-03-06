package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RoomAndChargeItemsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRoomAndChargeItemsDAO {

    @Select("SELECT * FROM room_charge_items WHERE organization_id = #{organizationId} " +
            "ORDER BY idt DESC LIMIT ${pageNo},${pageSize}")
    public List<RoomAndChargeItemsDO> findByPage(@Param("organizationId") String organizationId,
                                                 @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);


    @Select("SELECT count(*) FROM room_charge_items WHERE organization_id = #{organizationId}")
    public Integer findByPageCount(@Param("organizationId") String organizationId);
}
