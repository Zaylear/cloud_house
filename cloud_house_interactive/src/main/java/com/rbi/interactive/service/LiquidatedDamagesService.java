package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.LiquidatedDamagesDO;
import java.net.ConnectException;
import java.text.ParseException;

public interface LiquidatedDamagesService {

    /**
     * 判断是否存在违约金
     * @return
     */
    JSONObject liquidatedDamagesExistence(String roomCode,String organizationId, String dueTime,String quarterlyCycleTime) throws ParseException;

    JSONArray liquidatedDamagesImpl(String roomCode, String organizationId, String dueTime, Double actualMoneyCollection,String liquidatedDamageDueTime,Double superfluousAmount,String quarterlyCycleTime) throws ParseException;

    LiquidatedDamagesDO liquidatedDamagesCalculation(String orderId,String roomCode, Double actualMoneyCollection, String userId, String mobilePhone,String liquidatedDamageDueTime) throws ParseException, ConnectException;

}
