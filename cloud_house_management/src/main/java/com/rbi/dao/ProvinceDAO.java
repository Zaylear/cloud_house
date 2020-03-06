package com.rbi.dao;

import com.rbi.entity.ProvinceDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProvinceDAO extends JpaRepository<ProvinceDO,Integer>, JpaSpecificationExecutor<ProvinceDO> {
}
