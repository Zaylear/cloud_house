package com.rbi.interactive.service.impl.strategy;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;

import java.net.ConnectException;
import java.text.ParseException;

public interface ICostStrategy {

    BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) throws ConnectException, ParseException;

}
