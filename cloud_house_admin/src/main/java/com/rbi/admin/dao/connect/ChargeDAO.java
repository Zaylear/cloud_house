package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.dto.ChargeCodesDTO;
import com.rbi.admin.entity.dto.PropertyChargeDTO;
import com.rbi.admin.entity.edo.ChargeDetailDO;
import com.rbi.admin.entity.edo.ChargeItemDO;
import com.rbi.admin.entity.edo.RoomAndChargeItemsDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChargeDAO{

    @Select("SELECT charge_item.organization_id AS organizationId,organization_name AS organizationName,\n" +
            "charge_item.charge_code AS chargeCode,charge_item.charge_name AS chargeName,\n" +
            "charge_unit AS chargeUnit,charge_standard AS chargeStandard,datedif,discount,charge_way as chargeWay \n" +
            "FROM charge_detail,charge_item,organization \n" +
            "WHERE charge_detail.charge_code = charge_item.charge_code \n" +
            "AND charge_item.organization_id = organization.organization_id\n" +
            "AND charge_item.charge_code = #{chargeCode} \n" +
            "AND datedif = #{datedif} charge_item.`enable`=1")
    public PropertyChargeDTO findPropertyCharge(@Param("chargeCode") String chargeCode, @Param("datedif") int datedif);

    @Select("select i.id, i.organization_id AS organizationId,i.charge_code AS chargeCode,\n" +
            "i.charge_name AS chargeName,i.charge_type AS chargeType,i.charge_unit AS chargeUnit,\n" +
            "i.charge_standard AS chargeStandard,i.charge_way as chargeWay,i.enable,idt,udt from charge_item AS i " +
            "WHERE i.`enable` = 1 " +
            "AND i.charge_code IN (${chargeCodes})")
    public List<ChargeItemDO> findByChargeCodes(@Param("chargeCodes") String chargeCodes);

    @Select("select charge_code,charge_name from charge_item where organization_id = #{organizationId}")
    public List<ChargeCodesDTO> chooseChargeCode(@Param("organizationId") String organizationId);


    @Select("SELECT " +
            "charge_item.charge_code,\n" +
            "charge_item.organization_id,\n" +
            "charge_item.charge_name,\n" +
            "charge_item.charge_type,\n" +
            "charge_item.charge_unit,\n" +
            "charge_item.charge_standard,\n" +
            "charge_item.must_pay,\n" +
            "charge_item.`enable`,\n" +
            "charge_item.idt,\n" +
            "charge_item.udt,\n" +
            "charge_item.charge_way,\n" +
            "charge_item.refund\n" +
            "FROM charge_item where charge_item.charge_code = #{chargeCode} ")
    ChargeItemDO  findChargeItem(@Param("chargeCode")String chargeCode);

    @Select("SELECT " +
            "charge_detail.id,\n" +
            "charge_detail.area_max,\n" +
            "charge_detail.area_min,\n" +
            "charge_detail.datedif,\n" +
            "charge_detail.discount,\n" +
            "charge_detail.money,\n" +
            "charge_detail.parking_space_place,\n" +
            "charge_detail.parking_space_type\n" +
            "FROM charge_detail where charge_detail.charge_code = #{chargeCode} ")
    List<ChargeDetailDO>  findChargeDetail(@Param("chargeCode")String chargeCode);




    @Insert("insert into charge_item (organization_id,charge_code,charge_name ,charge_type, " +
            "charge_unit,charge_standard,charge_way,refund,must_pay,enable,idt,sort_state)values(#{organizationId}," +
            "#{chargeCode},#{charge_Name}," +
            "#{chargeType},#{chargeUnit},#{chargeStandard},#{chargeWay},#{refund},#{mustPay},#{enable},#{idt},#{sortState})")
    void addItem(@Param("organizationId")String organizationId,@Param("chargeCode")String chargeCode,@Param("charge_Name")String charge_Name,
                        @Param("chargeType")Integer chargeType,@Param("chargeUnit")String chargeUnit ,@Param("chargeStandard")Double chargeStandard,
                        @Param("chargeWay")Integer chargeWay,@Param("refund")Integer refund,@Param("mustPay")Integer mustPay,@Param("enable")Integer enable,
                        @Param("idt")String idt,@Param("sortState")Integer sortState);

    @Insert("insert into charge_detail (charge_code,area_max,area_min ,money, " +
            "datedif,discount,parking_space_place,parking_space_type)values(#{chargeCode}," +
            "#{areaMax},#{areaMin},#{money},#{datedif},#{discount},#{parkingSpacePlace},#{parkingSpaceType})")
    void addDetail(@Param("chargeCode")String chargeCode,@Param("areaMax")Integer areaMax,
                          @Param("areaMin")Integer areaMin,@Param("money")Double money ,
                          @Param("datedif")Integer datedif,@Param("discount")Double discount,
                          @Param("parkingSpacePlace")String parkingSpacePlace,@Param("parkingSpaceType")String parkingSpaceType);


    @Update("update charge_item set charge_name = #{chargeName} ,charge_type =#{chargeType}, " +
            "charge_unit =#{chargeUnit},charge_standard = #{chargeStandard}," +
            "charge_way = #{chargeWay},refund =#{refund},must_pay=#{mustPay},enable =#{enable}," +
            "udt = #{udt} where charge_code = #{chargeCode} ")
    void updateItem(@Param("chargeName")String charge_Name,
                        @Param("chargeType")Integer chargeType,@Param("chargeUnit")String chargeUnit ,@Param("chargeStandard")Double chargeStandard,
                        @Param("chargeWay")Integer chargeWay,@Param("refund")Integer refund,@Param("mustPay")Integer mustPay,@Param("enable")Integer enable,
                        @Param("udt")String udt,@Param("chargeCode")String chargeCode);

    @Update("update charge_detail set area_max=#{areaMax},area_min=#{areaMin} ,money=#{money}, " +
            "datedif=#{datedif},discount=#{discount},parking_space_place=#{parkingSpacePlace},parking_space_type=#{parkingSpaceType}" +
            "  where id = #{id}")
    void updateDetail(@Param("areaMax")Integer areaMax, @Param("areaMin")Integer areaMin,
                             @Param("money")Double money , @Param("datedif")Integer datedif,
                             @Param("discount")Double discount, @Param("parkingSpacePlace")String parkingSpacePlace,
                             @Param("parkingSpaceType")String parkingSpaceType,@Param("id")Integer id);


    @Select("SELECT\n" +
            "organization.organization_id,\n" +
            "organization.organization_name,\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_status\n" +
            "FROM regional_village,regional_region,regional_building,regional_unit,regional_room,organization " +
            "WHERE \n" +
            "organization.organization_id = regional_village.organization_id AND\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code and\n" +
            "regional_unit.unit_code = regional_room.unit_code AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_type = 1")
    List<RoomAndChargeItemsDO> findHouseRoomByOrganizationId(@Param("organizationId")String organizationId);

    @Select("SELECT\n" +
            "organization.organization_id,\n" +
            "organization.organization_name,\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_status\n" +
            "FROM regional_village,regional_region,regional_building,regional_unit,regional_room,organization " +
            "WHERE \n" +
            "organization.organization_id = regional_village.organization_id AND\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code and\n" +
            "regional_unit.unit_code = regional_room.unit_code AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_type = 3")
    List<RoomAndChargeItemsDO> findBusinessHouseRoomByOrganizationId(@Param("organizationId")String organizationId);

    @Select("SELECT\n" +
            "organization.organization_id,\n" +
            "organization.organization_name,\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_status\n" +
            "FROM regional_village,regional_region,regional_building,regional_unit,regional_room,organization " +
            "WHERE \n" +
            "organization.organization_id = regional_village.organization_id AND\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code and\n" +
            "regional_unit.unit_code = regional_room.unit_code AND\n" +
            "regional_village.organization_id = #{organizationId} AND regional_room.room_type = 2")
    List<RoomAndChargeItemsDO> findWorkRoomByOrganizationId(@Param("organizationId")String organizationId);

    @Select("SELECT\n" +
            "organization.organization_id,\n" +
            "organization.organization_name,\n" +
            "regional_village.village_name,\n" +
            "regional_village.village_code,\n" +
            "regional_region.region_name,\n" +
            "regional_region.region_code,\n" +
            "regional_building.building_name,\n" +
            "regional_building.building_code,\n" +
            "regional_unit.unit_name,\n" +
            "regional_unit.unit_code,\n" +
            "regional_room.room_code,\n" +
            "regional_room.room_status\n" +
            "FROM regional_village,regional_region,regional_building,regional_unit,regional_room,organization " +
            "WHERE \n" +
            "organization.organization_id = regional_village.organization_id AND\n" +
            "regional_village.village_code = regional_region.village_code AND\n" +
            "regional_region.region_code = regional_building.region_code AND\n" +
            "regional_building.building_code = regional_unit.building_code and\n" +
            "regional_unit.unit_code = regional_room.unit_code AND\n" +
            "regional_village.organization_id = #{organizationId}")
    List<RoomAndChargeItemsDO> findRoomByOrganizationId(@Param("organizationId")String organizationId);

    @Delete("DELETE from room_charge_items WHERE charge_code in ${chargeCode}")
    void deleRoomItem(@Param("chargeCode")String chargeCode);

    @Select("SELECT count(*) FROM charge_item where charge_type = #{chargeType} AND organization_id = #{organizationId}")
    int findChargeItemNum(@Param("chargeType")Integer chargeType,@Param("organizationId")String organizationId);

    @Select("SELECT count(*) FROM charge_item where charge_code = #{chargeCode} AND organization_id = #{organizationId}")
    int findChargeCodeNum(@Param("chargeCode")String chargeCode,@Param("organizationId")String organizationId);

    @Select("select * from charge_item where organization_id = #{organizationId} AND charge_name LIKE ${chargeName} Limit #{pageNo},#{pageSize}")
    List<ChargeItemDO> findByChargeName(@Param("organizationId")String organizationId,@Param("chargeName")String chargeName,
                                        @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Select("select count(*) from charge_item where organization_id = #{organizationId} AND charge_name LIKE ${chargeName}")
    int findNum2(@Param("organizationId")String organizationId,@Param("chargeName")String chargeName);


}
