package com.rbi.interactive.service;

import java.util.Map;

public interface RoomInfoTreeService {

    Map<String,Object> findRoomInfoByMobilePhone(String userId, String mobilePhone) throws Exception;

}
