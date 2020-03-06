package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.util.chargepayfactory.chargedAllDTO.ChargeElectricityDTO;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.Charge;
import com.rbi.wx.wechatpay.util.chargereal.Room;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.ChargeDetail;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.RoomPerproty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChargeRealMapper {
    @Select("SELECT\n" +
            "charge_detail.datedif,\n" +
            "charge_detail.charge_code AS chargeCode,\n" +
            "charge_item.charge_name AS chargeName,\n" +
            "charge_item.charge_type AS chargeType,\n" +
            "charge_item.organization_id AS organizationId,\n" +
            "organization.organization_name AS organizationName,\n" +
            "charge_item.charge_standard AS chargeStandard,\n" +
            "charge_item.charge_unit AS chargeUnit,\n" +
            "charge_detail.discount\n" +
            "FROM\n" +
            "charge_item\n" +
            "INNER JOIN charge_detail ON charge_item.charge_code = charge_detail.charge_code\n" +
            "INNER JOIN organization ON charge_item.organization_id = organization.organization_id\n" +
            "WHERE charge_item.charge_code =#{chargeCode} " +
            "and charge_detail.datedif=#{datedif}")
    Charge findChargeProperty(@Param("chargeCode") String chargeCode,@Param("datedif") String datedif);
    @Select("select area_max as areaMax,area_min as areaMin," +
            "charge_code as chargeCode,datedif,discount from charge_detail " +
            "where charge_code = #{chargeCode} and datedif=#{datedif}")
    ChargeDetail findChargeDetailProperty(@Param("chargeCode") String chargeCode,@Param("datedif") String datedif);
    @Select("select room_code as roomCode,room_size as roomSize," +
            "room_type as roomType from regional_room where room_code = #{roomCode}")
    RoomPerproty findRoomperproty(String roomCode);
    @Select("select charge_type from charge_item where charge_code= #{chargeCode} and enable = '1'")
    Integer getChargeType(String chargeCode);
    @Select("select id from room_coupon where room_code=#{roomCode} and charge_code=#{chargeCode} and id =#{couponId} and" +
            " audit_status='3' and past_due = '0' and balance_amount > 0.0")
    String couponIsTrue(@Param("couponId") String couponId,@Param("roomCode") String roomCode,@Param("chargeCode") String chargeCode);
    @Select("select balance_amount from room_coupon where room_code=#{roomCode} and charge_code=#{chargeCode} and id =#{couponId} and" +
            " audit_status='3' and past_due = '0' and balance_amount > 0.0")
    Double couponbalanceAmount(@Param("couponId") String couponId,@Param("roomCode") String roomCode,@Param("chargeCode") String chargeCode);
    @Select("select id,room_code AS roomCode,current_readings AS currentReadings,last_reading AS lastReading,charge_standard AS chargeStandard from " +
            "record_arrears where room_code=#{roomCode} and charge_code=#{chargeCode} and state_of_arrears = '1' and invalid_state = '0'")
    List<ChargeElectricityDTO> findElectricityByRoomCode(@Param("roomCode") String roomCode, @Param("chargeCode")String chargeCode);
    @Select("select SUM(surplus) from room_customer where room_code=#{roomCode}")
    double getSurplus(@Param("roomCode") String roomCode,@Param("chargeCode") String chargeCode);
}
