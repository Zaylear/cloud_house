package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ExclusiveParkingSpaceHistoryDataDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExclusiveParkingSpaceHistoryDataDAO extends JpaRepository<ExclusiveParkingSpaceHistoryDataDO,Integer>, JpaSpecificationExecutor<ExclusiveParkingSpaceHistoryDataDO> {

    ExclusiveParkingSpaceHistoryDataDO findByOrganizationIdAndParkingSpaceCode(String organizationId,String parkingSpaceCode);

}
