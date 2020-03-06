package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.UnitDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UnitDAO extends JpaRepository<UnitDO,Integer>, JpaSpecificationExecutor<UnitDO> {

    UnitDO findByUnitCodeAndBuildingCode(String unitCode, String buildingCode);

}
