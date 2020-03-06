package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ParkingSpaceStatisticsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParkingSpaceStatisticsDAO extends JpaRepository<ParkingSpaceStatisticsDO,Integer>, JpaSpecificationExecutor<ParkingSpaceStatisticsDO> {
}
