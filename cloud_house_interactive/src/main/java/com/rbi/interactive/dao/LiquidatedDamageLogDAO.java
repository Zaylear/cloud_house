package com.rbi.interactive.dao;

import com.rbi.interactive.entity.LiquidatedDamageLogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LiquidatedDamageLogDAO extends JpaRepository<LiquidatedDamageLogDO,Integer>, JpaSpecificationExecutor<LiquidatedDamageLogDO> {
}
