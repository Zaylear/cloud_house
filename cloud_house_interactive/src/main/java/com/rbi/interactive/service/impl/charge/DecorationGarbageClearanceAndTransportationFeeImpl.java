package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;

/**
 * 装修垃圾清运费
 * 项目类型为：3
 */
public class DecorationGarbageClearanceAndTransportationFeeImpl implements ICostStrategy {
    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) {
        billDetailedDO.setAmountReceivable(billDetailedDO.getChargeStandard());
        billDetailedDO.setActualMoneyCollection(billDetailedDO.getChargeStandard());
//        billDetailedDO.setPreferentialAmount(0d);
        billDetailedDO.setDatedif(0);
        return billDetailedDO;
    }
}
