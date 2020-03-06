package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.BuildingDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BuildingDAO extends JpaRepository<BuildingDO,Integer>, JpaSpecificationExecutor<BuildingDO> {

    BuildingDO findByBuildingCodeAndRegionCode(String buildingCode, String regionCode);

}
