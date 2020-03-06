package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDO;
import com.rbi.interactive.utils.PageData;

import java.text.ParseException;
import java.util.Map;

public interface BillsService {

    PageData<BillDO> findBillPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname);

    Map<String,Object> findBillDetail(String userId,String orderId) throws ParseException;

    void deleteBill(JSONObject jsonObject,String userId);

    /**
     * 下面四个接口用于更新单据信息
     */
    void updateBill(JSONObject jsonObject);
    void updateBillDetail(JSONObject jsonObject);
    void updateParkingSpaceBillDetail(JSONObject jsonObject);
}
