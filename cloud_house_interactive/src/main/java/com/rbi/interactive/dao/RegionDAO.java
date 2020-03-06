package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RegionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RegionDAO extends JpaRepository<RegionDO,Integer>, JpaSpecificationExecutor<RegionDO> {

    RegionDO findAllByRegionCodeAndVillageCode(String regionCode,String villageCode);

    RegionDO findByVillageCodeAndRegionName(String villageCode,String regionName);

    RegionDO findByRegionNameAndVillageCode(String regionName,String villageCode);
}
