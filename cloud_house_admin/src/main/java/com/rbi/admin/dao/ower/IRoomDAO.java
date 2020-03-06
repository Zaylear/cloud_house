package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.RoomDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IRoomDAO extends JpaRepository<RoomDO,Integer>, JpaSpecificationExecutor<RoomDO> {

    RoomDO findByRoomCode(String roomCode);

    RoomDO findByRoomCodeAndUnitCode(String roomCode, String unitCode);
}
