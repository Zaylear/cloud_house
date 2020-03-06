package com.rbi.dao;

import com.rbi.entity.LogLoginDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogLoginDAO extends JpaRepository<LogLoginDO,Integer>, JpaSpecificationExecutor<LogLoginDO> {
}
