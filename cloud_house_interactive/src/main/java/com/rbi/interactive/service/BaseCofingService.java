package com.rbi.interactive.service;

import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.RoomAndChargeItemsDO;
import com.rbi.interactive.utils.PageData;

import java.util.List;

public interface BaseCofingService {

    List<ChargeItemDO> findChargeItemByOrganizationId(String userId);

    void bindChargeItemAndRoom(RoomAndChargeItemsDO roomAndChargeItemsDO, String userId);

    PageData findRoomAndChargeItemByPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode);

    void updateRoomAndChargeItem(RoomAndChargeItemsDO roomAndChargeItemsDO, String userId);

    void deleteRoomAndChargeItem(List<Integer> ids);

}
