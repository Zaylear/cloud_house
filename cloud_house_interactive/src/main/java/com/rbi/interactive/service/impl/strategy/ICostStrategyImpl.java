package com.rbi.interactive.service.impl.strategy;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;

import java.net.ConnectException;
import java.text.ParseException;

public class ICostStrategyImpl {

    private ICostStrategy iCostStrategy;

    public ICostStrategyImpl(ICostStrategy iCostStrategy){
        this.iCostStrategy = iCostStrategy;
    }

    public BillDetailedDO costCalculationMethod(JSONObject jsonObject, BillDetailedDO billDetailedDO) throws ConnectException, ParseException {
        return iCostStrategy.iCostCalculation(jsonObject,billDetailedDO);
    }
}
