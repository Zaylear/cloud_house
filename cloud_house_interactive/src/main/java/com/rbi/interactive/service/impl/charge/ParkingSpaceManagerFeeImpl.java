package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.Tools;

import java.text.ParseException;

/**
 * 车位管理费
 * 项目类型：6
 * 单位：元/月
 */
public class ParkingSpaceManagerFeeImpl implements ICostStrategy {

    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) throws ParseException {

        int datedif = jsonObject.getInteger("datedif");
        String chargeCode = jsonObject.getString("chargeCode");
        String chargeName = jsonObject.getString("chargeName");
        String chargeType = jsonObject.getString("chargeType");
        String startTime = jsonObject.getString("startTime");
//        int rentalRenewalStatus = jsonObject.getInteger("rentalRenewalStatus");
        String dueTime = DateUtil.getAfterMonth(startTime,datedif);
        billDetailedDO.setChargeCode(chargeCode);
        billDetailedDO.setChargeName(chargeName);
        billDetailedDO.setDatedif(datedif);
        billDetailedDO.setChargeType(chargeType);
        billDetailedDO.setStartTime(startTime);
        billDetailedDO.setDueTime(dueTime);
        double cost = Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*billDetailedDO.getDiscount()*datedif/10d);
        billDetailedDO.setAmountReceivable(cost);
        billDetailedDO.setActualMoneyCollection(cost);
//        billDetailedDO.setPreferentialAmount(0d);
        return billDetailedDO;
    }
}
