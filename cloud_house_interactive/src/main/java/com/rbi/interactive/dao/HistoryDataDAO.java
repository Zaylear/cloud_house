package com.rbi.interactive.dao;

import com.rbi.interactive.entity.HistoryDataDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HistoryDataDAO extends JpaRepository<HistoryDataDO,Integer>, JpaSpecificationExecutor<HistoryDataDO> {

    HistoryDataDO findByOrganizationIdAndRoomCodeAndStatus(String organizationId,String roomCode,String status);

}
