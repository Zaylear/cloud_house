package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.edo.RoomDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChartDAO {
    @Select("select * from regional_room where room_code = #{roomCode}")
    RoomDO findRoomByRoomCode(@Param("roomCode")String roomCode);



    @Select("SELECT SUM(bt.actual_money_collection)\n" +
            "FROM \n" +
            "bill AS b,bill_detailed AS bt WHERE b.order_id = bt.order_id\n" +
            "AND b.room_code = #{roomCode}")
    Double totalSum(@Param("roomCode")String roomCode);

    @Select("SELECT SUM(bt.actual_money_collection)\n" +
            "FROM \n" +
            "bill AS b,bill_detailed AS bt WHERE b.order_id = bt.order_id\n" +
            "AND bt.charge_code =(SELECT charge_code FROM charge_item WHERE charge_type = #{chargeType} AND organization_id = #{organizationId})\n" +
            "AND b.room_code = #{roomCode} ")
    Double singleSum(@Param("chargeType")String chargeType,@Param("organizationId")String organizationId,
                 @Param("roomCode")String roomCode);



    @Select("SELECT SUM(bt.actual_money_collection)\n" +
            "FROM \n" +
            "bill AS b,bill_detailed AS bt WHERE b.order_id = bt.order_id\n" +
            "AND b.room_code = #{roomCode} AND YEAR(b.idt) = YEAR(NOW())")
    Double yeartotalSum(@Param("roomCode")String roomCode);

    @Select("SELECT SUM(bt.actual_money_collection)\n" +
            "FROM \n" +
            "bill AS b,bill_detailed AS bt WHERE b.order_id = bt.order_id\n" +
            "AND bt.charge_code =(SELECT charge_code FROM charge_item WHERE charge_type = #{chargeType} AND organization_id = #{organizationId})\n" +
            "AND b.room_code = #{roomCode} AND YEAR(b.idt) = YEAR(NOW())")
    Double yearSum(@Param("chargeType")String chargeType,@Param("organizationId")String organizationId,
                     @Param("roomCode")String roomCode);


    @Select("SELECT SUM(#{chargeType})\n" +
            "FROM \n" +
            "history_data WHERE room_code = #{roomCode} AND organization_id = #{organizationId}")
    Double PastSingleSum(@Param("chargeType")String chargeType, @Param("roomCode")String roomCode,
                         @Param("organizationId")String organizationId);





}
