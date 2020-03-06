package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ExclusiveParkingSpaceHistoryDataDueTimeDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExclusiveParkingSpaceHistoryDataDueTimeDAO extends JpaRepository<ExclusiveParkingSpaceHistoryDataDueTimeDO,Integer>, JpaSpecificationExecutor<ExclusiveParkingSpaceHistoryDataDueTimeDO> {

    ExclusiveParkingSpaceHistoryDataDueTimeDO findByOrganizationIdAndParkingSpaceCode(String organizationId,String parkingSpaceCode);

}
