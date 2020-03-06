package com.rbi.admin.dao.other;

import com.rbi.admin.entity.dto.CouponDO2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Coupon2DAO {
    @Select( "select * from pay_coupon where organization_id = #{organizationId} Limit #{pageNo},#{pageSize}")
    public List<CouponDO2> findByPage(@Param("organizationId")String organizationId, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Select("select count(id) from pay_coupon where organization_id = #{organizationId}")
    public int findNum(@Param("organizationId") String organizationId);
}
