package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.utils.Tools;

/**
 * 装修管理费
 * 项目类型为：2
 * 单位：元/平方米
 */
public class DecorationManagementCostImpl implements ICostStrategy {

    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) {
        Double roomSize = jsonObject.getDouble("roomSize");
        Double cost = Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*roomSize);
        billDetailedDO.setAmountReceivable(cost);
        billDetailedDO.setActualMoneyCollection(cost);
//        chargeItemCostDTO.setPreferentialAmount(0d);
        billDetailedDO.setDatedif(0);
        return billDetailedDO;
    }
}
