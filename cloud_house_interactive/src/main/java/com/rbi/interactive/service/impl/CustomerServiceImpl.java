package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.CustomerException;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    RegionDAO regionDAO;

    @Autowired
    BuildingDAO buildingDAO;

    @Autowired
    UnitDAO unitDAO;

    @Autowired
    RoomDAO roomDAO;

    @Override
    public Map<String,Object> findByMobilePhone(String phone,String roomCode) {

        Map<String,Object> map = new HashMap<>();

        List<CustomerInfoDO> customerInfoDO = customerDAO.findByMobilePhone(phone);
        if (customerInfoDO.size()==0){
            map.put("status","1001");
            return map;
        }

        RoomAndCustomerDO roomAndCustomerDO = roomAndCustomerDAO.findByCustomerUserIdAndRoomCodeAndLoggedOffState(customerInfoDO.get(0).getUserId(),roomCode,0);
        if (null == roomAndCustomerDO||"null".equals(roomAndCustomerDO)){
            map.put("status","1003");
            return map;
        }
        customerInfoDO.get(0).setSalt(null);
        customerInfoDO.get(0).setPassword(null);
        map.put("status","1000");
        map.put("customerInfoDO",customerInfoDO.get(0));
        return map;
    }

    @Override
    public Map<String,Object> findByRoomCode(JSONObject jsonObject, String userId) {
        String villageCode = jsonObject.getString("villageCode");
        String regionCode = jsonObject.getString("regionCode");
        String buildingCode = jsonObject.getString("buildingCode");
        String unitCode = jsonObject.getString("unitCode");
        String roomCode = jsonObject.getString("roomCode");
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findAllByRoomCodeAndLoggedOffStateAndIdentityAndOrganizationId(roomCode,0,1,userInfoDO.getOrganizationId());
        CustomerInfoDO customerInfoDO = customerDAO.findByUserId(roomAndCustomerDOS.get(0).getCustomerUserId());
        if (roomAndCustomerDOS.size()==0){
            throw new CustomerException();
        }

        VillageDO villageDO = villageDAO.findByOrganizationIdAndVillageCodeAndEnable(userInfoDO.getOrganizationId(),villageCode,1);
        RegionDO regionDO = regionDAO.findAllByRegionCodeAndVillageCode(regionCode,villageCode);
        BuildingDO buildingDO = buildingDAO.findByBuildingCodeAndRegionCode(buildingCode,regionCode);
        UnitDO unitDO = unitDAO.findByUnitCodeAndBuildingCode(unitCode,buildingCode);

        JSONObject houseInfo = new JSONObject();
        houseInfo.put("villageCode",villageDO.getVillageCode());
        houseInfo.put("villageName",villageDO.getVillageName());

        houseInfo.put("regionCode",regionDO.getRegionCode());
        houseInfo.put("regionName",regionDO.getRegionName());

        houseInfo.put("buildingCode",buildingDO.getBuildingCode());
        houseInfo.put("buildingName",buildingDO.getBuildingName());

        houseInfo.put("unitCode",unitDO.getUnitCode());
        houseInfo.put("unitName",unitDO.getUnitName());
        houseInfo.put("roomCode",roomCode);

        Map<String,Object> map = new HashMap<>();
        map.put("houseInfo",houseInfo);
        map.put("customerInfo",customerInfoDO);

        return map;
    }

    @Override
    public RoomAndCustomerDO findByRoomCodeAndOrganizationIdAndIdentity(String roomCode, String organizationId) {

        List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findAllByRoomCodeAndLoggedOffStateAndIdentityAndOrganizationId(roomCode,0,1,organizationId);

        return roomAndCustomerDOS.get(0);
    }
}
