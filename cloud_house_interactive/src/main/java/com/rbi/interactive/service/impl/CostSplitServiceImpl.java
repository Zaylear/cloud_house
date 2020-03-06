package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.DueTimeException;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.service.CostSplitService;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CostSplitServiceImpl implements CostSplitService {

    private final static Logger logger = LoggerFactory.getLogger(CostSplitServiceImpl.class);

    @Override
    public List<BillDetailedDO> costSplit(JSONObject jsonObject) throws ParseException, InvocationTargetException, IllegalAccessException {
        String firstStartTime = jsonObject.getString("firstStartTime");
        String firstEndTime = jsonObject.getString("firstEndTime");
        String secondStartTime = jsonObject.getString("secondStartTime");
        String secondEndTime = jsonObject.getString("secondEndTime");

        if(!DateUtil.isLastDayOfMonth(DateUtil.strToDateLong(secondEndTime,DateUtil.YEAR_MONTH_DAY))){
            throw new DueTimeException();
        }


//        String spiltTime = jsonObject.getString("spiltTime");
//        String spiltStartTime = spiltTime;
//        String spiltEndTime = DateUtil.getAfterDay(spiltTime,-1);
        BillDetailedDO billDetailedDO = JSONObject.parseObject(jsonObject.toString(),BillDetailedDO.class);//原账单明细
        Double monthlyPropertyFee = Tools.moneyHalfAdjust(jsonObject.getDouble("oneMonthPropertyFee"));//每月物业费
        Double amountReceivable = billDetailedDO.getAmountReceivable();//应收金额
        Double firstPropertyFee = 0d;
        Double secondPropertyFee = 0d;
        int firstMonth = 0;
        int secondMonth = 0;


        //判断开始时间是否是月的第一天
        if (DateUtil.isFirstDayOfMonth(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY))){
            //判断拆分结束时间是否是月最后一天
            if (DateUtil.isLastDayOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY))){
                //计算两日期之间的月数
                int firstMonthCount = DateUtil.getMonthDiff(firstStartTime,DateUtil.getAfterDay(firstEndTime,1));
                firstMonth = firstMonthCount;
                //计算第一段时间的物业费
                firstPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee*firstMonthCount);
                int secondMonthCount = DateUtil.getMonthDiff(secondStartTime,DateUtil.getAfterDay(secondEndTime,1));
                secondMonth = secondMonthCount;
                //计算第二段时间的物业费
                secondPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee*secondMonthCount);
            }else {
                //获取拆分时间的月的天数
                int monthCount = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)));
                //计算每天的物业费
                double everyDayPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee/monthCount);
                //获取该月份的第一天
                String firstDayOfMonth = DateUtil.getFirstDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)));
                //计算拆分月份的第一天至第一段时间的结束时间的天数
                int firstDayOfMonthAsFirstEndTime = DateUtil.longOfTwoDate(firstDayOfMonth,firstEndTime);
                //计算拆分月后半部分的天数
                int firstEndTimeAsFinallyMonthCount = monthCount-firstDayOfMonthAsFirstEndTime;
                //计算开始日期至拆分月数第一天的月数
                int monthCountFirstStartTimeAsFirstDayOfMonth = DateUtil.getMonthDiff(firstStartTime,firstDayOfMonth);
                firstMonth = monthCountFirstStartTimeAsFirstDayOfMonth;
                //计算拆分月下一月至结束时间的月数
                int monthCountSecondStartTimeAsSecondEndTime = DateUtil.getMonthDiff(DateUtil.getAfterMonth(firstStartTime,1),DateUtil.getAfterDay(secondEndTime,1));
                secondMonth = monthCountSecondStartTimeAsSecondEndTime;
                firstPropertyFee = Tools.moneyHalfAdjust(monthCountFirstStartTimeAsFirstDayOfMonth*monthlyPropertyFee+firstDayOfMonthAsFirstEndTime*everyDayPropertyFee);
                secondPropertyFee = Tools.moneyHalfAdjust(monthCountSecondStartTimeAsSecondEndTime*monthlyPropertyFee+firstEndTimeAsFinallyMonthCount*everyDayPropertyFee);
            }
        }else {
            //
            if (DateUtil.isLastDayOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY))){
                //获取第一段时间开始时间的月的天数
                int monthCountFirstStartTime = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)));
                //计算每天的物业费
                double everyDayPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee/monthCountFirstStartTime);
                //获取该月份的最后一天
                String firstDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)));
                //计算开始时间到月底最后一天相差的天数
                int dayCountFirstStartTimeAsFirstDayOfMonth = DateUtil.longOfTwoDate(firstStartTime,firstDayOfMonth)+1;
                //计算月初第一天至第二段时间开始日期相差的月数
                int monthCountFirstDayOfMonthAsFirstEndTime = DateUtil.getMonthDiff(DateUtil.getAfterDay(firstDayOfMonth,1),secondStartTime);
                firstMonth = monthCountFirstDayOfMonthAsFirstEndTime;
                //计算第二段时间开始日期至第二段结束时间相差的月数
                int monthCountSecondStartTimeAsSecondEndTime = DateUtil.getMonthDiff(secondStartTime,secondEndTime);
                secondMonth = monthCountSecondStartTimeAsSecondEndTime;
                //计算第一段时间的物业费
                firstPropertyFee = Tools.moneyHalfAdjust(dayCountFirstStartTimeAsFirstDayOfMonth*everyDayPropertyFee+monthCountFirstDayOfMonthAsFirstEndTime*monthlyPropertyFee);
                //计算第二段时间的物业费
                secondPropertyFee = Tools.moneyHalfAdjust(monthCountSecondStartTimeAsSecondEndTime*monthlyPropertyFee);

            }else {
                //获取第一段时间开始时间的月的天数
                int monthCountFirstStartTime = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)));
                //计算每天的物业费
                double everyDayPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee/monthCountFirstStartTime);
                //获取该月份的最后一天
                String firstDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY)));
                //判断拆分日期和第一段时间开始日期是否在同一月
                if (DateUtil.dateOfYear(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY))==DateUtil.dateOfYear(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY))&&DateUtil.dateOfMonth(DateUtil.strToDateLong(firstStartTime,DateUtil.YEAR_MONTH_DAY))==DateUtil.dateOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY))) {
                    //计算开始日期到第一段时间结束日期的天数
                    int dayCountFirstStartTimeAsFirstDayOfMonth = DateUtil.longOfTwoDate(firstStartTime,firstEndTime)+1;
                    //计算第一段时间的物业费
                    firstPropertyFee = Tools.moneyHalfAdjust(everyDayPropertyFee*dayCountFirstStartTimeAsFirstDayOfMonth);

                    //计算第二段开始日期至当月低相差的天数
                    int dayCountSecondFirstTimeAsFirstDayOfMonth = DateUtil.longOfTwoDate(secondStartTime,firstDayOfMonth)+1;
                    //计算当月低至第二段结束时间相差的月数
                    int monthCountFirstDayOfMonthAsSecondEndTime = DateUtil.getMonthDiff(DateUtil.getAfterDay(firstDayOfMonth,1),DateUtil.getAfterDay(secondEndTime,1));
                    secondMonth = monthCountFirstDayOfMonthAsSecondEndTime;
                    //计算第二段时间的物业费
                    secondPropertyFee = Tools.moneyHalfAdjust(dayCountSecondFirstTimeAsFirstDayOfMonth*everyDayPropertyFee+monthlyPropertyFee*monthCountFirstDayOfMonthAsSecondEndTime);
                }else {
                    //获取拆分月的天数
                    int dayCountSpiltMonth = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)));
                    //计算拆分月每天的物业费
                    double everyDaySpiltMonthPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee/dayCountSpiltMonth);

                    //获取开始时间到该月最后一天的天数
                    int dayCountFirstStartTimeAsFirstDayOfMonth = DateUtil.longOfTwoDate(firstStartTime,firstDayOfMonth)+1;
                    //获取拆分月的第一天和最后一天
                    String spiltTimeFirstDayOfMonth = DateUtil.getFirstDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)));
                    String spiltTimeLastDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(firstEndTime,DateUtil.YEAR_MONTH_DAY)));
                    //计算该月最后一天至拆分月第一天的月数
                    int monthCountFirstDayOfMonthAsSpiltTimeFirstDayOfMonth = DateUtil.getMonthDiff(DateUtil.getAfterDay(firstDayOfMonth,1),spiltTimeFirstDayOfMonth);
                    firstMonth = monthCountFirstDayOfMonthAsSpiltTimeFirstDayOfMonth;
                    //计算拆分月初第一天至第一段结束时间相差的天数
                    int dayCountSpiltTimeFirstDayOfMonthAsFirstEndTime = DateUtil.longOfTwoDate(spiltTimeFirstDayOfMonth,firstEndTime);


                    //计算第二段开始日期至拆分月最后一天相差的天数
                    int dayCountSecondStartTimeAsSpiltTimeLastDayOfMonth = DateUtil.longOfTwoDate(secondStartTime,spiltTimeLastDayOfMonth)+1;
                    //获取拆分月最后一天至第二段结束时间相差的月数
                    int monthCountSpiltTimeLastDayOfMonthAsSecondEndTime = DateUtil.getMonthDiff(DateUtil.getAfterDay(spiltTimeLastDayOfMonth,1),DateUtil.getAfterDay(secondEndTime,1));
                    secondMonth = monthCountSpiltTimeLastDayOfMonthAsSecondEndTime;

                    firstPropertyFee = Tools.moneyHalfAdjust(dayCountFirstStartTimeAsFirstDayOfMonth*everyDayPropertyFee+monthCountFirstDayOfMonthAsSpiltTimeFirstDayOfMonth*monthlyPropertyFee+dayCountSpiltTimeFirstDayOfMonthAsFirstEndTime*everyDaySpiltMonthPropertyFee);
                    secondPropertyFee = Tools.moneyHalfAdjust(dayCountSecondStartTimeAsSpiltTimeLastDayOfMonth*everyDaySpiltMonthPropertyFee+monthCountSpiltTimeLastDayOfMonthAsSecondEndTime*monthlyPropertyFee);
                }
            }
        }
        Integer code = jsonObject.getInteger("code");
        Integer code1 = code+50;
        Integer code2 = code+51;

        List<BillDetailedDO> billDetailedDOS = new ArrayList<>();
        BillDetailedDO billDetailedDO1 = new BillDetailedDO();
        BeanUtils.copyProperties(billDetailedDO,billDetailedDO1);

        billDetailedDO1.setAmountReceivable(firstPropertyFee);
        billDetailedDO1.setActualMoneyCollection(firstPropertyFee);
        billDetailedDO1.setStartTime(firstStartTime);
        billDetailedDO1.setDueTime(firstEndTime);
        billDetailedDO1.setCode(code1);
        billDetailedDO1.setStateOfArrears(1);
        billDetailedDO1.setDatedif(firstMonth);
        billDetailedDO1.setParentCode(billDetailedDO.getCode());
        billDetailedDO1.setSplitState(2);
        billDetailedDOS.add(billDetailedDO1);
        BillDetailedDO billDetailedDO2 = new BillDetailedDO();
        BeanUtils.copyProperties(billDetailedDO,billDetailedDO2);
        billDetailedDO2.setStartTime(secondStartTime);
        billDetailedDO2.setDueTime(secondEndTime);
        billDetailedDO2.setAmountReceivable(secondPropertyFee);
        billDetailedDO2.setActualMoneyCollection(secondPropertyFee);
        billDetailedDO2.setSplitState(2);
        billDetailedDO2.setDatedif(secondMonth);
        billDetailedDO2.setCode(code2);
        billDetailedDO2.setParentCode(billDetailedDO.getCode());
        billDetailedDOS.add(billDetailedDO2);
        billDetailedDO.setSplitState(1);
        billDetailedDOS.add(billDetailedDO);
        return billDetailedDOS;


//        //获取开始时间月份的最后一天
//        String lastDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(billDetailedDO.getStartTime(),DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(billDetailedDO.getStartTime(),DateUtil.YEAR_MONTH_DAY)));
//        //获取该月份的天数
//        int firstMonthOfDays = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(billDetailedDO.getStartTime(),DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(billDetailedDO.getStartTime(),DateUtil.YEAR_MONTH_DAY)));
//        //算出每天需要交的费用
//        Double daylyPropertyFee = Tools.moneyHalfAdjust(monthlyPropertyFee/firstMonthOfDays);
//        //首月需要交费的天数
//        int firstMonthPaymentDays = DateUtil.longOfTwoDate(billDetailedDO.getStartTime(),lastDayOfMonth)+1;
//
//        if (DateUtil.dateOfYear(DateUtil.strToDateLong(billDetailedDO.getStartTime(),DateUtil.YEAR_MONTH_DAY))!=DateUtil.dateOfYear(DateUtil.strToDateLong(spiltTime,DateUtil.YEAR_MONTH_DAY))&&DateUtil.dateOfMonth(DateUtil.strToDateLong(billDetailedDO.getStartTime(),DateUtil.YEAR_MONTH_DAY))!=DateUtil.dateOfMonth(DateUtil.strToDateLong(spiltTime,DateUtil.YEAR_MONTH_DAY))){
//            //计算首月需要交的物业费
//            Double firstMonthPropertyFee = Tools.moneyHalfAdjust(daylyPropertyFee*firstMonthPaymentDays);
//            int monthCount = 0;
//            int daysCount = 0;
//            String movementTime = DateUtil.getAfterDay(lastDayOfMonth,1);
////        //计算物业费欠费时长
//            for (int i = 0;;i++){
//                if (1==DateUtil.compareDate(movementTime,spiltStartTime)){
//                    break;
//                }
//                movementTime = DateUtil.getAfterMonth(movementTime,1);
//                monthCount++;
//            }
//            if (-1==DateUtil.compareDate(DateUtil.getAfterMonth(movementTime,-1),spiltStartTime)){
//                daysCount = DateUtil.longOfTwoDate(DateUtil.getAfterMonth(movementTime,-1),spiltStartTime);
//            }
//            int monthlyDay = DateUtil.daysMonth(Integer.parseInt(Tools.analysisStr(spiltStartTime,"-",1)),Integer.parseInt(Tools.analysisStr(spiltStartTime,"-",2)));//当月天数
//            Double dailyCost = Tools.moneyHalfAdjust(monthlyPropertyFee/monthlyDay);//每天费用
//            Integer code = jsonObject.getInteger("code");
//            Integer code1 = code+50;
//            Integer code2 = code+51;
//            Double actualMoneyCollection = jsonObject.getDouble("actualMoneyCollection");
//            List<BillDetailedDO> billDetailedDOS = new ArrayList<>();
//            BillDetailedDO billDetailedDO1 = new BillDetailedDO();
//            BeanUtils.copyProperties(billDetailedDO,billDetailedDO1);
//
//            Double actualMoneyCollection1 = Tools.moneyHalfAdjust(firstMonthPropertyFee+monthlyPropertyFee*(monthCount-1)+daysCount*dailyCost);
//            billDetailedDO1.setAmountReceivable(actualMoneyCollection1);
//            billDetailedDO1.setActualMoneyCollection(actualMoneyCollection1);
//            billDetailedDO1.setDueTime(spiltEndTime);
//            billDetailedDO1.setCode(code1);
//            billDetailedDO1.setParentCode(billDetailedDO.getCode());
//            billDetailedDO1.setSplitState(2);
//            billDetailedDOS.add(billDetailedDO1);
//            BillDetailedDO billDetailedDO2 = new BillDetailedDO();
//            BeanUtils.copyProperties(billDetailedDO,billDetailedDO2);
//            billDetailedDO2.setStartTime(spiltTime);
//            billDetailedDO2.setAmountReceivable(amountReceivable-billDetailedDO1.getAmountReceivable());
//            billDetailedDO2.setActualMoneyCollection(Tools.moneyHalfAdjust(actualMoneyCollection-actualMoneyCollection1));
//            billDetailedDO2.setSplitState(2);
//            billDetailedDO2.setCode(code2);
//            billDetailedDO2.setParentCode(billDetailedDO.getCode());
//            billDetailedDOS.add(billDetailedDO2);
//            billDetailedDO.setSplitState(1);
//            billDetailedDOS.add(billDetailedDO);
//            return billDetailedDOS;
//        }else {
//            int dayCount = DateUtil.longOfTwoDate(billDetailedDO.getStartTime(),spiltTime);
//            Double actualMoneyCollection1 = Tools.moneyHalfAdjust(daylyPropertyFee*dayCount);
//            Integer code = jsonObject.getInteger("code");
//            Integer code1 = code+50;
//            Integer code2 = code+51;
//            Double actualMoneyCollection = jsonObject.getDouble("actualMoneyCollection");
//            List<BillDetailedDO> billDetailedDOS = new ArrayList<>();
//            BillDetailedDO billDetailedDO1 = new BillDetailedDO();
//            BeanUtils.copyProperties(billDetailedDO,billDetailedDO1);
//
//            billDetailedDO1.setAmountReceivable(actualMoneyCollection1);
//            billDetailedDO1.setActualMoneyCollection(actualMoneyCollection1);
//            billDetailedDO1.setDueTime(spiltEndTime);
//            billDetailedDO1.setCode(code1);
//            billDetailedDO1.setParentCode(billDetailedDO.getCode());
//            billDetailedDO1.setSplitState(2);
//            billDetailedDOS.add(billDetailedDO1);
//            BillDetailedDO billDetailedDO2 = new BillDetailedDO();
//            BeanUtils.copyProperties(billDetailedDO,billDetailedDO2);
//            billDetailedDO2.setStartTime(spiltTime);
//            billDetailedDO2.setAmountReceivable(amountReceivable-actualMoneyCollection1);
//            billDetailedDO2.setActualMoneyCollection(Tools.moneyHalfAdjust(actualMoneyCollection-actualMoneyCollection1));
//            billDetailedDO2.setSplitState(2);
//            billDetailedDO2.setCode(code2);
//            billDetailedDO2.setParentCode(billDetailedDO.getCode());
//            billDetailedDOS.add(billDetailedDO2);
//            billDetailedDO.setSplitState(1);
//            billDetailedDOS.add(billDetailedDO);
//            return billDetailedDOS;
//        }
//        return null;
    }

}
