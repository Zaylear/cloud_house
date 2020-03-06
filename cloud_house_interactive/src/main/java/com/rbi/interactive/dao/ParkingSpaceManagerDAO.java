package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceBillDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParkingSpaceManagerDAO extends JpaRepository<ParkingSpaceBillDO,Integer>, JpaSpecificationExecutor<ParkingSpaceBillDO> {


}
