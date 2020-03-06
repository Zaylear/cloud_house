package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.RoomAndCustomerDO;

import java.util.Map;

public interface CustomerService {

    Map<String,Object> findByMobilePhone(String phone,String roomCode);

    Map<String,Object> findByRoomCode(JSONObject jsonObject, String userId);

    RoomAndCustomerDO findByRoomCodeAndOrganizationIdAndIdentity(String roomCode, String organizationId);
}
