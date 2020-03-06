package com.rbi.interactive.dao;

import com.rbi.interactive.entity.VillageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VillageDAO extends JpaRepository<VillageDO,Integer>, JpaSpecificationExecutor<VillageDO> {

    List<VillageDO> findAllByOrganizationIdAndEnable(String organizationId,Integer enable);

    List<VillageDO> findAllByOrganizationIdAndEnableAndVillageCode(String organizationId,Integer enable,String villageCode);

    VillageDO findByOrganizationIdAndVillageCodeAndEnable(String organizationId,String villageCode,Integer enable);

    List<VillageDO> findAllByOrganizationId(String organizationId);

}
