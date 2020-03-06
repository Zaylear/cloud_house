package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RoomAndChargeItemsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomAndChargeItemsDAO extends JpaRepository<RoomAndChargeItemsDO,Integer>, JpaSpecificationExecutor<RoomAndChargeItemsDO> {

    @Transactional
    void deleteByIdIn(List<Integer> ids);

    @Transactional
    void deleteAllByRoomCodeAndOrganizationIdAndChargeCodeIn(String roomCode,String organizationId,List<String> chargeCodes);

    RoomAndChargeItemsDO findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(String roomCode,String unitCode,String buildingCode,String regionCode,String villageCode,String organizationId,String chargeCode);

    List<RoomAndChargeItemsDO> findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCodeIn(String roomCode,String unitCode,String buildingCode,String regionCode,String villageCode,String organizationId,List<String> chargeCodes);
}
