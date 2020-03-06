package com.rbi.interactive.dao;

import com.rbi.interactive.entity.dto.RefundAndApplicationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRefundApplicationDAO {

    @Select("SELECT ra.id,ra.order_id,ra.refundable_amount,ra.actual_money_collection," +
            "ra.mortgage_amount,ra.transfer_card_amount,ra.deduction_property_fee,ra.audit_status," +
            "ra.organization_id,ra.remark,ra.idt,ra.udt,toll_collector_id,payer_phone," +
            "payer_name,charge_type,payment_method,refund_status,invalid_state,village_code," +
            "village_name,region_code,region_name,building_code,building_name,unit_code," +
            "unit_name,customer_user_id,surname,mobile_phone,room_code,room_size,organization_name," +
            "charge_code,charge_name,reason_for_deduction,charge_unit,start_time,due_time," +
            "delay_time,delay_reason,person_liable,person_liable_phone,responsible_agencies " +
            "FROM refund_application AS ra INNER JOIN refund_history AS rh ON ra.order_id=" +
            "rh.order_id WHERE ra.organization_id = #{organizationId} and invalid_state = 0 LIMIT ${pageNo},${pageSize}")
    List<RefundAndApplicationDTO> findByPage(@Param("organizationId") String organizationId,
                                                    @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT count(*) " +
            "FROM refund_application AS ra INNER JOIN refund_history AS rh ON ra.order_id=" +
            "rh.order_id WHERE ra.organization_id = #{organizationId} and invalid_state = 0")
    Integer findByPageCount(@Param("organizationId") String organizationId);

    @Select("SELECT ra.id,ra.order_id,ra.refundable_amount,ra.actual_money_collection," +
            "ra.mortgage_amount,ra.transfer_card_amount,ra.deduction_property_fee,ra.audit_status," +
            "ra.organization_id,ra.remark,ra.idt,ra.udt,toll_collector_id,payer_phone," +
            "payer_name,charge_type,payment_method,refund_status,invalid_state,village_code," +
            "village_name,region_code,region_name,building_code,building_name,unit_code," +
            "unit_name,customer_user_id,surname,mobile_phone,room_code,room_size,organization_name," +
            "charge_code,charge_name,reason_for_deduction,charge_unit,start_time,due_time," +
            "delay_time,delay_reason,person_liable,person_liable_phone,responsible_agencies " +
            "FROM refund_application AS ra INNER JOIN refund_history AS rh ON ra.order_id=" +
            "rh.order_id WHERE ra.organization_id = #{organizationId} AND audit_status = " +
            "#{auditStatus} and invalid_state = 0 LIMIT ${pageNo},${pageSize}")
    List<RefundAndApplicationDTO> findByPageAndAudit(@Param("organizationId") String organizationId,
                                                            @Param("auditStatus") int auditStatus,
                                                    @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT count(*) " +
            "FROM refund_application AS ra INNER JOIN refund_history AS rh ON ra.order_id=" +
            "rh.order_id WHERE ra.organization_id = #{organizationId} AND audit_status = " +
            "#{auditStatus} and invalid_state = 0")
    Integer findByPageAndAuditCount(@Param("organizationId") String organizationId,
                                                                 @Param("auditStatus") int auditStatus);

}
