package com.rbi.interactive.service.impl;

import com.rbi.interactive.dao.ExclusiveParkingSpaceHistoryDataDueTimeDAO;
import com.rbi.interactive.dao.IParkingSpaceCostDAO;
import com.rbi.interactive.entity.ExclusiveParkingSpaceHistoryDataDueTimeDO;
import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import com.rbi.interactive.service.ParkingSpaceDueTimeService;
import com.rbi.interactive.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParkingSpaceDueTimeServiceImpl implements ParkingSpaceDueTimeService {

    @Autowired(required = false)
    IParkingSpaceCostDAO iParkingSpaceCostDAO;

    @Autowired
    ExclusiveParkingSpaceHistoryDataDueTimeDAO exclusiveParkingSpaceHistoryDataDueTimeDAO;


    @Override
    public String findExclusiveParkingSpaceDueTime(String parkingSpaceCode, String organizationId) {
        String dueTime = iParkingSpaceCostDAO.findExclusiveParkingSpaceDueTime(parkingSpaceCode,organizationId);
        if (null==dueTime||"".equals(dueTime)|| StringUtils.isBlank(dueTime)){

            ExclusiveParkingSpaceHistoryDataDueTimeDO exclusiveParkingSpaceHistoryDataDueTimeDO = exclusiveParkingSpaceHistoryDataDueTimeDAO.findByOrganizationIdAndParkingSpaceCode(organizationId,parkingSpaceCode);
            if (null == exclusiveParkingSpaceHistoryDataDueTimeDO){
                dueTime = iParkingSpaceCostDAO.findExclusiveParkingSpaceStartTime(parkingSpaceCode,organizationId);
            }else {
                dueTime = exclusiveParkingSpaceHistoryDataDueTimeDO.getDueTime();
            }

        }
        return dueTime;
    }

    @Override
    public ParkingSpaceCostDetailDO findRentalParkingSpaceDueTime(String licensePlateNumber,String roomCode,String organizationId) {
        ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = iParkingSpaceCostDAO.findRentalParkingSpaceDueTime(licensePlateNumber,roomCode,organizationId);
        return parkingSpaceCostDetailDO;
    }


}
