package com.rbi.interactive.service;

import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;

public interface ParkingSpaceDueTimeService {

    String findExclusiveParkingSpaceDueTime(String parkingSpaceCode, String organizationId);

    ParkingSpaceCostDetailDO findRentalParkingSpaceDueTime(String licensePlateNumber, String roomCode, String organizationId);

    //查询当天非续租的数量


}
