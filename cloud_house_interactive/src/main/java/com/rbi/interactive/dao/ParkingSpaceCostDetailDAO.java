package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ParkingSpaceCostDetailDAO extends JpaRepository<ParkingSpaceCostDetailDO,Integer>, JpaSpecificationExecutor<ParkingSpaceCostDetailDO> {

    List<ParkingSpaceCostDetailDO> findAllByOrderIdAndOrganizationId(String orderId,String organizationId);

}
