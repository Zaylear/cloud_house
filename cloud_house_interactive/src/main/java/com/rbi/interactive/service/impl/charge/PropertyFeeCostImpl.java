package com.rbi.interactive.service.impl.charge;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.FrontOfficeCashierService;
import com.rbi.interactive.service.PropertyFeeDueTimeService;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.ParseException;

public class PropertyFeeCostImpl implements ICostStrategy {
    /**
     *物业费   项目类型为：1
     * @param
     */
    @Override
    public BillDetailedDO iCostCalculation(JSONObject jsonObject, BillDetailedDO billDetailedDO) throws ParseException {
        Double roomSize = jsonObject.getDouble("roomSize");
        Integer datedif = jsonObject.getInteger("datedif");
        String chargeCode = jsonObject.getString("chargeCode");
        String chargeName = jsonObject.getString("chargeName");
        String startTime = jsonObject.getString("startTime");

        billDetailedDO.setChargeCode(chargeCode);
        billDetailedDO.setChargeName(chargeName);
        billDetailedDO.setChargeType(billDetailedDO.getChargeType());
        billDetailedDO.setDatedif(datedif);
        billDetailedDO.setStartTime(startTime);

//        DateUtil.isFirstDayOfMonth(startTime);
        if (DateUtil.isFirstDayOfMonth(DateUtil.strToDateLong(startTime,DateUtil.YEAR_MONTH_DAY))){

            //根据月数计算结束时间
            String dueTime = DateUtil.getAfterMonth(startTime,datedif);
            billDetailedDO.setDueTime(dueTime);

            //计算应收，实收，优惠金额
            double amountReceivable = Tools.moneyHalfAdjust(Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*roomSize)*datedif);
            double actualMoneyCollection=Tools.moneyHalfAdjust(Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*roomSize)*datedif*billDetailedDO.getDiscount()/10.0);
//        double preferentialAmount = Tools.moneyHalfAdjust(amountReceivable-actualMoneyCollection);
            billDetailedDO.setAmountReceivable(amountReceivable);
            billDetailedDO.setActualMoneyCollection(actualMoneyCollection);
//        billDetailedDO.setPreferentialAmount(preferentialAmount);
            return billDetailedDO;
        }else {
            //计算过期时间
            String dueTime = DateUtil.getAfterMonth(startTime,datedif);
            dueTime = DateUtil.getLastDayOfMonth(Integer.parseInt(Tools.analysisStr(dueTime,"-",1)),Integer.parseInt(Tools.analysisStr(dueTime,"-",2)));
            dueTime = DateUtil.getAfterDay(dueTime,1);
            billDetailedDO.setDueTime(dueTime);

            //计算一个月的应收，实收金额
            double amountReceivable = Tools.moneyHalfAdjust(Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*roomSize));
//            double actualMoneyCollection=Tools.moneyHalfAdjust(Tools.moneyHalfAdjust(billDetailedDO.getChargeStandard()*roomSize));

            //计算开始日期至月底的天数
            String firstDayOfMonth = DateUtil.getFirstDayOfMonth(Integer.parseInt(Tools.analysisStr(DateUtil.getAfterMonth(startTime,1),"-",1)),Integer.parseInt(Tools.analysisStr(DateUtil.getAfterMonth(startTime,1),"-",2)));
            int days = DateUtil.longOfTwoDate(startTime,firstDayOfMonth)-1;
            if (days == 0){
                days=1;
            }
            //计算开始时间月份的天数
            int monthDays = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(startTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(startTime,DateUtil.YEAR_MONTH_DAY)));
            //计算一天的费用
            double oneDayFee = Tools.moneyHalfAdjust(amountReceivable/monthDays);

            //计算费用，没有打折，如果需要打折还需单独作处理
            amountReceivable = Tools.moneyHalfAdjust(amountReceivable*datedif+oneDayFee*days);

            billDetailedDO.setAmountReceivable(amountReceivable);
            billDetailedDO.setActualMoneyCollection(amountReceivable);

            return billDetailedDO;
        }


    }
}
