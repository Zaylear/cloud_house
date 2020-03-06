package com.rbi.interactive.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ILiquidatedDamagesDAO {

    @Select("SELECT MAX(due_time) FROM liquidated_damages WHERE room_code = #{roomCode} and organization_id = #{organizationId}")
    String findLiquidatedDamagesDueTime(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);

    @Select("SELECT COUNT(*) FROM (SELECT order_id FROM liquidated_damages " +
            "WHERE organization_id = #{organizationId} AND order_id LIKE '%${time}%' GROUP BY order_id) AS order_id")
    Integer orderIdCount(@Param("organizationId") String organizationId,@Param("time") String time);

}
