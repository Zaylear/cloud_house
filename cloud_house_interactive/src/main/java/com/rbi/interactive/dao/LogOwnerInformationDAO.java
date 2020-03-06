package com.rbi.interactive.dao;

import com.rbi.interactive.entity.LogOwnerInformationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogOwnerInformationDAO extends JpaRepository<LogOwnerInformationDO,Integer>, JpaSpecificationExecutor<LogOwnerInformationDO> {
}
