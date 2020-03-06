package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RoomDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoomDAO extends JpaRepository<RoomDO,Integer>, JpaSpecificationExecutor<RoomDO> {

    RoomDO findByRoomCode(String roomCode);

    RoomDO findByRoomCodeAndUnitCode(String roomCode,String unitCode);
}
