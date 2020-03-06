package com.rbi.interactive.dao;

import com.rbi.interactive.entity.EventRecordDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventRecordDAO extends JpaRepository<EventRecordDO,Integer>, JpaSpecificationExecutor<EventRecordDO> {
}
