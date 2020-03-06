package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RefundHistoryDO;
import com.rbi.interactive.entity.dto.RefundAndApplicationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface IRefundDAO {

    @Select("SELECT * FROM refund_history WHERE organization_id = #{organizationId} AND " +
            "invalid_state = 0 LIMIT ${pageNo},${pageSize}")
    List<RefundHistoryDO> findByPage(@Param("organizationId") String organizationId,
                                            @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM refund_history WHERE organization_id = #{organizationId} AND invalid_state = 0")
    Integer findByPageCount(@Param("organizationId") String organizationId);

    @Select("SELECT * FROM refund_history WHERE organization_id = #{organizationId} AND " +
            "invalid_state = 0 AND refund_status = #{refundStatus} LIMIT ${pageNo},${pageSize}")
    List<RefundHistoryDO> findByPageAndRefundStatus(@Param("organizationId") String organizationId,
                                                           @Param("refundStatus") Integer refundStatus,
                                            @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM refund_history WHERE organization_id = #{organizationId} AND " +
            "invalid_state = 0 AND refund_status = #{refundStatus}")
    Integer findByPageAndRefundStatusCount(@Param("organizationId") String organizationId,
                                                                @Param("refundStatus") Integer refundStatus);

    @Update("update refund_history set invalid_state = 1 where id in (${ids})")
    void deleteData(@Param("ids") String ids);


    @Select("SELECT ra.id,ra.order_id,ra.refundable_amount,ra.actual_money_collection,\n" +
            "ra.mortgage_amount,ra.transfer_card_amount,ra.deduction_property_fee,ra.audit_status,\n" +
            "ra.organization_id,ra.remark,ra.idt,ra.udt,toll_collector_id,payer_phone,\n" +
            "payer_name,payment_type,payment_method,refund_status,invalid_state,village_code,\n" +
            "village_name,region_code,region_name,building_code,building_name,unit_code,\n" +
            "unit_name,user_id,surname,mobile_phone,room_code,room_size,organization_name,\n" +
            "charge_code,charge_name,reason_for_deduction,charge_unit,start_time,due_time,\n" +
            "delay_time,delay_reason,person_liable,person_liable_phone,responsible_agencies \n" +
            "FROM refund_application AS ra INNER JOIN refund_history AS rh ON ra.order_id=\n" +
            "rh.order_id WHERE ra.organization_id = #{organizationId} and invalid_state = 0 AND " +
            "refund_status = #{refundStatus} LIMIT ${pageNo},${pageSize}")
    List<RefundAndApplicationDTO> findRefundInfo(@Param("organizationId") String organizationId,
                                                 @Param("refundStatus") Integer refundStatus,
                                                 @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) \n" +
            "FROM refund_application AS ra INNER JOIN refund_history AS rh ON ra.order_id=\n" +
            "rh.order_id WHERE ra.organization_id = #{organizationId} and invalid_state = 0 AND " +
            "refund_status = #{refundStatus}")
    Integer findRefundInfoCount(@Param("organizationId") String organizationId,
                                                 @Param("refundStatus") Integer refundStatus,
                                                 @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

}
