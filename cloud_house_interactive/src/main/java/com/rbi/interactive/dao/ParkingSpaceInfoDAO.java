package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkingSpaceInfoDAO extends JpaRepository<ParkingSpaceInfoDO,Integer>, JpaSpecificationExecutor<ParkingSpaceInfoDO> {

    List<ParkingSpaceInfoDO> findByRegionCodeAndVehicleCapacityGreaterThan(String regionCode,int vehicleCapacity);

    ParkingSpaceInfoDO findByParkingSpaceInfoId(int id);

    @Override
    <S extends ParkingSpaceInfoDO> S saveAndFlush(S s);

    @Transactional
    void deleteByParkingSpaceInfoIdIn(List<Integer> ids);

    void deleteByParkingSpaceInfoId(Integer id);

    ParkingSpaceInfoDO findAllByParkingSpaceCodeAndOrganizationId(String parkingSpaceCode,String organizationId);

    int countAllByParkingSpaceCode(String parkingSpaceCode);

    ParkingSpaceInfoDO findAllByParkingSpaceCode(String parkingSpaceCode);


    int countByParkingSpaceCode(String parkingSpaceCode);

}
