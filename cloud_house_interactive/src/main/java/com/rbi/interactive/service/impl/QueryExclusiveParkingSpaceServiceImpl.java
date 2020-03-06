package com.rbi.interactive.service.impl;

import com.rbi.interactive.dao.ParkingSpaceManagementDAO;
import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import com.rbi.interactive.service.QueryExclusiveParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryExclusiveParkingSpaceServiceImpl implements QueryExclusiveParkingSpaceService {

    @Autowired
    ParkingSpaceManagementDAO parkingSpaceManagementDAO;

    @Override
    public List<ParkingSpaceManagementDO> queryExclusiveParkingSpaceService(String roomCode, String organizationId) {
        List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = parkingSpaceManagementDAO.findByOrganizationIdAndRoomCode(organizationId,roomCode);
        return parkingSpaceManagementDOS;
    }
}
