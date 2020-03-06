package com.rbi.interactive.service;

import com.rbi.interactive.entity.ParkingSpaceManagementDO;

import java.util.List;

public interface QueryExclusiveParkingSpaceService {

    List<ParkingSpaceManagementDO> queryExclusiveParkingSpaceService(String roomCode, String organizationId);
}
