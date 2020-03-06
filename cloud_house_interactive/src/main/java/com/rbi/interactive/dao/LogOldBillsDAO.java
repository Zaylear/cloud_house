package com.rbi.interactive.dao;

import com.rbi.interactive.entity.LogOldBillsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogOldBillsDAO extends JpaRepository<LogOldBillsDO,Integer>, JpaSpecificationExecutor<LogOldBillsDO> {
}
