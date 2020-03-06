package com.rbi.interactive.dao;

import com.alibaba.dubbo.config.annotation.Service;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.ChargeDetailDO;
import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.CostDeductionDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IChargeDAO {

    @Select("SELECT charge_item.charge_code AS chargeCode,charge_item.charge_name AS " +
            "chargeName,charge_unit AS chargeUnit,charge_standard AS chargeStandard,datedif," +
            "discount,charge_item.charge_type FROM charge_detail,charge_item,organization " +
            "WHERE charge_detail.charge_code = charge_item.charge_code AND " +
            "charge_item.organization_id = organization.organization_id AND " +
            "charge_item.charge_code = #{chargeCode} AND datedif = #{datedif} AND " +
            "charge_item.`enable`=1")
    BillDetailedDO findPropertyCharge(@Param("chargeCode") String chargeCode, @Param("datedif") int datedif);

    @Select("SELECT ci.charge_code,ci.charge_name,ci.charge_type,cd.money AS chargeStandard," +
            "cd.datedif,cd.discount FROM charge_item AS ci INNER JOIN charge_detail AS cd ON " +
            "ci.charge_code = cd.charge_code WHERE ci.charge_code = #{chargeCode} AND " +
            "#{rangeOfValues}<cd.area_max AND #{rangeOfValues}>cd.area_min")
    BillDetailedDO findByChargeCodeAndRangeOfValues(@Param("chargeCode") String chargeCode, @Param("rangeOfValues") Double rangeOfValues);

    @Select("SELECT\n" +
            "\tci.charge_code,\n" +
            "\tci.charge_name,\n" +
            "\tci.charge_type,\n" +
            "\tcd.money AS chargeStandard,\n" +
            "\tcd.datedif,\n" +
            "\tcd.discount\n" +
            "FROM\n" +
            "\tcharge_item AS ci\n" +
            "INNER JOIN charge_detail AS cd ON ci.charge_code = cd.charge_code\n" +
            "WHERE\n" +
            "\tci.charge_code = #{chargeCode}")
    List<BillDetailedDO> findDetailByChargeCode(@Param("chargeCode") String chargeCode);

    @Select("SELECT charge_code,charge_name,charge_type,charge_unit,charge_standard " +
            "FROM charge_item WHERE charge_code = #{chargeCode} AND `enable` = 1")
    BillDetailedDO findByChargeCode(@Param("chargeCode") String chargeCode);

    @Select("SELECT ci.charge_code,ci.charge_name,ci.charge_type,cd.money AS chargeStandard," +
            "cd.datedif,cd.discount FROM charge_item AS ci INNER JOIN charge_detail AS cd " +
            "ON ci.charge_code = cd.charge_code WHERE ci.charge_code = #{chargeCode} AND " +
            "cd.parking_space_place = #{parkingSpacePlace} AND cd.parking_space_type = " +
            "#{parkingSpaceType} AND ci.`enable` = 1")
    BillDetailedDO findByChargeCodeAndParkingSpaceNatureAndParkingSpaceType(
            @Param("chargeCode") String chargeCode,
            @Param("parkingSpacePlace") String parkingSpacePlace,
            @Param("parkingSpaceType") String parkingSpaceType);

    /**
     * 查询三通费抵扣费用
     * @param roomCode
     * @return
     */
    @Select("SELECT bill_detailed_id as deductionOrderId,deductible_money,deductibled_money,deduction_record," +
            "surplus_deductible_money,deductible_money,charge_type,charge_name AS deductionItem,bill_detailed.discount FROM bill_detailed " +
            "INNER JOIN bill ON bill_detailed.order_id=bill.order_id WHERE surplus_deductible_money>0 " +
            "AND bill.room_code=#{roomCode} and bill.organization_id=#{organizationId} AND charge_type IN (14,15,16)")
    List<CostDeductionDO> findThreeWayFee(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);

    /**
     * 查询退款项抵扣费用
     * @param roomCode
     * @return
     */
    @Select("SELECT\n" +
            "\tra.id AS deductionOrderId,\n" +
            "\tsurplus_deductible_money,\n" +
            "\tdeductible_money,\n" +
            "\tdeductibled_money,\n" +
            "\tdeduction_record,\n" +
            "\trh.charge_name AS deductionItem,\n" +
            "\trh.charge_type AS chargeType\n" +
            "FROM\n" +
            "\trefund_application AS ra\n" +
            "INNER JOIN refund_history AS rh ON ra.order_id = rh.order_id\n" +
            "WHERE\n" +
            "\tsurplus_deductible_money > 0\n" +
            "AND ra.audit_status = 3\n" +
            "AND rh.room_code = #{roomCode} and rh.organization_id=#{organizationId}")
    List<CostDeductionDO> findRefundFee(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);

    /**
     * 查询优惠券抵扣费用
     * @param roomCode
     * @param organizationId
     * @return
     */
    @Select("SELECT\n" +
            "\tid AS deductionOrderId,\n" +
            "\tdeductible_money,\n" +
            "\tsurplus_deductible_money,\n" +
            "\tdeduction_record,\n" +
            "\tdeductibled_money,\n" +
            "\tcoupon_name AS deductionItem\n" +
            "FROM\n" +
            "\troom_coupon\n" +
            "WHERE\n" +
            "\troom_code = #{roomCode} AND organization_id = #{organizationId} AND audit_status = 3 and surplus_deductible_money>0")
    List<CostDeductionDO> findCouponFee(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);


    /**
     * 查询水电费费用
     * @param roomCode
     * @param chargeType
     * @return
     */
    @Select("SELECT bd.* FROM bill \n" +
            "INNER JOIN bill_detailed AS bd ON bill.order_id = bd.order_id WHERE bill.room_code = #{roomCode} \n" +
            "AND bd.state_of_arrears = 1 and arrears_type = '18'")
//    AND bd.charge_type = #{chargeType}
//    ,@Param("chargeType") String chargeType
    List<BillDetailedDO> findWaterElectricFee(@Param("roomCode") String roomCode);

    /**
     * 查询物业费收费项目
     * @param roomCode
     * @param organizationId
     * @return
     */
    @Select("SELECT\n" +
            "\tci.*\n" +
            "FROM\n" +
            "\troom_charge_items AS rci\n" +
            "INNER JOIN charge_item AS ci ON rci.charge_code = ci.charge_code\n" +
            "WHERE\n" +
            "\troom_code = #{roomCode}\n" +
            "AND ci.organization_id = #{organizationId}\n" +
            "AND ci.`enable` = 1\n" +
            "AND charge_type IN (1, 2, 3)")
    ChargeItemDO findChargeItemProperty(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);


    @Select("SELECT cd.* FROM charge_item AS ci,charge_detail AS cd WHERE cd.charge_code = ci.charge_code AND cd.charge_code = #{chargeCode} AND ci.`enable`=1 AND cd.money = #{money} AND organization_id = #{organizationId}")
    ChargeDetailDO findChargeThreeWayDetail(@Param("chargeCode") String chargeCode,@Param("money") Double money,@Param("organizationId") String organizationId);

    @Delete("DELETE FROM room_charge_items WHERE charge_code IN (SELECT charge_code FROM charge_item WHERE organization_id=#{organizationId} AND charge_type IN (1,2,3)) AND room_code = #{roomCode}")
    void deleteChargeItemConfigInfo(@Param("organizationId") String organizationId,@Param("roomCode") String roomCode);


    @Select("SELECT cd.money FROM charge_item AS ci INNER JOIN charge_detail AS cd ON ci.charge_code = cd.charge_code WHERE ci.charge_type = #{chargeType} AND cd.parking_space_place = #{parkingSpacePlace} AND cd.parking_space_type = #{parkingSpaceType} AND ci.organization_id = #{organizationId}")
    Double findMoneyBychargeTypeAndParkingSpacePlaceAndParkingSpaceTypeAndOrganizationId(@Param("chargeType") Integer chargeType,
                                                                                           @Param("parkingSpacePlace") Integer parkingSpacePlace,
                                                                                           @Param("parkingSpaceType") String parkingSpaceType,
                                                                                           @Param("organizationId") String organizationId);
}
