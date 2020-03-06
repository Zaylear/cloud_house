package com.rbi.wx.wechatpay.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IBillDAO {

    @Select("SELECT COUNT(*) FROM bill WHERE organization_id = #{organizationId} AND order_id LIKE '${time}%'")
    Integer findOrderIdsByorganizationId(@Param("organizationId") String organizationId, @Param("time") String time);
}
