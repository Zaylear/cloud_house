package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkingSpaceManagementDAO extends JpaRepository<ParkingSpaceManagementDO,Integer>, JpaSpecificationExecutor<ParkingSpaceManagementDO> {

    List<ParkingSpaceManagementDO> findByOrganizationIdAndRoomCode(String organizationId, String roomCode);

    ParkingSpaceManagementDO findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(String parkingSpaceCode,String organizationId,int loggedOffState);

    int countByParkingSpaceCode(String parkingSpaceCode);

    int countByContractNumber(String contractNumber);

    ParkingSpaceManagementDO findAllByParkingSpaceManagementId(int parkingSpaceManagementId);

    ParkingSpaceManagementDO findByParkingSpaceCode(String parkingSpaceCode);

    @Transactional
    void deleteByParkingSpaceManagementIdIn(List<Integer> ids);



}
