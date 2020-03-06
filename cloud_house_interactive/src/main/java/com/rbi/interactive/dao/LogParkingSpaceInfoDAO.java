package com.rbi.interactive.dao;


import com.rbi.interactive.entity.LogParkingSpaceInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogParkingSpaceInfoDAO extends JpaRepository<LogParkingSpaceInfoDO,Integer>, JpaSpecificationExecutor<LogParkingSpaceInfoDO> {
}
