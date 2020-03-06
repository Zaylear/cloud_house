package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.ParkingSpaceInfoDO;
import com.rbi.interactive.entity.VehicleInformationDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.utils.PageData;

import java.util.List;

public interface ParkingSpaceManagerService {

    List<VehicleInformationDO> findLicensePlateNumberByRoomCode(String roomCode);


    List<ChargeItemDO> findChargeItem(String userId);

    String findParkingSpaceDueTime(String roomCode,String parkingSpaceCode,String mobilePhone,String userId);

    BillDetailedDO calculationCost(JSONObject jsonObject);

    List<ParkingSpaceInfoDO> findParkingSpaceCodeByRegionCode(String regionCode);


    void add(JSONObject jsonObject, String userId);

    PageData findByPage(int pageNo, int pageSize, String userId);
}
