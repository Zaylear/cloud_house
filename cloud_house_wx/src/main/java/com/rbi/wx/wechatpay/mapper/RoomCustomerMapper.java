package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.RoomInfo;
import com.rbi.wx.wechatpay.dto.RoomUserDTO;
import com.rbi.wx.wechatpay.dto.UserIdentityEntity;
import com.rbi.wx.wechatpay.dto.houseinfo.RoomUser;
import com.rbi.wx.wechatpay.dto.houseinfo.UserDTO;
import com.rbi.wx.wechatpay.dto.indexroom.IndexRoomDTO;
import com.rbi.wx.wechatpay.dto.indexroom.UpdateRoomUserDTO;
import com.rbi.wx.wechatpay.dto.roomcusomer.GetRoomCodesDTO;
import com.rbi.wx.wechatpay.dto.roomcusomer.PorpertyDTO;
import com.rbi.wx.wechatpay.dto.roomcusomer.RoomTimeDTO;
import com.rbi.wx.wechatpay.entity.ChargeMaxTimeDTO;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.AddTenantUserRoomDTO;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.TenantuseRoomCostmer;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.UpdateRoomCustomerRequestEntity;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomCustomerMapper {
    @Update("update room_customer set logged_off_state = '1', udt=now() where " +
            "customer_user_id=#{userId} and room_code=#{roomCode} and logged_off_state = '0' and identity=#{identity}")
    Integer delete(@Param("userId") String userId,@Param("roomCode") String roomCode,@Param("identity") String identity);
    @Insert("insert into room_customer " +
            "(organization_name,organization_id,identity,idt,logged_off_state,room_code,udt,customer_user_id,start_time,past_due,end_time,start_billing_time,normal_payment_status) VALUES" +
            "(#{roomCode.organizationName},#{roomCode.organizationId},#{userIdentityEntity.identity},now(),'0',#{roomCode.roomCode},now(),#{userId},#{roomCode.startTime},'0',#{roomCode.endTime},#{userIdentityEntity.date},'1')")
    Integer addUserRoomCustomer3(@Param("roomCode") UpdateRoomCustomerRequestEntity roomCode,@Param("userId") String userId,@Param("userIdentityEntity") UserIdentityEntity userIdentityEntity);
    @Insert("insert into room_customer " +
            "(organization_name,organization_id,identity,idt,logged_off_state,room_code,udt,customer_user_id,start_time,past_due,end_time,start_billing_time,real_recycling_home_time,normal_payment_status) VALUES" +
            "(#{roomCode.organizationName},#{roomCode.organizationId},#{userIdentityEntity.identity},now(),'0',#{roomCode.roomCode},now(),#{userId},#{roomCode.startTime},'0',#{roomCode.endTime}," +
            "#{roomTime.startBillingTime},#{roomTime.realRecyclingHomeTime},'1')")
    Integer addUserRoomCustomer2(@Param("roomCode") UpdateRoomCustomerRequestEntity roomCode,@Param("userId") String userId,@Param("userIdentityEntity") UserIdentityEntity userIdentityEntity,@Param("roomTime")RoomTimeDTO roomTimeDTO);

    @Select("select room_code from room_customer where " +
            "customer_user_id = #{userId} and logged_off_state = '0' and (past_due = '0' or past_due is null)")
    List<String> getRoomCode(@Param("userId") String userId);
    @Select("<script>" +
            "select" +
            " customer_user_id as userId,room_code as roomCode " +
            "from" +
            " room_customer" +
            " where identity = #{identity}  and logged_off_state = '0' and (past_due = '0' or past_due is null) and " +
            " room_code in" +
            "<foreach collection='roomCodes' item='item' open='(' separator=',' close=')'>" +
            "#{item}"+
            "</foreach>" +
            "</script>")
    List<RoomUser> getRoomUser(@Param("roomCodes")List<String> roomCodes,@Param("identity")String identity);
    @Select("<script>" +
            "select" +
            " customer_user_id as userId " +
            "from" +
            " room_customer" +
            " where identity = #{identity} and logged_off_state = '0' and past_due = '0' and " +
            " room_code in" +
            "<foreach collection='roomCodes' item='item' open='(' separator=',' close=')'>" +
            "#{item}"+
            "</foreach>" +
            "</script>")
    List<String> getuserIds(@Param("roomCodes")List<String> roomCodes,@Param("identity")String identity);
    @Select("select DISTINCT user_id from room_customer where open_id=#{openId}")
    String userIsNull(String openId);
    @Select("select room_code from room_customer where customer_user_id = #{userId} and logged_off_state = '0' and (past_due = '0' or past_due is null)")
    List<String> getIndexRoomCode(String userId);
    @Select("select (CASE WHEN NOW()>MIN(due_time) THEN '欠费' ELSE '正常' END) as statu from original_bill WHERE room_code = #{roomCode} and invalid_state ='0'")
    String getStatus(String roomCode);
    @Select("<script>" +
            "SELECT\n" +
            "regional_room.room_code as roomCode,\n" +
            "regional_village.organization_id as organizationId,\n" +
            "CONCAT(regional_city.city_name,\n" +
            "regional_village.village_name) as address,\n" +
            "regional_village.picture_path as photoPath,\n" +
            "CONCAT(\n" +
            "regional_district.district_name,'-',\n" +
            "regional_village.village_name,'-',\n" +
            "regional_region.region_name,'-',\n" +
            "regional_building.building_name,'-',\n" +
            "regional_unit.unit_name ) AS roomInfo " +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "INNER JOIN regional_district ON regional_village.district_code = regional_district.district_code\n" +
            "INNER JOIN regional_city ON regional_district.city_code = regional_city.city_code\n" +
            "WHERE room_code in " +
            "<foreach collection='roomCodes' item='item' open='(' separator=',' close=')'>" +
            "#{item}"+
            "</foreach>" +
            "</script>")
    List<IndexRoomDTO> findIndexRoomByroomCodes(@Param("roomCodes") List<String> list);
    @Select("SELECT DISTINCT * from\n" +
            "(SELECT regional_room.room_code as roomCode, CONCAT(regional_city.city_name, regional_village.village_name) as address FROM regional_room INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code INNER JOIN regional_district ON regional_village.district_code = regional_district.district_code INNER JOIN regional_city ON regional_district.city_code = regional_city.city_code WHERE room_code = #{roomCode}) T,\n" +
            "(SELECT user_id as userId,CONCAT(surname,(CASE WHEN sex=1 THEN '先生' ELSE '女士' END)) as userName,mobile_phone AS userPhone from sys_customer_info where user_id = #{userId} and enabled = '1') T1,\n" +
            "(SELECT DATE_FORMAT(end_time,'%Y-%m-%d') as endDate,DATE_FORMAT(idt,'%Y-%m-%d') as date,DATE_FORMAT(start_time,'%Y-%m-%d') as startDate from room_customer where identity=#{identity} and room_code=#{roomCode} and customer_user_id = #{userId} and logged_off_state ='0' and (past_due= '0' or past_due is null)) T2")
    RoomInfo finRoomUserByCode(@Param("roomCode") String roomCode,@Param("userId") String userId,@Param("identity")String identity);

    @Select("SELECT user_id FROM sys_customer_info WHERE mobile_phone = #{Phone} and enabled='1'")
    String userIsNullFindByPhone(String Phone);

    @Select("SELECT user_id FROM sys_customer_info WHERE id_number = #{idNumber} and enabled='1'")
    String userIsNullFindByIdNumber(String idNumber);
    @Select(
            "select room_code from room_customer where customer_user_id=#{userId} and logged_off_state='0' and (past_due='0' or past_due is null) and identity = #{identity}"
            )
    List<String> getTrueRoomCodes(@Param("userId") String userId,@Param("identity")String identity);
    @Insert("insert into sys_customer_info(sex,mobile_phone,surname,idt,user_id,id_number) values" +
            "(#{sex},#{phone},#{surname},NOW(),#{userId},#{idNumber})")
    Integer addUser(@Param("userId")String userId,@Param("sex") String sex, @Param("phone") String phone, @Param("surname") String surname,@Param("idNumber")String idNumber);
    @Update("update sys_customer_info set sex=#{sex},mobile_phone=#{userPhone},surname=#{surName},udt=NOW() where " +
            "id_number=#{idNumber} and enabled ='1'")
    Integer updateUser(@Param("userId")String userId,@Param("sex")String sex,@Param("userPhone") String userPhoen,@Param("surName")String surName,@Param("idNumber")String idNumber);
    @Select("SELECT DISTINCT regional_room.room_code as roomCode,regional_room.room_size as roomSize,\n" +
            "\n" +
            "regional_unit.unit_name as unitName,regional_building.building_name as buildingName,\n" +
            "regional_region.region_name as regionName,regional_village.village_name as villageName,\n" +
            "regional_district.district_name as districtName\n" +
            "FROM\n" +
            "regional_room INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "INNER JOIN regional_district ON regional_village.district_code = regional_district.district_code,\n" +
            "sys_customer_info\n" +
            "where regional_room.room_code=#{roomCode} and regional_village.organization_id=#{organizationId}")
     RoomUserDTO findRoomUser(@Param("roomCode") String roomCode,@Param("organizationId")String organizationId);

    @Select("SELECT\n" +
            "regional_room.renovation_deadline as realRecyclingHomeTime,\n" +
            "regional_room.renovation_start_time  as startBillingTime\n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "where regional_room.room_code=#{roomCode} and regional_village.organization_id=#{organizationId}")
    RoomTimeDTO getRoomTimeInfo(@Param("roomCode")String roomCode,@Param("organizationId")String organizationId);

    @Select("<script>" +
            "SELECT\n" +
            "room_customer.idt AS date,\n" +
            "room_customer.customer_user_id AS userId," +
            "room_customer.end_time AS endDate," +
            "room_customer.room_code AS roomCode," +
            "room_customer.start_time AS startDate,\n" +
            "(CASE WHEN sys_customer_info.sex='1' THEN '男' ELSE '女' END) AS sex," +
            "sys_customer_info.surname as userName,\n" +
            "sys_customer_info.mobile_phone,\n" +
            "CONCAT(regional_district.district_name,regional_village.village_name,\n" +
            "regional_region.region_name,regional_building.building_name,\n" +
            "regional_unit.unit_name\n" +
            ") AS address \n" +
            "FROM\n" +
            "regional_room\n" +
            "INNER JOIN room_customer ON regional_room.room_code = room_customer.room_code\n" +
            "INNER JOIN sys_customer_info ON room_customer.customer_user_id = sys_customer_info.user_id\n" +
            "INNER JOIN regional_unit ON regional_room.unit_code = regional_unit.unit_code\n" +
            "INNER JOIN regional_building ON regional_unit.building_code = regional_building.building_code\n" +
            "INNER JOIN regional_region ON regional_building.region_code = regional_region.region_code\n" +
            "INNER JOIN regional_village ON regional_region.village_code = regional_village.village_code\n" +
            "INNER JOIN regional_district ON regional_village.district_code = regional_district.district_code\n" +
            "where room_customer.logged_off_state = '0' and room_customer.past_due = '0' and " +
            "room_customer.customer_user_id in" +
            "<foreach collection='userIds' item='item' open='(' separator=',' close=')'>" +
            "#{item}"+
            "</foreach>" +
            "and room_customer.identity=#{identity} limit #{pageNum},#{pageSize}\n" +
            "</script>")
    List<RoomInfo> findRoomUserList(@Param("userIds") List<String> list,@Param("identity")Integer identity,@Param("pageSize")Integer pageSize,@Param("pageNum")Integer pageNum);
    @Select( "<script>" +
            "select" +
            " user_id as userId,username as userName,mobile_phone as mobilePhone " +
            " from " +
            " sys_customer_info "+
            "where " +
            "user_id in \n" +
            "<foreach collection='userIdList' item='item' open='(' separator=',' close=')'>" +
            "#{item}"+
            "</foreach>" +
            "</script>"
    )
    List<UserDTO> findPhoneAndName(@Param("userIdList") List<String> list);

    @Select("select user_id from room_customer where room_code=#{roomCode} and " +
            "past_due = 0 and " +
            "logged_off_state = 0 and " +
            "identity=#{identity}")
    List<String> findUserIdByRoomCode(@Param("roomCode") String roomCode,@Param("identity") String identity);
    @Select("SELECT DISTINCT CONCAT((charge_name),'到期时间') AS chargeStr,MAX(due_time) AS endTime\n" +
            "FROM\n" +
            "original_bill\n" +
            "WHERE \n" +
            "invalid_state='0' and \n" +
            "state_of_arrears='0' AND\n" +
            "refund_status = '0' and\n" +
            "room_code = #{roomCode}\n" +
            "GROUP BY charge_name")
    List<ChargeMaxTimeDTO> findChargeMaxTime(String roomCode);
    @Select("select room_code from room_customer where room_code=#{roomCode} and user_id=#{userId} and " +
            "past_due = '0' and logged_off_state= '0'")
    List<String> findRoomPermission(@Param("roomCode") String roomCode,@Param("userId") String userId);
    @Select("select room_code from room_customer where and user_id=#{userId} and " +
            "past_due = '0' and logged_off_state= '0' and identity='1'")
    List<String> findRoomCodes(String userId);
    @Update("update sys_customer_info set mobile_phone=#{userPhone},sex=#{sex},surname=#{surname},udt=NOW() where " +
            "enabled = '1' and user_id=#{userId}")
    Integer updateUserInfo(@Param("userId") String userId,@Param("userPhone") String userPhone,@Param("sex") String sex,@Param("surname") String surname);

    @Select("select room_code as roomCode" +
            ",organization_id as organizationId," +
            "organization_name as organizationName from room_customer where customer_user_id=#{userId} and (identity='1' or identity='2') and " +
            "logged_off_state = '0' and (past_due = '0' or past_due is null)")
    List<GetRoomCodesDTO> getRoomCodesIdentity(@Param("userId") String userId, @Param("identity") String identity);

    @Select("<script>" +
            "select room_code as roomCode" +
            ",organization_id as organizationId," +
            "organization_name as organizationName," +
            "end_time as endTime," +
            "start_time  as startTime from room_customer where customer_user_id=#{userId} and identity='3' and " +
            "logged_off_state = '0' and past_due = '0' and room_code in " +
            "<foreach collection='roomCodes' item='item' open='(' close=')' separator=','>" +
            "#{item}"+
            "</foreach>" +
            "</script>")
    List<TenantuseRoomCostmer> getRoomCodesTenantuser(@Param("userId") String userId, @Param("roomCodes") List<String> roomCodes);
    @Insert( "<script>" +
            "insert into room_customer (identity,idt,room_code,start_time,customer_user_id,logged_off_state,past_due) values"+
            "<foreach collection='roomCodes' item='item' separator=','>" +
            "(#{identity},NOW(),#{item},#{startDate},#{userId},'0','0')"+
            "</foreach>" +
            "</script>")
    Integer addRoomUserInIndex(@Param("startDate")String startDate,@Param("identity") String identity,@Param("userId") String userId,@Param("roomCodes") List<String> roomCode);
    @Update("update sys_customer_info set udt=NOW(),sex=#{sex},mobile_phone=#{userPhone},surname=#{surName} where user_id=#{userId} and enabled = '1'")
    Integer updateUserInfoByUserId(@Param("userPhone")String userPhone,@Param("sex")String sex,@Param("userId")String userId,@Param("surName")String surName);
    @Insert("<script>" +
            "insert into room_customer (identity,idt,room_code,start_time,customer_user_id,logged_off_state,past_due,end_time) values" +
            "<foreach collection='roomCodes' item='item' separator=','>" +
            "('3',NOW(),#{item.roomCode},#{item.startDate},#{userId},'0','0',#{item.endDate})"+
            "</foreach>" +
            "</script>")
    Integer addTenantUser(@Param("userId")String userId, @Param("roomCodes")List<AddTenantUserRoomDTO> roomCodes);
    @Select("select DATE_FORMAT(end_time,'%Y-%m-%d') AS endTime,DATE_FORMAT(start_time,'%Y-%m-%d') AS startTime,room_code AS roomCode from room_customer where " +
            "logged_off_state = '0' and past_due= '0' and customer_user_id=#{userId} and identity='3' and room_code in (select room_code from room_customer where logged_off_state = '0' and (past_due= '0' or past_due is null) and " +
            "identity='1' and customer_user_id=#{mineUserId})")
    List<AddTenantUserRoomDTO> getTenantRoom(@Param("userId")String userId, @Param("mineUserId")String mine);

    @Select("select room_code from room_customer where identity = '3' and logged_off_state = '0' and past_due= '0' and user_id=#{userId} and room_code=#{roomCode}")
    String tenantRoomIsNull(@Param("roomCode")String roomCode,@Param("userId")String userId);
    @Update("update room_customer set udt=NOW(),start_time=#{userRoom.startDate},end_time=#{userRoom.endDate}" +
            "where logged_off_state = '0' and past_due= '0' and user_id=#{userId} and identity='3' and room_code=#{userRoom.roomCode}")
    Integer updateTenantRoom(@Param("userId")String userId,@Param("userRoom") AddTenantUserRoomDTO addTenantUserRoomDTO);
    @Insert("insert into room_customer (identity,idt,room_code,start_time,user_id,logged_off_state,past_due,end_time) values" +
            "('3',NOW(),#{userRoom.roomCode},#{userRoom.startDate},#{userId},'0','0',#{userRoom.endDate})")
    Integer addTenantRoom(@Param("userId")String userId,@Param("userRoom") AddTenantUserRoomDTO addTenantUserRoomDTO);
    @Select("SELECT\n" +
            "sys_customer_info.surname AS userName,\n" +
            "sys_customer_info.mobile_phone AS phone\n," +
            "room_customer.idt AS startDate\n " +
            "FROM\n" +
            "room_customer\n" +
            "INNER JOIN sys_customer_info ON sys_customer_info.user_id = room_customer.customer_user_id\n" +
            "WHERE\n" +
            "room_customer.logged_off_state = '0' AND\n" +
            "room_customer.identity = '1' AND (room_customer.past_due = '0' or room_customer.past_due is null) AND room_customer.room_code=#{roomCode}")
    List<PorpertyDTO> getPerporty(String roomCode);
    @Select("select mobile_phone from sys_customer_info where " +
            "enabled='1' and user_id=#{userId}")
    String getPhoneByUserId(String userId);

    @Insert( "<script>" +
            "insert into room_customer (identity,idt,room_code,start_time,user_id) values " +
            "<foreach collection='roomCodes' item='item' separator=','>" +
            "(#{identity},NOW(),#{item.roomCode},#{item.startDate},#{userId})"+
            "</foreach>" +
            "</script>")
    Integer addRoomUser(@Param("roomCodes") List<UpdateRoomUserDTO> roomCodes, @Param("userId")String userId, @Param("identity")String identity);

    @Insert(
            "insert into room_customer (end_time,identity,idt,logged_off_state,organization_id,organization_name,past_due,room_code,start_time,customer_user_id,normal_payment_status,real_recycling_home_time,start_billing_time) values " +
            "(#{roomCodes.endTime},#{identity},NOW(),'0',#{roomCodes.organizationId},#{roomCodes.organizationName},'0',#{roomCodes.roomCode},#{roomCodes.startTime},#{userId},'1',#{roomTime.realRecyclingHomeTime},#{roomTime.startBillingTime})"

          )
    void insertNewRoomCustomer2(@Param("userId")String userId
            ,@Param("roomCodes")UpdateRoomCustomerRequestEntity roomCodes
            ,@Param("identity")String identity
            ,@Param("roomTime")RoomTimeDTO roomTimeDTO);
    @Insert(
            "insert into room_customer (end_time,identity,idt,logged_off_state,organization_id,organization_name,past_due,room_code,start_time,customer_user_id,normal_payment_status,start_billing_time) values " +
                    "(#{roomCodes.endTime},#{identity},NOW(),'0',#{roomCodes.organizationId},#{roomCodes.organizationName},'0',#{roomCodes.roomCode},#{roomCodes.startTime},#{userId},'1',#{roomCodes.startTime})"

    )
    void insertNewRoomCustomer3(@Param("userId")String userId
            ,@Param("roomCodes")UpdateRoomCustomerRequestEntity roomCodes
            ,@Param("identity")String identity
           );
    @Update("update room_customer set logged_off_state='1',past_due='1' where " +
            "room_code=#{roomCode} and customer_user_id=#{userId} and identity=#{identity}")
    void deleteRoomCustomer(@Param("userId")String userId,
                            @Param("identity")String identity,
                            @Param("roomCode")String roomCode);
}
