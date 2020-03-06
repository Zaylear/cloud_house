package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.ParkingSpaceManagementDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ParkingSpaceManagementDAO extends JpaRepository<ParkingSpaceManagementDO,Integer>, JpaSpecificationExecutor<ParkingSpaceManagementDO> {

    List<ParkingSpaceManagementDO> findByOrganizationIdAndCustomerUserId(String organizationId, String customerUserId);

    ParkingSpaceManagementDO findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(String parkingSpaceCode, String organizationId, int loggedOffState);


}
