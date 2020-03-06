package com.rbi.interactive.dao;

import com.rbi.interactive.entity.BillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IBillDAO {

    @Select("SELECT COUNT(*) FROM bill WHERE organization_id = #{organizationId} AND order_id LIKE '${time}%'")
    Integer findOrderIdsByorganizationId(@Param("organizationId") String organizationId,@Param("time") String time);

    @Select("SELECT id,actual_total_money_collection,remark,region_code,region_name,surplus," +
            "amount_total_receivable,building_code,building_name," +
            "detailed,idt,invalid_state,mobile_phone,order_id,organization_id,organization_name,payer_name," +
            "payer_phone,payment_method,preferential_total_amount,refund_status,room_code,room_size," +
            "state_of_arrears,surname,toll_collector_id,udt,unit_code,unit_name,user_id,village_code," +
            "village_name FROM bill WHERE organization_id = #{organizationId} ORDER BY idt DESC LIMIT ${pageNo},${pageSize}")
    List<BillDO> findBillPage(@Param("organizationId") String organizationId,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM bill WHERE organization_id = #{organizationId}")
    Integer findBillCount(@Param("organizationId") String organizationId);

    @Select("SELECT\n" +
            "\trc.charge_code\n" +
            "FROM\n" +
            "\trefund_application AS ra\n" +
            "INNER JOIN refund_history AS rh ON ra.order_id = rh.order_id\n" +
            "INNER JOIN room_charge_items AS rc ON rh.room_code = rc.room_code\n" +
            "INNER JOIN charge_item AS ri ON rc.charge_code = ri.charge_code\n" +
            "WHERE\n" +
            "\trh.room_code = #{roomCode}\n" +
            "AND ra.id = #{id}\n" +
            "AND ri.charge_type IN (1, 2, 3)")
    String findChargeCodeByRoomCode(@Param("roomCode") String roomCode,@Param("id") Integer id);

}
