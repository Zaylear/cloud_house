package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.RoomAndChargeItemsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomAndChargeItemsDAO extends JpaRepository<RoomAndChargeItemsDO,Integer>, JpaSpecificationExecutor<RoomAndChargeItemsDO> {

    @Transactional
    void deleteByIdIn(List<Integer> ids);

    RoomAndChargeItemsDO findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(String roomCode, String unitCode, String buildingCode, String regionCode, String villageCode, String organizationId, String chargeCode);
}
