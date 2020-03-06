package com.rbi.interactive.dao;

import com.rbi.interactive.entity.LogParkingSpaceManagementInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogParkingSpaceManagementDAO extends JpaRepository<LogParkingSpaceManagementInfoDO,Integer>, JpaSpecificationExecutor<LogParkingSpaceManagementInfoDO> {
}
