package com.rbi.interactive.dao;

import com.rbi.interactive.entity.OriginalBillDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OriginalBillDAO extends JpaRepository<OriginalBillDO,Integer>, JpaSpecificationExecutor<OriginalBillDO> {
}
