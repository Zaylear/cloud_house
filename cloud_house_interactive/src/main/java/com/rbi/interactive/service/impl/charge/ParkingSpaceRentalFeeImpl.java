package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.Tools;

import java.text.ParseException;

/**
 * 车位租赁费
 * 项目类型：5
 * 单位：元/月
 */
public class ParkingSpaceRentalFeeImpl implements ICostStrategy {

    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) throws ParseException {

        int datedif = jsonObject.getInteger("datedif");
        String chargeCode = jsonObject.getString("chargeCode");
        String chargeName = jsonObject.getString("chargeName");
        String chargeType = jsonObject.getString("chargeType");
        String dueTime = jsonObject.getString("dueTime");
        int rentalRenewalStatus = jsonObject.getInteger("rentalRenewalStatus");
        String startTime;
        if ("".equals(dueTime)||"null".equals(dueTime)||null==dueTime){
            startTime = DateUtil.date(DateUtil.YEAR_MONTH_DAY);
            dueTime = DateUtil.getAfterMonth(startTime,datedif);
        }else {
            if (0==rentalRenewalStatus){
                startTime = DateUtil.date(DateUtil.YEAR_MONTH_DAY);
                dueTime = DateUtil.getAfterMonth(startTime,datedif);
            }else {
                startTime = dueTime;
                dueTime = DateUtil.getAfterMonth(startTime,datedif);
            }
        }
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
