package com.rbi.interactive.dao;

import com.rbi.interactive.entity.VehicleInformationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IVehicleInformationDAO {

    @Select("SELECT * FROM vehicle_info WHERE organization_id = #{organizationId} LIMIT ${pageNo},${pageSize}")
    public List<VehicleInformationDO> findByPage(@Param("organizationId") String organizationId,
                                                 @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    @Select("SELECT Count(*) FROM vehicle_info WHERE organization_id = #{organizationId}")
    public Integer findCount(@Param("organizationId") String organizationId);

}
