package com.rbi.interactive.dao;

import com.rbi.interactive.entity.VehicleInformationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehicleInformationDAO extends JpaRepository<VehicleInformationDO,Integer>, JpaSpecificationExecutor<VehicleInformationDO> {

    List<VehicleInformationDO> findByRoomCode(String roomCode);

    @Transactional
    void deleteByIdIn(List<Integer> ids);
}
