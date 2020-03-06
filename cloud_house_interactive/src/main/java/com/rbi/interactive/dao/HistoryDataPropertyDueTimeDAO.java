package com.rbi.interactive.dao;

import com.rbi.interactive.entity.HistoryDataPropertyDueTimeDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HistoryDataPropertyDueTimeDAO extends JpaRepository<HistoryDataPropertyDueTimeDO,Integer>, JpaSpecificationExecutor<HistoryDataPropertyDueTimeDO> {

    HistoryDataPropertyDueTimeDO findByOrganizationIdAndRoomCode(String organizationId,String roomCode);

}
