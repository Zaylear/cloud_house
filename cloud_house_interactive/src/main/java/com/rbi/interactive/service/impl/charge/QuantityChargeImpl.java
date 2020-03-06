package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.utils.Tools;

/**
 * 根据数量缴费：临时出入证工本费，门禁办卡费
 * 项目类型：8,9
 */

public class QuantityChargeImpl implements ICostStrategy {
    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) {
        billDetailedDO.setDatedif(jsonObject.getInteger("datedif"));
        Double cost = Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*jsonObject.getInteger("datedif"));
        billDetailedDO.setAmountReceivable(cost);
        billDetailedDO.setActualMoneyCollection(cost);
//        billDetailedDO.setPreferentialAmount(0d);
        return billDetailedDO;
    }
}
