package com.rbi.interactive.dao;

import com.rbi.interactive.entity.dto.ChargeItemBackDTO;
import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IFrontOfficeCashierDao {

    /**
     * 分页查询话费基础信息
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} and rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}') LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouse(@Param("organizationId") String organizationId,@Param("villageCodes") String villageCodes,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);
    /**
     * 查询基础信息总条数
     * @return
     */
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rc.identity='1' and rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}')")
    Integer findCount(@Param("organizationId") String organizationId,@Param("villageCodes") String villageCodes);

    /**
     * 根据手机号模糊搜索
     * @param organizationId
     * @param mobilePhone
     * @param villageCodes
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}') LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByMobilePhone(@Param("organizationId") String organizationId,
                                                              @Param("mobilePhone") String mobilePhone,
                                                              @Param("villageCodes") String villageCodes,
                                                              @Param("pageNo") int pageNo,
                                                              @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}')")
    Integer findHouseByMobilePhoneCount(@Param("organizationId") String organizationId,
                                               @Param("mobilePhone") String mobilePhone,
                                               @Param("villageCodes") String villageCodes);


    /**
     * 根据省份证号模糊搜索
     * @param organizationId
     * @param idNumber
     * @param villageCodes
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND sci.id_number like '%${idNumber}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}') LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByIdNumber(@Param("organizationId") String organizationId,
                                                       @Param("idNumber") String idNumber,
                                                       @Param("villageCodes") String villageCodes,
                                                       @Param("pageNo") int pageNo,
                                                       @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND sci.id_number like '%${idNumber}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}')")
    Integer findHouseByIdNumberCount(@Param("organizationId") String organizationId,
                                        @Param("idNumber") String idNumber,
                                        @Param("villageCodes") String villageCodes);



    /**
     * 根据业主姓名模糊搜索
     * @param organizationId
     * @param surname
     * @param villageCodes
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND sci.surname like '%${surname}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}') LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseBySurname(@Param("organizationId") String organizationId,
                                                    @Param("surname") String surname,
                                                    @Param("villageCodes") String villageCodes,
                                                    @Param("pageNo") int pageNo,
                                                    @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND sci.surname like '%${surname}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}')")
    Integer findHouseBySurnameCount(@Param("organizationId") String organizationId,
                                     @Param("surname") String surname,
                                     @Param("villageCodes") String villageCodes);




    /**
     * 根据房间号搜索
     * @param organizationId
     * @param roomCode
     * @param villageCodes
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rc.room_code like '%${roomCode}%' AND " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}') LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByRoomCodePage(@Param("organizationId") String organizationId,
                                                              @Param("roomCode") String roomCode,
                                                               @Param("villageCodes") String villageCodes,
                                                              @Param("pageNo") int pageNo,
                                                              @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rc.room_code like '%${roomCode}%' AND rc.identity='1' and " +
            "rc.logged_off_state = 0 and rv.village_code in ('${villageCodes}')")
    Integer findHouseByRoomCodeCount(@Param("organizationId") String organizationId,
                                            @Param("roomCode") String roomCode,
                                            @Param("villageCodes") String villageCodes);

    /**
     * 根据小区搜索
     * @param organizationId
     * @param villageCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCode(@Param("organizationId") String organizationId,
                                                               @Param("villageCode") String villageCode,
                                                               @Param("pageNo") int pageNo,
                                                               @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeCount(@Param("organizationId") String organizationId,
                                                @Param("villageCode") String villageCode);

    /**
     * 根据小区和手机号搜索
     * @param organizationId
     * @param villageCode
     * @param mobilePhone
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND sci.mobile_phone like '%${mobilePhone}%' " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndMobilePhone(@Param("organizationId") String organizationId,
                                                              @Param("villageCode") String villageCode,
                                                              @Param("mobilePhone") String mobilePhone,
                                                              @Param("pageNo") int pageNo,
                                                              @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                               @Param("villageCode") String villageCode,@Param("mobilePhone") String mobilePhone);

    /**
     * 根据小区地块查询
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCode(@Param("organizationId") String organizationId,
                                                              @Param("villageCode") String villageCode,
                                                              @Param("regionCode") String regionCode,
                                                              @Param("pageNo") int pageNo,
                                                              @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeCount(@Param("organizationId") String organizationId,
                                               @Param("villageCode") String villageCode,@Param("regionCode") String regionCode);

    /**
     * 根据小区地块手机号查询
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param mobilePhone
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,rc.customer_user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} AND sci.mobile_phone like '%${mobilePhone}%' " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndMobilePhone(@Param("organizationId") String organizationId,
                                                                           @Param("villageCode") String villageCode,
                                                                           @Param("regionCode") String regionCode,
                                                                           @Param("mobilePhone") String mobilePhone,
                                                                           @Param("pageNo") int pageNo,
                                                                           @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                            @Param("villageCode") String villageCode,@Param("regionCode") String regionCode,@Param("mobilePhone") String mobilePhone);

    /**
     * 根据小区地块楼宇查询
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param buildingCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndBuildingCode(@Param("organizationId") String organizationId,
                                                                           @Param("villageCode") String villageCode,
                                                                           @Param("regionCode") String regionCode,
                                                                           @Param("buildingCode") String buildingCode,
                                                                           @Param("pageNo") int pageNo,
                                                                           @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndBuildingCodeCount(@Param("organizationId") String organizationId,
                                                            @Param("villageCode") String villageCode,
                                                            @Param("regionCode") String regionCode,
                                                            @Param("buildingCode") String buildingCode);

    /**
     * 根据小区地块楼宇手机查询
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param buildingCode
     * @param mobilePhone
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND sci.mobile_phone like '%${mobilePhone}%' " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndMobilePhone(@Param("organizationId") String organizationId,
                                                                                         @Param("villageCode") String villageCode,
                                                                                         @Param("regionCode") String regionCode,
                                                                                         @Param("buildingCode") String buildingCode,
                                                                                         @Param("mobilePhone") String mobilePhone,
                                                                                         @Param("pageNo") int pageNo,
                                                                                         @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                                          @Param("villageCode") String villageCode,@Param("regionCode") String regionCode,
                                                                          @Param("buildingCode") String buildingCode,@Param("mobilePhone") String mobilePhone);

    /**
     * 根据小区地块楼宇单元查询
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param buildingCode
     * @param unitCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCode(@Param("organizationId") String organizationId,
                                                                                          @Param("villageCode") String villageCode,
                                                                                          @Param("regionCode") String regionCode,
                                                                                          @Param("buildingCode") String buildingCode,
                                                                                          @Param("unitCode") String unitCode,
                                                                                          @Param("pageNo") int pageNo,
                                                                                          @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeCount(@Param("organizationId") String organizationId,
                                                                           @Param("villageCode") String villageCode,
                                                                           @Param("regionCode") String regionCode,
                                                                           @Param("buildingCode") String buildingCode,
                                                                           @Param("unitCode") String unitCode);

    /**
     * 根据小区地块楼宇单元手机搜索
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param buildingCode
     * @param unitCode
     * @param mobilePhone
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} AND sci.mobile_phone like '%${mobilePhone}%' " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndMobilePhone(@Param("organizationId") String organizationId,
                                                                                                        @Param("villageCode") String villageCode,
                                                                                                        @Param("regionCode") String regionCode,
                                                                                                        @Param("buildingCode") String buildingCode,
                                                                                                        @Param("unitCode") String unitCode,
                                                                                                        @Param("mobilePhone") String mobilePhone,
                                                                                                        @Param("pageNo") int pageNo,
                                                                                                        @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                                                         @Param("villageCode") String villageCode,@Param("regionCode") String regionCode,
                                                                                         @Param("buildingCode") String buildingCode,@Param("unitCode") String unitCode,
                                                                                         @Param("mobilePhone") String mobilePhone);


    /**
     * 根据小区地块楼宇单元房号搜索
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param buildingCode
     * @param unitCode
     * @param roomCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} And rc.room_code = #{roomCode} " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomCode(@Param("organizationId") String organizationId,
                                                                                                     @Param("villageCode") String villageCode,
                                                                                                     @Param("regionCode") String regionCode,
                                                                                                     @Param("buildingCode") String buildingCode,
                                                                                                     @Param("unitCode") String unitCode,
                                                                                                     @Param("roomCode") String roomCode,
                                                                                                     @Param("pageNo") int pageNo,
                                                                                                     @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} And rc.room_code = #{roomCode} AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomCodeCount(@Param("organizationId") String organizationId,
                                                                                      @Param("villageCode") String villageCode,
                                                                                      @Param("regionCode") String regionCode,
                                                                                      @Param("buildingCode") String buildingCode,
                                                                                      @Param("unitCode") String unitCode,
                                                                                      @Param("roomCode") String roomCode);

    /**
     * 根据小区地块楼宇单元房号手机搜索
     * @param organizationId
     * @param villageCode
     * @param regionCode
     * @param buildingCode
     * @param unitCode
     * @param roomCode
     * @param mobilePhone
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code,rc.identity," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname,sci.sex,sci.id_number,rc.surplus FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} And rc.room_code = #{roomCode} AND sci.mobile_phone like '%${mobilePhone}%' " +
            "and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    List<HouseAndProprietorDTO> findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomAndMobilePhone(@Param("organizationId") String organizationId,
                                                                                                                   @Param("villageCode") String villageCode,
                                                                                                                   @Param("regionCode") String regionCode,
                                                                                                                   @Param("buildingCode") String buildingCode,
                                                                                                                   @Param("unitCode") String unitCode,
                                                                                                                   @Param("roomCode") String roomCode,
                                                                                                                   @Param("mobilePhone") String mobilePhone,
                                                                                                                   @Param("pageNo") int pageNo,
                                                                                                                   @Param("pageSize") int pageSize);
    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND rv.village_code = #{villageCode} AND rrn.region_code = #{regionCode} " +
            "AND ru.building_code = #{buildingCode} AND ru.unit_code = #{unitCode} And rc.room_code = #{roomCode} AND sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' " +
            "and rc.logged_off_state = 0")
    Integer findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                                                                    @Param("villageCode") String villageCode,@Param("regionCode") String regionCode,
                                                                                                    @Param("buildingCode") String buildingCode,@Param("unitCode") String unitCode,
                                                                                                    @Param("roomCode") String roomCode,@Param("mobilePhone") String mobilePhone);


    /**
     * 根据房间号查询房间信息
     * @param roomCode
     * @param organizationId
     * @return
     */
    @Select("SELECT rc.organization_id,rc.organization_name,rc.start_billing_time,rc.quarterly_cycle_time,rv.village_code,rv.village_name,rrn.region_code," +
            "rrn.region_name,rb.building_code,rb.building_name,ru.unit_code,ru.unit_name,rc.room_code," +
            "rr.room_size,rr.room_type,sci.user_id as customerUserId,sci.mobile_phone,sci.surname FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON rr.unit_code " +
            "= ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code INNER JOIN " +
            "regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village AS rv ON " +
            "rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rr.room_code = #{roomCode} AND rc.organization_id=#{organizationId} AND rc.identity='1' and rc.logged_off_state = 0")
    List<HouseAndProprietorDTO> findHouseByRoomCode(@Param("roomCode") String roomCode,@Param("organizationId") String organizationId);




    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND ru.building_code = #{buildingCode} and sci.mobile_phone " +
            "like '%${mobilePhone}%' AND rc.identity='1' and rc.logged_off_state = 0")
    Integer findHouseByBuildingCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                                             @Param("buildingCode") String buildingCode,
                                                                             @Param("mobilePhone") String mobilePhone);


    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND ru.building_code = #{buildingCode} and rr.unit_code = " +
            "#{unitCode} AND rc.identity='1' and rc.logged_off_state = 0")
    Integer findHouseByBuildingCodeAndUnitCodeCount(@Param("organizationId") String organizationId,
                                                                          @Param("buildingCode") String buildingCode,
                                                                          @Param("unitCode") String unitCode);

    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND ru.building_code = #{buildingCode} and rr.unit_code = " +
            "#{unitCode} and sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' and rc.logged_off_state = 0 LIMIT ${pageNo},${pageSize}")
    Integer findHouseByBuildingCodeAndUnitCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                                                        @Param("buildingCode") String buildingCode,
                                                                                        @Param("unitCode") String unitCode,
                                                                                        @Param("mobilePhone") String mobilePhone);

    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND ru.building_code = #{buildingCode} and rr.unit_code = " +
            "#{unitCode} AND rc.room_code = #{roomCode} AND rc.identity='1' and rc.logged_off_state = 0")
    Integer findHouseByBuildingCodeAndUnitCodeAndRoomCodeCount(@Param("organizationId") String organizationId,
                                                                                     @Param("buildingCode") String buildingCode,
                                                                                     @Param("unitCode") String unitCode,
                                                                                     @Param("roomCode") String roomCode);

    @Select("SELECT COUNT(*) FROM room_customer AS rc INNER JOIN " +
            "regional_room AS rr ON rc.room_code=rr.room_code INNER JOIN regional_unit AS ru ON " +
            "rr.unit_code = ru.unit_code INNER JOIN regional_building AS rb ON ru.building_code=rb.building_code " +
            "INNER JOIN regional_region AS rrn ON rb.region_code = rrn.region_code INNER JOIN regional_village " +
            "AS rv ON rrn.village_code = rv.village_code INNER JOIN sys_customer_info AS sci ON rc.customer_user_id = sci.user_id " +
            "WHERE rc.organization_id=#{organizationId} AND ru.building_code = #{buildingCode} and rr.unit_code = " +
            "#{unitCode} AND rc.room_code = #{roomCode} and sci.mobile_phone like '%${mobilePhone}%' AND rc.identity='1' and rc.logged_off_state = 0")
    Integer findHouseByBuildingCodeAndUnitCodeAndRoomCodeAndMobilePhoneCount(@Param("organizationId") String organizationId,
                                                                                    @Param("buildingCode") String buildingCode,
                                                                                    @Param("unitCode") String unitCode,
                                                                                    @Param("roomCode") String roomCode,
                                                                                    @Param("mobilePhone") String mobilePhone);





//    @Select("SELECT COUNT(*) FROM room_customer WHERE identity = '1' and organization_id = #{organizationId} and logged_off_state = 0")
//    public Integer findCount(@Param("organizationId") String organizationId);
//
//    @Select("SELECT COUNT(*) FROM room_customer WHERE identity = '1' and organization_id = #{organizationId} and logged_off_state = 0")
//    public Integer findCount(@Param("organizationId") String organizationId);
//
//    @Select("SELECT COUNT(*) FROM room_customer WHERE identity = '1' and organization_id = #{organizationId} and logged_off_state = 0")
//    public Integer findCount(@Param("organizationId") String organizationId);
//
//    @Select("SELECT COUNT(*) FROM room_customer WHERE identity = '1' and organization_id = #{organizationId} and logged_off_state = 0")
//    public Integer findCount(@Param("organizationId") String organizationId);

    /**
     * 根据房间编号查询物业费到期时间
     * @param roomCode
     * @return
     */

    @Select("SELECT rci.charge_code FROM room_charge_items AS rci INNER JOIN " +
            "charge_item AS ci ON rci.charge_code=ci.charge_code " +
            "WHERE room_code=#{roomCode}")
    List<String> findChargeCodesByRoomCodeAndChargeStatus(@Param("roomCode") String roomCode,@Param("chargeStatus") int chargeStatus);

    @Select("SELECT MAX(bd.due_time) FROM bill INNER JOIN bill_detailed AS bd ON bill.order_id = bd.order_id WHERE bill.room_code = #{roomCode} AND " +
            "bill.organization_id=#{organizationId} AND bd.charge_type IN (1,2,3) and bill.invalid_state=0 and bd.split_state<>1")
    String findMaxDueTime(@Param("roomCode") String roomCode,
                                 @Param("organizationId") String organizationId);

    @Select("SELECT ci.charge_code,ci.charge_name,ci.charge_type,ci.charge_way,ci.charge_standard " +
            "FROM room_charge_items AS rci INNER JOIN charge_item AS ci ON " +
            "rci.charge_code = ci.charge_code WHERE room_code = #{roomCode} ORDER BY ci.sort_state,ci.charge_type")
    List<ChargeItemBackDTO> findChargeCodeList(String roomCode);

    @Select("SELECT charge_code,charge_name,charge_type,charge_way,charge_standard \n" +
            "FROM charge_item WHERE organization_id = #{organizationId} and charge_type=6 and enable = 1")
    ChargeItemBackDTO findParkingManagementChargeItem(@Param("organizationId") String organizationId);

}
