package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.PayMentRecordsDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayMentRoomDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayMentUserDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayerInfoDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayerPayListDTO;
import com.rbi.wx.wechatpay.dto.paysuecces.ParkingSpaceRentalRoomDTO;
import com.rbi.wx.wechatpay.dto.paysuecces.ParkingSpaceRentalUserDTO;
import com.rbi.wx.wechatpay.entity.payment.PayMentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface PaymentRecordsMapper {
    @Select("select surplus from room_charge_items where room_code=#{roomCode} and charge_code=#{chargeCode}")
    double getSurplus(String roomCode,String chargeCode);
    @Select("SELECT original_bill.charge_name as chargeName,original_bill.charge_code as chargeCode,(CASE WHEN NOW()>MAX(due_time) THEN 'red' ELSE 'green' END) AS color,(CASE WHEN NOW()>MAX(due_time) THEN '欠费' ELSE '未欠费' END) AS stateOfArrears from original_bill INNER JOIN charge_item ON charge_item.charge_code=original_bill.charge_code  where charge_item.charge_type in ('1') and original_bill.room_code = #{roomCode} GROUP BY original_bill.charge_code,original_bill.charge_name")
    List<PayMentEntity> chargeList(String roomCode);
    @Select("SELECT record_arrears.charge_name as chargeName,record_arrears.charge_code as chargeCode,(CASE WHEN state_of_arrears='1' THEN 'red' ELSE 'green' END) AS color,(CASE WHEN state_of_arrears='1' THEN '欠费' ELSE '未欠费' END) AS stateOfArrears from record_arrears INNER JOIN charge_item ON charge_item.charge_code=record_arrears.charge_code  where charge_item.charge_type in ('7') and record_arrears.room_code = #{roomCode} GROUP BY record_arrears.charge_code,record_arrears.charge_name")
    PayMentEntity getRecords(String roomCode);
    @Select("SELECT user_id from room_customer where room_code=#{roomCode} and identity= '1'")
    String getProperty(String roomCode);
    @Select("SELECT\n" +
            "bill.actual_total_money_collection as money,\n" +
            "bill.payer_name as userName,\n" +
            "bill_detailed.charge_name as chargeName,\n" +
            "bill.idt as date,\n" +
            "bill.room_code as roomCode,\n" +
            "bill_detailed.payer_user_id as userId\n" +
            "FROM\n" +
            "bill\n" +
            "INNER JOIN bill_detailed ON bill_detailed.order_id = bill.order_id\n" +
            "where payer_user_id = #{userId}\n" +
            "and invalid_state='0'\n" +
            "GROUP BY bill.order_id\n" +
            "limit #{pageSize},#{pageNum}")
    List<PayMentRecordsDTO> getPaymentRecords(@Param("userId") String userId,@Param("pageSize") Integer pageSize,@Param("pageNum") Integer pageNum);
    @Select("SELECT DISTINCT\n" +
            "bill.idt as date,\n" +
            "bill_detailed.payer_user_id  as userId,\n" +
            "bill_detailed.payer_name as userName,\n" +
            "bill_detailed.charge_name as chargeName,\n" +
            "bill.actual_total_money_collection as money\n" +
            "FROM\n" +
            "bill\n" +
            "INNER JOIN bill_detailed ON bill.order_id = bill_detailed.order_id\n" +
            " where bill.room_code = #{roomCode} and invalid_state='0' ORDER BY bill.idt DESC limit #{pageNum},#{pageSize}")
    List<PayMentUserDTO> getPayMentListByRoomCode(@Param("roomCode") String roomCode,@Param("pageSize")Integer pageSize,@Param("pageNum") Integer pageNum);
    @Select("SELECT\n" +
            "charge_item.charge_name as chargeName,\n" +
            "bill.idt as date,\n" +
            "bill_detailed.payer_user_id,\n" +
            "bill_detailed.actual_money_collection as money\n" +
            "FROM\n" +
            "bill\n" +
            "INNER JOIN bill_detailed ON bill.order_id = bill_detailed.order_id\n" +
            "INNER JOIN charge_item ON bill_detailed.charge_code = charge_item.charge_code\n" +
            "where bill.invalid_state='0' and bill.refund_status='0' and  bill.room_code=#{roomCode} and bill_detailed.payer_user_id=#{userId} and " +
            "bill.payment_method ='8' \n" +
            "ORDER BY bill.idt DESC limit #{pageNum},#{pageSize}")
    List<PayerPayListDTO> getPayerPayList(@Param("roomCode") String roomCode,@Param("userId") String userId,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    @Select("SELECT\n" +
            "regional_village.village_name AS villageName,\n" +
            "regional_region.region_name AS regionName,\n" +
            "regional_building.building_name AS buildingName,\n" +
            "regional_unit.unit_name AS unitName,\n" +
            "regional_room.room_code AS roomCode,\n" +
            "regional_room.room_size AS roomSize,\n" +
            "sys_customer_info.user_id AS userId,\n" +
            "sys_customer_info.username AS userName,\n" +
            "sys_customer_info.mobile_phone AS mobilePhone\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "INNER JOIN room_customer ON room_customer.room_code = regional_room.room_code\n" +
            "INNER JOIN sys_customer_info ON room_customer.user_id = sys_customer_info.user_id\n" +
            "where room_customer.user_id = #{userId} and regional_room.room_code = #{roomCode}")
    PayMentRoomDTO getPayMentRoom(@Param("userId") String userId,@Param("roomCode") String roomCode);
    @Select("select sys_customer_info.user_id AS userId,\n" +
            "sys_customer_info.username AS userName,\n" +
            "sys_customer_info.mobile_phone AS mobilePhone FROM\n" +
            "sys_customer_info\n" +
            "where user_id= #{userId}")
    PayerInfoDTO getPayerInfo(String userId);
    @Select("SELECT CONCAT(surname,(CASE WHEN sex = '1' THEN '先生' ELSE '女士' END)) as surName,\n" +
            "sys_customer_info.mobile_phone AS mobilePhone\n" +
            "FROM\n" +
            "sys_customer_info" +
            " where user_id =#{userId}")
    com.rbi.wx.wechatpay.dto.paymentlist.PayerInfoDTO getPayerInfoSimple(String userId);


    @Select("select  (CASE WHEN charge_type = '1' THEN 'true' ELSE 'false' END) AS type from charge_item where charge_code=#{chargeCode}")
    String getCharType(String chargeCode);
    @Select("SELECT\n" +
            "charge_item.charge_name as chargeName,\n" +
            "charge_item.charge_code as chargeCode,\n" +
            "(CASE WHEN charge_item.charge_type='1' THEN '1' ELSE '0' END) as type\n" +
            "FROM\n" +
            "room_charge_items\n" +
            "INNER JOIN charge_item ON charge_item.charge_code = room_charge_items.charge_code\n" +
            "WHERE charge_item.`enable`='1' and charge_item.charge_type='1' and room_charge_items.room_code=#{roomCode} " +
            "and charge_item.organization_id=#{organizationId}")
    PayMentEntity getPayMent(@Param("roomCode") String roomCode,@Param("organizationId")String organizationId);
    @Select("SELECT\n" +
            "room_charge_items.organization_id\n"+
            "FROM\n" +
            "room_charge_items\n" +
            "INNER JOIN charge_item ON charge_item.charge_code = room_charge_items.charge_code\n" +
            "WHERE charge_item.`enable`='1' and charge_item.charge_type='1' and room_charge_items.room_code=#{roomCode}")
    String getOrganizationId(String roomCode);

    @Select("SELECT MAX(bd.due_time) FROM bill INNER JOIN bill_detailed AS bd ON bill.order_id = bd.order_id WHERE bill.room_code = #{roomCode} AND " +
            "bill.organization_id=#{organizationId} AND bd.charge_type ='1'")
    String findMaxDueTime(@Param("roomCode") String roomCode,
                          @Param("organizationId") String organizationId);
    @Select("SELECT DISTINCT\n" +
            "room_customer.start_billing_time\n" +
            "FROM\n" +
            "room_customer\n" +
            "WHERE room_code =#{roomCode}")
    String findMaxDueTimeFromRoomCustom(String roomCode);

    @Select("select room_size from regional_room where room_code=#{roomCode}")
    Double getRoomSize(String roomCode);
    @Select("select charge_standard from charge_item where charge_code =#{chargeCode} and enable='1'")
    Double getChargeStandard(String chargeCode);

    @Select("SELECT\n" +
            "sys_customer_info.id_number as idNumber,\n" +
            "sys_customer_info.surname,\n" +
            "sys_customer_info.mobile_phone as mobilePhone,\n" +
            "sys_customer_info.user_id as customerUserId\n" +
            "FROM\n" +
            "sys_customer_info\n" +
            "where user_id=#{userId}")
    ParkingSpaceRentalUserDTO getUserInfo(String userId);
    @Select("SELECT\n" +
            "regional_room.room_size as roomSize,\n" +
            "regional_room.room_code as roomCode,\n" +
            "regional_unit.unit_code as unitCode,\n" +
            "regional_unit.unit_name as unitName,\n" +
            "regional_building.building_code as buildingCode,\n" +
            "regional_building.building_name as buildingName,\n" +
            "regional_region.region_code as regionCode,\n" +
            "regional_region.region_name as regionName,\n" +
            "regional_village.village_code as villageCode,\n" +
            "regional_village.village_name as villageName               \n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "where room_code=#{roomCode} and organization_id=#{organizationId}")
    ParkingSpaceRentalRoomDTO getRoomInfo(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);

}
