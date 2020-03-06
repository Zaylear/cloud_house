package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.RoomAndCustomerDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IRoomAndCustomerDAO extends JpaRepository<RoomAndCustomerDO,Integer>, JpaSpecificationExecutor<RoomAndCustomerDO> {

    List<RoomAndCustomerDO> findAllByLoggedOffState(int loggedOffState);

    List<RoomAndCustomerDO> findByOrganizationIdAndRoomCodeAndLoggedOffState(String organizationId, String roomCode, int loggedOffState);

    RoomAndCustomerDO findByCustomerUserIdAndRoomCodeAndLoggedOffState(String customerUserId, String roomCode, int loggedOffState);

    List<RoomAndCustomerDO> findAllByRoomCodeAndLoggedOffStateAndIdentityAndOrganizationId(String roomCode, Integer loggedOffState, Integer identity, String organizationId);

    @Transactional
    void deleteAllByRoomCode(String roomCode);

    List<RoomAndCustomerDO> findByRoomCodeAndAndLoggedOffState(String roomCode, int loggedOffState);

    RoomAndCustomerDO findByCustomerUserIdAndLoggedOffState(String customerUserId, int loggedOffState);

}
