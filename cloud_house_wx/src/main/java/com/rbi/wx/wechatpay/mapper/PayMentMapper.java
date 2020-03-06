package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.paymentlist.PayCouponDTO;
import com.rbi.wx.wechatpay.dto.paymentlist.PayMentListPropertyDTO;
import com.rbi.wx.wechatpay.util.chargepayfactory.chargedAllDTO.ChargeElectricityDTO;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.RoomPerproty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PayMentMapper {
    @Select("SELECT\n" +
            "charge_detail.charge_code AS chargeCode,\n" +
            "charge_detail.area_min AS areaMin,\n" +
            "charge_detail.area_max AS areaMax,\n" +
            "charge_item.charge_name AS chargeName,\n" +
            "charge_item.charge_type AS chargeType,\n" +
            "charge_item.charge_standard AS changeStandard,\n" +
            "charge_detail.discount,\n" +
            "charge_detail.datedif\n" +
            "FROM\n" +
            "charge_item\n" +
            "INNER JOIN charge_detail ON charge_item.charge_code = charge_detail.charge_code\n" +
            "WHERE\n" +
            "charge_detail.charge_code = #{chaegeCode} AND\n" +
            "charge_item.`enable` = '1'\n" +
            "ORDER BY\n" +
            "charge_detail.datedif ASC\n")
    List<PayMentListPropertyDTO> findPropertyList(String chargeCode);
    @Select("SELECT\n" +
            "regional_room.room_size as roomSize,\n" +
            "regional_room.room_code as roomCode,\n" +
            "regional_room.room_type as roomType\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "where regional_village.organization_id=#{organizationId} and regional_room.room_code=#{roomCode}")
    RoomPerproty findRoomByPerproty(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);


    @Select("select DISTINCT charge_type FROM charge_item where enable= '1' and charge_code=#{chargeCode}")
    Integer findChargeTypeByChargeCode(String chargeCode);

    @Select("SELECT\n" +
            "room_coupon.id,\n" +
            "room_coupon.surplus_deductible_money AS balanceAmount,\n" +
            "room_coupon.charge_code AS chargeCode,\n" +
            "room_coupon.money,\n" +
            "coupon_name AS couponName\n," +
            "organization_name AS organizationName," +
            "end_time AS endDate " +
            "FROM\n" +
            "room_coupon\n" +
            "WHERE past_due='0' and audit_status = '3' " +
            "and room_code=#{roomCode} and charge_code=#{chargeCode} and " +
            "surplus_deductible_money > 0.0 and usage_state = '0'")
    List<PayCouponDTO> findCoupon(@Param("roomCode") String roomCode,@Param("chargeCode") String chargeCode);
    @Select("select charge_type from charge_item where charge_code=#{chargeCode}")
    Integer findType(String chargeCode);

    @Select("select charge_standard from charge_item where charge_code=#{chargeCode} and enable = '1'")
    Double getMoney(String chargeCode);
    @Select("select surplus_deductible_money from room_coupon where id=#{couponId}")
    Double  getCouponMoney(String couponId);
    @Select("SELECT\n" +
            "room_customer.surplus\n" +
            "FROM\n" +
            "wx_user_info\n" +
            "INNER JOIN room_customer ON room_customer.customer_user_id = wx_user_info.user_id\n" +
            "where wx_user_info.`status`='0' and wx_user_info.open_id=#{openId} and room_customer.room_code=#{roomCode}")
    Double  getSurplus(@Param("roomCode") String roomCode,@Param("openId")String openId);
    @Select("select discount from charge_detail where datedif=#{datedif} and " +
            "charge_code =#{chargeCode} ")
    Double  getChargeDiscount(@Param("datedif") String datedif,@Param("chargeCode") String chargeCode);
    @Select("select room_size from regional_room where room_code=#{roomCode}")
    Double  getRoomSize(String roomCode);
    @Select("select area_max from charge_detail where datedif=#{datedif} and charge_code=#{chargeCode}")
    String getAreaMax(@Param("chargeCode") String chargeCode,@Param("datedif")String datedif);
    @Select("select area_min from charge_detail where datedif=#{datedif} and charge_code=#{chargeCode}")
    String getAreaMin(@Param("chargeCode") String chargeCode,@Param("datedif")String datedif);
    @Select("select id,room_code AS roomCode,current_readings AS currentReadings,last_reading AS lastReading,charge_standard AS chargeStandard from " +
            "record_arrears where room_code=#{roomCode} and charge_code=#{chargeCode} and state_of_arrears = '1' and invalid_state = '0'")
    List<ChargeElectricityDTO> findElectricityByRoomCode(@Param("roomCode") String roomCode,@Param("chargeCode")String chargeCode);
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
            "AND rh.room_code = #{roomCode}")
    Double selectReturnMoney(String roomCode);
    @Select("SELECT\n" +
            "charge_item.organization_id\n" +
            "FROM\n" +
            "charge_item\n" +
            "WHERE `enable`='0' and charge_code=#{chargeCode}")
    String getOrganizationId(String chargeCode);
}
