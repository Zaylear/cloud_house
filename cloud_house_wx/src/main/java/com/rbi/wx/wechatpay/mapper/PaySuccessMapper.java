package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.paymentrecordentity.*;
import com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto.SuccessRoomDTO;
import com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto.SuccesssCouponDTO;
import com.rbi.wx.wechatpay.util.successmapperresult.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface PaySuccessMapper {

    @Insert("insert into original_bill(actual_money_collection,amount_receivable,building_code,building_name,charge_code,charge_name,charge_standard,charge_unit,datedif,discount,due_time,idt,invalid_state,mobile_phone,order_id,organization_id,organization_name,payer_name,payer_phone,payment_method,payment_type,preferential_amount,refund_status,room_code,room_size,start_time,state_of_arrears,surname,udt,unit_code,unit_name,user_id,village_code,village_name,balance_amount,coupon_code,coupon_name) values\n" +
            " (#{payMentRecordEntity.actualMoneyCollection},#{payMentRecordEntity.amountReceivable},#{payMentRoomEntity.buildingCode},#{payMentRoomEntity.buildingName},#{payMentChargeEntity.chargeCode},#{payMentChargeEntity.chargeName},#{payMentChargeEntity.chargeStandard},#{payMentChargeEntity.chargeUnit},#{payMentChargeEntity.datedif},#{payMentChargeEntity.discount},(SELECT DATE_ADD((SELECT (CASE WHEN T.t1 is NOT NULL then T.t1 ELSE NOW() END) as t2 FROM (SELECT MAX(due_time) as t1 from original_bill WHERE room_code = #{payMentRoomEntity.roomCode} and charge_code= #{payMentChargeEntity.chargeCode} and invalid_state = '0') T),INTERVAL 1 MONTH)),NOW(),0,#{mobilePhone},#{payMentRecordEntity.orderId},#{payMentChargeEntity.organizationId},#{payMentChargeEntity.organizationName},#{payMentPayerEntity.userName},#{payMentPayerEntity.userPhone},\"微信支付\",1,#{payMentRecordEntity.preferentialAmount},0,#{payMentRoomEntity.roomCode},#{payMentRoomEntity.roomSize}, (SELECT (CASE WHEN T.t1 is NOT NULL then T.t1 ELSE NOW() END) as t2 FROM (SELECT MAX(due_time) as t1 from original_bill WHERE room_code = #{payMentRoomEntity.roomCode} and charge_code= #{payMentChargeEntity.chargeCode} and invalid_state = '0') T),0,#{payMentPayerEntity.surname},NOW(),#{payMentRoomEntity.unitCode},#{payMentRoomEntity.unitName},#{payMentPayerEntity.userId},#{payMentRoomEntity.villageCode},#{payMentRoomEntity.villageName},#{balanceAmount},#{payMentCouponEntity.id},#{payMentCouponEntity.couponName})")
    void addOriginalBill(@Param("payMentRecordEntity") PayMentRecordEntity payMentRecordEntity,
                         @Param("payMentChargeEntity") PayMentChargeEntity payMentChargeEntity,
                         @Param("payMentRoomEntity") PayMentRoomEntity payMentRoomEntity,
                         @Param("payMentPayerEntity") PayMentPayerEntity payMentPayerEntity,
                         @Param("mobilePhone") String mobilePhone,
                         @Param("payMentCouponEntity") PayMentCouponEntity payMentCouponEntity,
                         @Param("balanceAmount") Double balanceAmount);

    @Update("update room_charge_items set surplus='0',upt=NOW() where room_code=#{roomCode} and charge_code = #{chargeCode}")
    void updateSurplus(String roomCode,String chargeCode);

    @Select("SELECT\n" +
            "organization.organization_id AS organizationId,\n" +
            "organization.organization_name AS organizationName,\n" +
            "charge_item.charge_code AS chargeCode,\n" +
            "charge_item.charge_name AS chargeName,\n" +
            "charge_item.charge_type AS chargeType,\n" +
            "charge_item.charge_unit AS chargeUnit,\n" +
            "charge_item.charge_standard AS chargeStandard,\n" +
            "charge_detail.datedif,\n" +
            "charge_detail.area_min AS areaMin,\n" +
            "charge_detail.area_max AS areaMax,\n" +
            "charge_detail.discount\n" +
            "FROM\n" +
            "charge_item\n" +
            "INNER JOIN charge_detail ON charge_item.charge_code = charge_detail.charge_code\n" +
            "INNER JOIN organization ON organization.organization_id = charge_item.organization_id\n" +
            "WHERE\n" +
            "charge_item.`enable` = '1' and " +
            "charge_detail.datedif=#{datedif} and " +
            "charge_item.charge_code = #{chargeCode}")
    PayMentChargeEntity getPayMentCharge(@Param("chargeCode")String chargeCode,@Param("datedif")String datedif);
    @Select("SELECT\n" +
            "sys_customer_info.username AS userName,\n" +
            "sys_customer_info.mobile_phone AS userPhone,\n" +
            "sys_customer_info.user_id AS userId,\n" +
            "sys_customer_info.surname\n" +
            "FROM\n" +
            "sys_customer_info\n " +
            "where enabled = '1' and " +
            "user_id = #{userId}")
    PayMentPayerEntity getPayMentPayerEntity(String userId);
    @Select("SELECT\n" +
            "regional_unit.unit_name AS unitName," +
            "regional_room.room_code AS roomCode," +
            "regional_room.room_size AS roomSize,\n" +
            "regional_building.building_name AS buildingName,\n" +
            "regional_building.building_code AS buildingCode,\n" +
            "regional_unit.unit_code AS unitCode,\n" +
            "regional_village.village_code AS villageCode,\n" +
            "regional_village.village_name AS villageName\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "where regional_room.room_code=#{roomCode}")
    PayMentRoomEntity getPayMentRoomEntity(String roomCode);
    @Select("SELECT\n" +
            "room_coupon.id,\n" +
            "room_coupon.balance_amount AS balanceAmount,\n" +
            "room_coupon.coupon_name\n" +
            "FROM\n" +
            "room_coupon where id=#{couponId}")
    PayMentCouponEntity getPayMentCouponEntity(String couponId);

    @Update("update room_coupon set udt=NOW(),balance_amount=balance_amount-#{conponMoney}," +
            "property_fee=property_fee+#{conponMoney} where id=#{couponId}")
    Integer updateCoupon(@Param("conponMoney")Double couponMoney,@Param("couponId")String couponId);
    @Update("update room_coupon set udt=NOW(),usage_state='1' where id=#{id}")
    Integer updateType(String id);
    @Select("select charge_type from charge_item where charge_code=#{chargeCode} and enable = '1'")
    String chargeType(String chargeCode);
    @Insert("insert into bill (invalid_state,actual_total_money_collection,amount_total_receivable,detailed,building_code,building_name,idt,mobile_phone,order_id,organization_id,organization_name,payer_name,payer_phone,payment_method,preferential_total_amount,room_code,room_size,surname,surplus,unit_code,unit_name,user_id,village_code,village_name) values " +
            "('1',#{payMentRecordEntity.actualMoneyCollection},#{payMentRecordEntity.amountReceivable},#{detailed},#{payMentRoomEntity.buildingCode},#{payMentRoomEntity.buildingName},NOW(),#{mobilePhone},#{payMentRecordEntity.orderId}," +
            "#{payMentChargeEntity.organizationId},#{payMentChargeEntity.organizationName},#{payMentPayerEntity.userName},#{payMentPayerEntity.userPhone},'微信支付',#{payMentRecordEntity.preferentialAmount},#{payMentRoomEntity.roomCode},#{payMentRoomEntity.roomSize}" +
            ",#{payMentPayerEntity.surname},'0',#{payMentRoomEntity.unitCode},#{payMentRoomEntity.unitName},#{payMentPayerEntity.userId},#{payMentRoomEntity.villageCode},#{payMentRoomEntity.villageName})")
    Integer addPropertyBill(@Param("payMentRecordEntity") PayMentRecordEntity payMentRecordEntity,
                            @Param("payMentChargeEntity") PayMentChargeEntity payMentChargeEntity,
                            @Param("payMentRoomEntity") PayMentRoomEntity payMentRoomEntity,
                            @Param("payMentPayerEntity") PayMentPayerEntity payMentPayerEntity,
                            @Param("mobilePhone") String mobilePhone,
                            @Param("payMentCouponEntity") PayMentCouponEntity payMentCouponEntity,
                            @Param("balanceAmount") Double balanceAmount,
                           @Param("detailed")String detailed);
    @Select("select SUM(surplus) from room_customer where logged_off_state='0' and room_code=#{roomCode}")
    Double getRoomSurplus(String roomCode);
    @Select("select id,surplus from room_customer where room_code=#{roomCode} and logged_off_state='0' and surplus>0 and surplus is not null ORDER BY surplus DESC")
    List<SuccessRoomDTO> getSurplus(String roomCode);
    @Update("update room_customer set surplus=#{surplus},udt=NOW() where id=#{id}")
    Integer updateSurPlus(@Param("id")String id,@Param("surplus")Double surplus);
    @Select("select SUM(current_readings-last_reading) from record_arrears  where room_code=#{roomCode} and charge_code=#{chargeCode} and state_of_arrears = '1' and invalid_state = '0'")
    Double getEletricitySum(@Param("roomCode")String roomCode,@Param("chargeCode") String chargeCode);
    @Select("SELECT\n" +
            "regional_room.room_size as roomSize,\n" +
            "regional_room.room_code as roomCode,\n" +
            "regional_village.village_code as villageCode,\n" +
            "regional_village.village_name as villageName,\n" +
            "regional_unit.unit_code as unitCode,\n" +
            "regional_unit.unit_name as unitName ,\n" +
            "regional_building.building_code as buildingCode,\n" +
            "regional_building.building_name as buildingName,\n" +
            "regional_region.region_code as regionCode,\n" +
            "regional_region.region_name as regionName,\n" +
            "regional_village.organization_id as organizationId,\n" +
            "regional_village.organization_name as organizationName\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "where regional_room.room_code=#{roomCode}")
    RoomInfo getRoomInf(String roomCode);

    @Select("SELECT MAX(bd.due_time) FROM bill INNER JOIN bill_detailed AS bd ON bill.order_id = bd.order_id WHERE bill.room_code = #{roomCode} AND " +
            "bill.organization_id=#{organizationId} AND bd.charge_type IN (1,2,3)")
    String findMaxDueTime(@Param("roomCode") String roomCode,
                          @Param("organizationId") String organizationId);

    @Select("SELECT\n" +
            "sys_customer_info.id_number as idNumber,\n" +
            "sys_customer_info.mobile_phone as mobilePhone,\n" +
            "sys_customer_info.mobile_phone as payerPhone,\n" +
            "sys_customer_info.surname as payerName,\n" +
            "sys_customer_info.user_id as customerUserId\n" +
            "FROM\n" +
            "sys_customer_info\n" +
            "where enabled='1' and user_id=#{userCode}\n")
    CustomerUserInfo getCustomerUserInfo(String userCode);
    @Select("SELECT\n" +
            "charge_detail.datedif,\n" +
            "charge_detail.discount,\n" +
            "charge_item.organization_id as organizationId,\n" +
            "charge_item.charge_name as chargeName,\n" +
            "charge_item.charge_standard as chargeStandard\n" +
            "FROM\n" +
            "charge_detail\n" +
            "INNER JOIN charge_item ON charge_item.charge_code = charge_detail.charge_code\n" +
            "where charge_detail.charge_code=#{chargeCode} and charge_item.`enable`='1'  and charge_detail.datedif=#{datedif}")
    ChargeInfo getChargeInfo(@Param("chargeCode") String chargeCode,@Param("datedif") String datedif);
    @Select("SELECT\n" +
            "id,\n" +
            "room_coupon.surplus_deductible_money\n" +
            "FROM\n" +
            "room_coupon\n" +
            "where id=#{id}")
    CouponDTO getCouponInfo(String id);

    @Select("SELECT\n" +
            "SUM(bill_detailed.surplus_deductible_money)\n" +
            "FROM\n" +
            "bill_detailed\n" +
            "INNER JOIN bill ON bill_detailed.order_id = bill.order_id\n" +
            "where bill_detailed.surplus_deductible_money>'0' and bill.refund_status ='0' and bill.invalid_state='0' and bill.room_code=#{roomCode}")
    Double selectThreeWayMoney(String roomCode);

    @Select("SELECT\n" +
            "\tsum(surplus_deductible_money)\n" +
            "FROM\n" +
            "\trefund_application AS ra\n" +
            "INNER JOIN refund_history AS rh ON ra.order_id = rh.order_id\n" +
            "WHERE\n" +
            "\tsurplus_deductible_money > 0\n" +
            "AND ra.audit_status = '3'\n" +
            "AND rh.room_code = #{roomCode}\n" +
            "AND ra.organization_id =#{organizationId}")
    Double selectReturnMoney(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);


    @Select("SELECT\n" +
            "room_customer.surplus\n" +
            "FROM\n" +
            "room_customer\n" +
            "where room_customer.customer_user_id=#{userCode} and room_customer.room_code=#{roomCode}")
    Double  getSurplusInfo(@Param("roomCode") String roomCode,@Param("userCode")String userCode);




    @Insert("<script>\n"+
            "insert into bill_detailed (actual_money_collection,amount_deducted_this_time," +
            "amount_receivable,charge_code,charge_name,charge_standard,charge_type,charge_unit,deductible_money,deductibled_money,deduction_record," +
            "discount,order_id,organization_id,payer_name,payer_user_id,surplus_deductible_money,datedif,start_time,due_time,payer_phone" +
            ",arrears_type,parent_code,split_state,state_of_arrears) values \n" +
            "(#{actualMoneyCollection},#{bill.amountDeductedThisTime},#{amountReceivable},#{bill.chargeCode}" +
            ",#{bill.chargeName},#{bill.chargeStandard},'1',#{bill.chargeUnit},'0','0',#{deductionRecord}," +
            "#{bill.discount},#{orderId},#{roomInfo.organizationId},#{payerInfo.payerName},#{payerInfo.customerUserId}," +
            "'0',#{bill.datedif},#{bill.startTime},#{bill.dueTime},#{payerInfo.payerPhone},'1','0','0','0')" +
            "</script>\n")
    void insertBillDetailed(@Param("bill") BillDetailedDTO billDetailedDTO
            ,@Param("actualMoneyCollection") String actualMoneyCollection
            ,@Param("amountReceivable") String amountReceivable
            ,@Param("orderId")String orderId
            ,@Param("payerInfo")CustomerUserInfo customerUserInfo
            ,@Param("roomInfo")RoomInfo roomInfo
            ,@Param("deductionRecord")String deductionRecord);

    @Update("update room_coupon set surplus_deductible_money=surplus_deductible_money-#{money}," +
            "amount_deducted_this_time=#{money}," +
            "deductible_money=deductible_money-#{money}," +
            "deductibled_money=deductibled_money+#{money}," +
            "deduction_record=#{deductionRecord}," +
            "property_fee=property_fee+#{money}," +
            "surplus_deductible_money=surplus_deductible_money-#{money},\n" +
            " udt=NOW()  " +
            "where id=#{couponId}")
    void updateCouponMoney(@Param("couponId")String couponId,@Param("money")String money,@Param("deductionRecord") String deductionRecord);


    /*




,region_code,region_name,room_code,room_size,surname,udt,unit_code,unit_name,village_code,village_name)" +
            "values" +


,#{roomInfo.regionCode}" +
            ",#{roomInfo.regionName},#{roomInfo.roomCode},#{roomInfo.roomSize},#{payerInfo.surname},NOW(),#{roomInfo.unitCode},#{roomInfo.unitName}" +
            "#{roomInfo.villageCode},#{roomInfo.villageName})"
     */



    @Insert("insert into bill(customer_user_id,actual_total_money_collection,amount_total_receivable,building_code,building_name" +
            ",id_number,idt,invalid_state,mobile_phone,order_id,organization_id,organization_name,payer_name,payer_phone,payment_method" +
            ",refund_status,region_code,region_name,room_code,room_size,surname,udt,unit_code,unit_name,village_code,village_name,real_generation_time,corrected_amount)" +
            "values" +
            "(#{payerInfo.customerUserId},#{billInfo.actualTotalMoneyCollection},#{billInfo.amountTotalReceivable}," +
            "#{roomInfo.buildingCode},#{roomInfo.buildingName},#{payerInfo.idNumber},NOW(),'0',#{payerInfo.mobilePhone},#{billInfo.orderId}" +
            ",#{roomInfo.organizationId},#{roomInfo.organizationName},#{payerInfo.payerName},#{payerInfo.mobilePhone},'8','0',#{roomInfo.regionCode}" +
            ",#{roomInfo.regionName},#{roomInfo.roomCode},#{roomInfo.roomSize},#{payerInfo.payerName},NOW(),#{roomInfo.unitCode},#{roomInfo.unitName}," +
            "#{roomInfo.villageCode},#{roomInfo.villageName},NOW(),'0')")
    void addBill(@Param("payerInfo")CustomerUserInfo customerUserInfo
            ,@Param("billInfo")AddBillDTO addBillDTO
            ,@Param("roomInfo")RoomInfo roomInfo);
    @Select("SELECT\n" +
            "bill_detailed.deductible_money as deductibleMoney,\n" +
            "bill_detailed.discount,\n" +
            "bill_detailed.deduction_record as deductionRecord,\n" +
            "bill_detailed.charge_type as chargeType,\n" +
            "bill_detailed.surplus_deductible_money as surplusDeductibleMoney,\n" +
            "bill_detailed.bill_detailed_id as billDetailedId,\n" +
            "bill_detailed.deductibled_money as deductibledMoney,\n" +
            "bill_detailed.charge_name as chargeName\n" +
            "FROM\n" +
            "bill_detailed\n" +
            "INNER JOIN bill ON bill_detailed.order_id = bill.order_id\n" +
            "where charge_type in (14,15,16) and bill.room_code=#{roomCode} and bill_detailed.surplus_deductible_money>'0'\n")
    List<ThreeWayDTO> getThreeWayList(String roomCode);
    @Update("update bill_detailed set surplus_deductible_money=surplus_deductible_money-#{money},\n" +
            "deductibled_money=deductibled_money+#{money},\n" +
            "amount_deducted_this_time=#{money},\n" +
            "deductible_money=deductible_money-#{money},\n" +
            "deduction_record=#{deductionRecord}\n" +
            "where bill_detailed_id=#{id}")
    void updateThreeWay(@Param("id")String id,@Param("money")String money,@Param("deductionRecord") String deductionRecord);


    @Select("SELECT\n" +
            "refund_application.id,\n" +
            "refund_application.surplus_deductible_money as surplusDeductibleMoney,\n" +
            "refund_application.deductible_money as deductibleMoney,\n" +
            "refund_application.deductibled_money as deductibledMoney,\n" +
            "refund_application.deduction_record as deductionRecord,\n" +
            "FROM\n" +
            "bill\n" +
            "INNER JOIN refund_application ON bill.order_id = refund_application.order_id\n" +
            "where refund_application.surplus_deductible_money>'0' and bill.invalid_state='0' and bill.room_code=#{roomCode}")
    List<RefundMoneyDTO> getRefundMoney(@Param("roomCode")String roomCode);
    @Update("update refund_application set surplus_deductible_money=surplus_deductible_money-#{money}," +
            "deductibled_money=deductibled_money+#{money},\n" +
            "amount_deducted_this_time=#{money},\n" +
            "deductible_money=deductible_money-#{money},\n" +
            "deductibled_money=deductibled_money+{money},\n" +
            "deduction_property_fee=deduction_property_fee+#{money},\n" +
            "surplus_deductible_money=surplus_deductible_money-#{money},\n" +
            "udt=NOW(),deduction_record=#{deductionRecord}\n" +
            "where id=#{id}")
    void updateRefundMoney(@Param("id")String id,@Param("money")String money,@Param("deductionRecord") String deductionRecord);
    @Select("SELECT\n" +
            "id,\n" +
            "room_customer.surplus\n" +
            "FROM\n" +
            "room_customer\n" +
            "where room_customer.customer_user_id=#{userCode} and room_customer.room_code=#{roomCode} and room_customer.surplus>'0'")
    SurplusDTO getSurplusDTO(@Param("roomCode")String roomCode,@Param("userCode")String userCode);
    @Update("update room_customer set surplus=surplus-#{surplus} where id=#{id}")
    void updateSurplusDTO(@Param("id")String id,@Param("surplus")String surplus);


    @Select("SELECT COUNT(*) FROM bill WHERE organization_id = #{organizationId} AND order_id LIKE '${time}%'")
    Integer findOrderIdsByorganizationId(@Param("organizationId") String organizationId, @Param("time") String time);

    @Insert("<script>\n" +
            "insert into cost_deduction (amount_deducted_this_time,charge_type,deductible_money,deductibled_money," +
            "deduction_code,deduction_method,deduction_order_id,deduction_record,deduction_status,discount,order_id,organization_id" +
            ",original_deduction_status,surplus_deductible_money) values " +
            "<foreach collection='costListA' item='costList' separator=','>\n" +
            "(#{costList.amountDeductedThisTime},#{costList.chargeType},#{costList.deductibleMoney},#{costList.deductibledMoney}" +
            ",#{costList.deductionCode},#{costList.deductionMethod},#{costList.deductionOrderId},#{deductionRecord}" +
            ",'1',#{costList.discount},#{orderNum},#{organizationId},'1',#{costList.surplusDeductibleMoney})"  +
            "</foreach>\n" +
            "</script>\n")
    void addCostDeduction(@Param("costListA") List<CostDeduction> costList
            ,@Param("orderNum") String orderNum
            ,@Param("organizationId") String organizationId
            ,@Param("deductionRecord") String deductionRecord);
    @Select("select\n" +
            "id,\n" +
            "amount_deducted_this_time as amountDeductedThisTime,\n" +
            "deductible_money as deductibleMoney,\n" +
            "deductibled_money as deductibledMoney,\n" +
            "deduction_record as deductionRecord,\n" +
            "property_fee as propertyFee,\n" +
            "surplus_deductible_money as surplusDeductibleMoney\n" +
            " from room_coupon where id=#{id}")
    SuccesssCouponDTO getCoupon(String id);
    @Select("SELECT\n" +
            "MAX(bill_detailed.due_time)\n" +
            "FROM\n" +
            "bill\n" +
            "INNER JOIN bill_detailed ON bill.order_id = bill_detailed.order_id\n" +
            "where bill.room_code=#{roomCode} and bill.organization_id=#{organizationId}")
    String getMaxTime(@Param("roomCode")String roomCode,@Param("organizationId") String organizationId);
}
