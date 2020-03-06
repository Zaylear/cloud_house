package com.rbi.interactive.dao;

import com.rbi.interactive.entity.BuildingDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BuildingDAO extends JpaRepository<BuildingDO,Integer>, JpaSpecificationExecutor<BuildingDO> {

    BuildingDO findByBuildingCodeAndRegionCode(String buildingCode,String regionCode);

    BuildingDO findByBuildingNameAndRegionCode(String buildingName,String regionCode);

}
