package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RoomAndCustomerDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomAndCustomerDAO extends JpaRepository<RoomAndCustomerDO,Integer>, JpaSpecificationExecutor<RoomAndCustomerDO> {

    List<RoomAndCustomerDO> findAllByLoggedOffState(int loggedOffState);

    List<RoomAndCustomerDO> findByOrganizationIdAndRoomCodeAndLoggedOffState(String organizationId, String roomCode, int loggedOffState);

    List<RoomAndCustomerDO> findAllByRoomCodeAndLoggedOffStateAndIdentityAndOrganizationId(String roomCode,int loggedOffState,int identity,String organizationId);

    RoomAndCustomerDO findByCustomerUserIdAndRoomCodeAndLoggedOffState(String customerUserId, String roomCode, int loggedOffState);

    List<RoomAndCustomerDO> findAllByRoomCodeAndLoggedOffStateAndIdentityAndOrganizationId(String roomCode,Integer loggedOffState,Integer identity,String organizationId);

    @Transactional
    void deleteAllByRoomCode(String  roomCode);

    List<RoomAndCustomerDO> findByRoomCodeAndAndLoggedOffState(String roomCode, int loggedOffState);

    RoomAndCustomerDO findByCustomerUserIdAndLoggedOffState(String customerUserId,int loggedOffState);

    List<RoomAndCustomerDO> findAllByRoomCodeAndLoggedOffStateAndOrganizationId(String roomCode, int loggedOffState, String organizationId);

}
