package com.rbi.interactive.dao.report;

import com.rbi.interactive.entity.CollectionSummaryDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ICostAmountDAO {

    /**
     * 查询业主信息
     * @return
     */
    @Select("SELECT rv.organization_id,rv.organization_name,rv.village_code,rv.village_name,rr.region_code,rr.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rrm.room_code,rrm.room_type,rrm.room_size,GROUP_CONCAT(sci.surname SEPARATOR ';') AS surname,GROUP_CONCAT(sci.mobile_phone SEPARATOR ';') AS mobile_phone,GROUP_CONCAT(sci.id_number SEPARATOR ';') AS id_number FROM regional_village AS rv INNER JOIN regional_region AS rr ON rv.village_code=rr.village_code INNER JOIN regional_building AS rb ON rr.region_code=rb.region_code INNER JOIN regional_unit AS ru ON rb.building_code=ru.building_code INNER JOIN regional_room AS rrm ON ru.unit_code=rrm.unit_code INNER JOIN room_customer AS rc ON rrm.room_code=rc.room_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id GROUP BY rv.organization_id,rrm.room_code")
    List<CollectionSummaryDO> queryRoomAndCustomerInfo();



    @Select("SELECT SUM(bd.actual_money_collection) " +
            "FROM bill AS b INNER JOIN bill_detailed AS bd ON b.order_id=bd.order_id " +
            "WHERE b.invalid_state = 0 AND state_of_arrears = 0 AND bd.charge_type = #{chargeType} AND " +
            "b.real_generation_time LIKE #{realGenerationTime} AND b.organization_id = #{organizationId}")
    Double queryActualMoneyCollectionCostSumByChargeTypeAndRealGenerationTimeAndOrganizationId(@Param("chargeType") String chargeType,
                                                                                                      @Param("realGenerationTime") String realGenerationTime,
                                                                                                      @Param("organizationId") String organizationId);



}
