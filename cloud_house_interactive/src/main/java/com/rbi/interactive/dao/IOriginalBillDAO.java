package com.rbi.interactive.dao;

import com.rbi.interactive.entity.OriginalBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IOriginalBillDAO {

    @Select("SELECT * FROM original_bill WHERE due_time>#{dueTime} and " +
            "organization_id = #{organizationId} and charge_type in ('1','5','6') ORDER BY order_id,customer_user_id limit ${pageNo},${pageSize}")
    List<OriginalBillDO> findPrePaymentByPageAndUserIdDesc(@Param("dueTime") String dueTime,
                                                           @Param("organizationId") String organizationId,
                                                           @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT count(*) FROM original_bill WHERE due_time>#{dueTime} and " +
            "organization_id = #{organizationId}")
    Integer findPrePaymentByPageCount(@Param("dueTime") String dueTime,@Param("organizationId") String organizationId);

    @Select("SELECT SUM(actual_money_collection) FROM bill_detailed,bill WHERE bill.order_id=bill_detailed.order_id AND room_code = #{roomCode} AND charge_type IN (1,2,3) " +
            "AND bill.organization_id = #{organizationId} AND #{startTime}<idt AND idt<#{endTime}")
    Double findAllByRoomCodeAndChargeStatusAndOrganizationIdAnd(@Param("roomCode") String roomCode,
                                                                @Param("organizationId") String organizationId,
                                                                @Param("startTime") String startTime,
                                                                @Param("endTime") String endTime);

}
