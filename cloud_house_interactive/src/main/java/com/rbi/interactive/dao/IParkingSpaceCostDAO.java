package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import com.rbi.interactive.entity.dto.RealGenerationTimeAndContractNumberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IParkingSpaceCostDAO {

    @Select("SELECT MAX(pscd.due_time) FROM bill INNER JOIN parking_space_cost_detail AS pscd ON bill.order_id=pscd.order_id " +
            "WHERE pscd.parking_space_code = #{parkingSpaceCode} AND pscd.organization_id=#{organizationId} and pscd.charge_type='6' AND bill.invalid_state=0")
    String findExclusiveParkingSpaceDueTime(@Param("parkingSpaceCode") String parkingSpaceCode,
                                            @Param("organizationId") String organizationId);

    @Select("SELECT start_time FROM parking_space_management " +
            "WHERE parking_space_code = #{parkingSpaceCode} AND organization_id=#{organizationId}")
    String findExclusiveParkingSpaceStartTime(@Param("parkingSpaceCode") String parkingSpaceCode,
                                            @Param("organizationId") String organizationId);

    @Select("SELECT pscd.* FROM parking_space_cost_detail AS pscd INNER JOIN bill ON pscd.order_id = bill.order_id " +
            "WHERE license_plate_number=#{licensePlateNumber} AND bill.room_code=#{roomCode} AND bill.organization_id=#{organizationId} and bill.invalid_state = 0 ORDER BY due_time DESC LIMIT 1")
    ParkingSpaceCostDetailDO findRentalParkingSpaceDueTime(@Param("licensePlateNumber") String licensePlateNumber, @Param("roomCode") String roomCode, @Param("organizationId") String organizationId);

    //查询当天租赁车位的数量
    @Select("SELECT COUNT(*) FROM bill AS b INNER JOIN parking_space_cost_detail AS pscd ON b.order_id=pscd.order_id " +
            "WHERE pscd.organization_id = #{organizationId} AND b.real_generation_time LIKE #{realGenerationTime} AND pscd.charge_type = '5' " +
            "AND pscd.rental_renewal_status=0 and b.invalid_state=0")
    int queryNonRentalParkingSpaceCountBySameDay(@Param("organizationId") String organizationId,@Param("realGenerationTime") String realGenerationTime);

    //查询该客户续租次数
    @Select("SELECT COUNT(*) FROM bill AS b INNER JOIN parking_space_cost_detail AS pscd ON b.order_id=pscd.order_id WHERE pscd.organization_id = #{organizationId} AND pscd.charge_type = '5' AND pscd.rental_renewal_status=1 AND b.room_code=#{roomCode} AND pscd.license_plate_number=#{licensePlateNumber} and b.invalid_state=0")
    int queryRentalParkingSpaceCount(@Param("organizationId") String organizationId,@Param("roomCode") String roomCode,@Param("licensePlateNumber") String licensePlateNumber);

    //查询该客户首租合同编号
    @Select("SELECT MAX(b.real_generation_time),pscd.contract_number FROM bill AS b INNER JOIN parking_space_cost_detail AS pscd ON b.order_id=pscd.order_id WHERE pscd.organization_id = #{organizationId} AND pscd.charge_type = '5' AND pscd.rental_renewal_status = 0 AND b.room_code=#{roomCode} AND pscd.license_plate_number=#{licensePlateNumber} and b.invalid_state=0")
    RealGenerationTimeAndContractNumberDTO queryContractNumber(@Param("organizationId") String organizationId,@Param("roomCode") String roomCode,@Param("licensePlateNumber") String licensePlateNumber);
}
