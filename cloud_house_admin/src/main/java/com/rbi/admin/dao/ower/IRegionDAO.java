package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.RegionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface IRegionDAO extends JpaRepository<RegionDO,Integer>, JpaSpecificationExecutor<RegionDO> {

    RegionDO findAllByRegionCodeAndVillageCode(String regionCode, String villageCode);

    RegionDO findByVillageCodeAndRegionName(String villageCode, String regionName);
}
