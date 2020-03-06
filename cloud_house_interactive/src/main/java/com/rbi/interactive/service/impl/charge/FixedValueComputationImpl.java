package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.utils.Tools;

/**
 *定值缴费：保证金
 * 单位：元/户
 * 项目类型：4
 */

public class FixedValueComputationImpl implements ICostStrategy {
    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) {
        Double cost = Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard());
        billDetailedDO.setAmountReceivable(cost);
        billDetailedDO.setActualMoneyCollection(cost);
//        chargeItemCostDTO.setPreferentialAmount(0d);
        billDetailedDO.setDatedif(0);
        return billDetailedDO;
    }
}
