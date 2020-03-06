package com.rbi.interactive.dao;

import com.rbi.interactive.entity.UnitDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UnitDAO extends JpaRepository<UnitDO,Integer>, JpaSpecificationExecutor<UnitDO> {

    UnitDO findByUnitCodeAndBuildingCode(String unitCode,String buildingCode);

}
