package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RoomAndCouponDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRoomAndCouponDAO {

    @Select("SELECT COUNT(*) FROM room_coupon WHERE organization_id = #{organizationId}")
    public Integer findByOrganizationIdCount(@Param("organizationId") String organizationId);

    @Select("SELECT id,balance_amount,region_code,region_name,building_code,building_name,coupon_code,coupon_name," +
            "effective_time,end_time,idt,mobile_phone,money,coupon_type,remarks,organization_id,organization_name," +
            "past_due,property_fee,room_code,start_time,surname,udt,unit_code,unit_name," +
            "usage_state,village_code,village_name,audit_status FROM room_coupon WHERE organization_id = " +
            "#{organizationId} ORDER BY idt LIMIT ${pageNo},${pageSize}")
    public List<RoomAndCouponDO> findByOrganizationIdOrderByIdtDESC(
            @Param("organizationId") String organizationId,
            @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM room_coupon WHERE organization_id = #{organizationId} AND audit_status = #{auditStatus}")
    public Integer findByOrganizationIdAuditCount(@Param("organizationId") String organizationId,@Param("auditStatus") int auditStatus);

    @Select("SELECT id,balance_amount,region_code,region_name,building_code,building_name,coupon_code,coupon_name," +
            "effective_time,end_time,idt,mobile_phone,money,coupon_type,remarks,organization_id,organization_name," +
            "past_due,property_fee,room_code,start_time,surname,udt,unit_code,unit_name," +
            "usage_state,village_code,village_name,audit_status FROM room_coupon WHERE organization_id = " +
            "#{organizationId} AND audit_status = #{auditStatus} ORDER BY idt LIMIT ${pageNo},${pageSize}")
    public List<RoomAndCouponDO> findByOrganizationIdAuditOrderByIdtDESC(
            @Param("organizationId") String organizationId,@Param("auditStatus") int auditStatus,
            @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

}
