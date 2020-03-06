package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.service.LiquidatedDamagesService;
import com.rbi.interactive.service.impl.charge.PropertyFeeCostImpl;
import com.rbi.interactive.service.impl.strategy.ICostStrategyImpl;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.List;

@Service
public class LiquidatedDamagesServiceImpl implements LiquidatedDamagesService {

    private final static Logger logger = LoggerFactory.getLogger(LiquidatedDamagesServiceImpl.class);

    @Autowired(required = false)
    IFrontOfficeCashierDao iFrontOfficeCashierDao;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Autowired(required = false)
    ILiquidatedDamagesDAO iLiquidatedDamagesDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    CustomerDAO customerDAO;

    /**
     * 判断是否存在违约金
     * @param roomCode 房间号
     * @param organizationId 机构编号
     * @param dueTime 物业费开始时间
     * @return
     * @throws ParseException
     */
    public JSONObject liquidatedDamagesExistence(String roomCode,String organizationId, String dueTime,String quarterlyCycleTime) throws ParseException {
        String dueTimeFront = quarterlyCycleTime;
        String startTime = quarterlyCycleTime;
        int count = 0;
        int compareDateCount;
        //统计季度，时间接近物业费到期时间
        while (count != -1){
            startTime = DateUtil.getAfterMonth(startTime,3);
            compareDateCount = DateUtil.compareDate(startTime,dueTime);
            if (compareDateCount==0){
                dueTimeFront = startTime;
                count++;
                break;
            }else if (compareDateCount==1){
                break;
            }
            dueTimeFront = startTime;
            count++;
        }
        String dueTimeAfter = DateUtil.getAfterMonth(dueTimeFront,3);
        compareDateCount = DateUtil.compareDate(dueTimeAfter,DateUtil.date(DateUtil.YEAR_MONTH_DAY));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dueTimeFront",dueTimeFront);
        jsonObject.put("dueTimeAfter",dueTimeAfter);
        if (compareDateCount == -1){
            logger.info("【违约金判断类】需要缴纳违约金，房间号:",roomCode);
            jsonObject.put("status",1);
        }else {
            jsonObject.put("status",0);
        }
        return jsonObject;
    }


    /**
     * 计算违约金
     * @param roomCode 房间号
     * @param organizationId
     * @param dueTime 物业费开始时间
     * @param actualMoneyCollection 单月物业费
     * @param liquidatedDamageDueTime 违约金到期时间
     * @return
     */
    public JSONArray liquidatedDamagesImpl(String roomCode, String organizationId, String dueTime, Double actualMoneyCollection,String liquidatedDamageDueTime,Double superfluousAmount,String quarterlyCycleTime) throws ParseException {
        //申请 用于封装违约金数据详情
        JSONArray liquidatedDamagesList = new JSONArray();
        JSONObject jsonTime = liquidatedDamagesExistence(roomCode,organizationId,dueTime,quarterlyCycleTime);
        /**
         * 存在违约金，计算违约金
         */
        if (1==jsonTime.getInteger("status")){
            //统计物业费到期时间与季度末时间相差的月数
            int monthCount = 0;
            monthCount = DateUtil.getMonthDiff(dueTime,jsonTime.getString("dueTimeAfter"));
            //统计欠费天数
            int days = 0;
            int count=0;
            String startTime = jsonTime.getString("dueTimeFront");
            int compareDateCount;
            String endTime = startTime;
            while (count!=-100){
                startTime = DateUtil.getAfterMonth(startTime,3);
                compareDateCount = DateUtil.compareDate(startTime,liquidatedDamageDueTime);
                JSONObject liquidatedDamages = new JSONObject();
                if (compareDateCount==0){
                    days = DateUtil.longOfTwoDate(startTime,liquidatedDamageDueTime)+1;
                    liquidatedDamages.put("dueTimeFront",endTime);
                    liquidatedDamages.put("dueTimeAfter",startTime);
                    liquidatedDamages.put("amountMoney",Tools.moneyHalfAdjust(actualMoneyCollection*3*days*0.003));
                    liquidatedDamages.put("days",days);
                    liquidatedDamagesList.add(liquidatedDamages);
                    endTime = startTime;
                    count++;
                    break;
                }else if (compareDateCount==1){
                    break;
                }
                if (count==0){
                    days = DateUtil.longOfTwoDate(startTime,liquidatedDamageDueTime)+1;
                    liquidatedDamages.put("amountMoney",Tools.moneyHalfAdjust((actualMoneyCollection*monthCount-superfluousAmount)*days*0.003));
                    liquidatedDamages.put("dueTimeFront",jsonTime.getString("dueTimeFront"));
                    liquidatedDamages.put("dueTimeAfter",jsonTime.getString("dueTimeAfter"));
                    liquidatedDamages.put("days",days);
                    liquidatedDamagesList.add(liquidatedDamages);
                }else {
                    days = DateUtil.longOfTwoDate(startTime,liquidatedDamageDueTime)+1;
                    liquidatedDamages.put("dueTimeFront",endTime);
                    liquidatedDamages.put("dueTimeAfter",startTime);
                    liquidatedDamages.put("amountMoney",Tools.moneyHalfAdjust(actualMoneyCollection*3*days*0.003));
                    liquidatedDamages.put("days",days);
                    liquidatedDamagesList.add(liquidatedDamages);
                }
                endTime = startTime;
                count++;
            }
            logger.info("计算滞纳金金额成功：{}",roomCode);
        }
        return liquidatedDamagesList;
    }

    /**
     *
     * @param roomCode 房间号
     * @param actualMoneyCollection 物业费缴费金额
     * @param userId 用户ID
     * @param mobilePhone 客户手机号
     * @param liquidatedDamageDueTime 违约金到期时间
     * @return
     * @throws ParseException
     * @throws ConnectException
     */
    @Override
    public LiquidatedDamagesDO liquidatedDamagesCalculation(String orderId,String roomCode, Double actualMoneyCollection, String userId, String mobilePhone,String liquidatedDamageDueTime) throws ParseException, ConnectException {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        //手机号查询客户信息
        List<CustomerInfoDO> customerInfoDOS = customerDAO.findByMobilePhone(mobilePhone);
        //房间号查询房间信息
        List<HouseAndProprietorDTO> houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByRoomCode(roomCode,organizationId);

        if (0==houseAndProprietorDTOS.size()){
            throw new NullPointerException();
        }

        //查询周期循环时间
        String quarterlyCycleTime = houseAndProprietorDTOS.get(0).getQuarterlyCycleTime();
        if (null == quarterlyCycleTime|| "".equals(quarterlyCycleTime)){
            List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findByRoomCodeAndAndLoggedOffState(roomCode,0);
            quarterlyCycleTime = roomAndCustomerDOS.get(0).getStartBillingTime();
        }

        int chargeType;//收费项目类型
        if (1 == houseAndProprietorDTOS.get(0).getRoomType()){
            //住宅
            chargeType = 1;
        }else if (2 == houseAndProprietorDTOS.get(0).getRoomType()){
            //办公
            chargeType = 3;
        } else {
            //商业
            chargeType = 2;
        }
        //查询房间关联收费项目
        ChargeItemDO chargeItemDO = chargeItemDAO.findByOrganizationIdAndChargeTypeAndEnable(organizationId,chargeType,1);
        //查询物业费到期时间，作为物业费开始时间
        String startTime = iLiquidatedDamagesDAO.findLiquidatedDamagesDueTime(roomCode,organizationId);
        if (null == startTime||"".equals(startTime)){
        //查询房子开始计物业费时间
            startTime = houseAndProprietorDTOS.get(0).getStartBillingTime();
        }
        JSONObject json = new JSONObject();
        json.put("roomSize",houseAndProprietorDTOS.get(0).getRoomSize());
        json.put("datedif",1);
        json.put("chargeCode",chargeItemDO.getChargeCode());
        json.put("chargeName",chargeItemDO.getChargeName());
        json.put("chargeType",chargeType);
        json.put("startTime",DateUtil.getFirstDayOfMonth(Integer.parseInt(Tools.analysisStr(startTime,"-",1)),Integer.parseInt(Tools.analysisStr(startTime,"-",2))));
        //计算单月物业费
        BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeItemDO.getChargeCode(),1);
        ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
        ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
        //计算物业费缴费月数
        int month = (int) Math.floor(actualMoneyCollection/billDetailedBackDO.getAmountReceivable());
        //计算除整月物业费以外多余的金额
        Double superfluousAmount = Tools.moneyHalfAdjust(actualMoneyCollection-billDetailedBackDO.getAmountReceivable()*month);
        //计算物业费过期时间即违约金计算开始时间
        String dueTime = DateUtil.getAfterMonth(startTime,month);
        //计算违约金
        JSONArray jsonArray = liquidatedDamagesImpl(roomCode,organizationId,dueTime,billDetailedBackDO.getAmountReceivable(),liquidatedDamageDueTime,superfluousAmount,quarterlyCycleTime);
        //封装违约金数据详情
        LiquidatedDamagesDO liquidatedDamagesDO = new LiquidatedDamagesDO();
        liquidatedDamagesDO.setOrderId(orderId);
        liquidatedDamagesDO.setMonth(month);
        liquidatedDamagesDO.setOrganizationId(organizationId);
        liquidatedDamagesDO.setOrganizationName(organizationDO.getOrganizationName());
        liquidatedDamagesDO.setVillageCode(houseAndProprietorDTOS.get(0).getVillageCode());
        liquidatedDamagesDO.setVillageName(houseAndProprietorDTOS.get(0).getVillageName());
        liquidatedDamagesDO.setRegionCode(houseAndProprietorDTOS.get(0).getRegionCode());
        liquidatedDamagesDO.setRegionName(houseAndProprietorDTOS.get(0).getRegionName());
        liquidatedDamagesDO.setBuildingCode(houseAndProprietorDTOS.get(0).getBuildingCode());
        liquidatedDamagesDO.setBuildingName(houseAndProprietorDTOS.get(0).getBuildingName());
        liquidatedDamagesDO.setUnitCode(houseAndProprietorDTOS.get(0).getUnitCode());
        liquidatedDamagesDO.setUnitName(houseAndProprietorDTOS.get(0).getUnitName());
        liquidatedDamagesDO.setRoomSize(houseAndProprietorDTOS.get(0).getRoomSize());
        Double amountTotalReceivable = 0d;
        for (int i = 0;i < jsonArray.size();i++){
            amountTotalReceivable+=JSONObject.parseObject(jsonArray.get(i).toString()).getDouble("amountMoney");
        }
        liquidatedDamagesDO.setAmountTotalReceivable(Tools.moneyHalfAdjust(amountTotalReceivable));
        liquidatedDamagesDO.setActualTotalMoneyCollection(Tools.moneyHalfAdjust(amountTotalReceivable));
        liquidatedDamagesDO.setSurplusTotal(0d);
        liquidatedDamagesDO.setPropertyActualMoneyCollection(actualMoneyCollection);
        liquidatedDamagesDO.setMobilePhone(mobilePhone);
        if (0<customerInfoDOS.size()){
            liquidatedDamagesDO.setCustomerUserId(customerInfoDOS.get(0).getUserId());
            liquidatedDamagesDO.setSurname(customerInfoDOS.get(0).getSurname());
            liquidatedDamagesDO.setIdNumber(customerInfoDOS.get(0).getIdNumber());
        }
        liquidatedDamagesDO.setAuditStatus(4);
        liquidatedDamagesDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        liquidatedDamagesDO.setTollCollectorId(userId);
        liquidatedDamagesDO.setTollCollectorName(userInfoDO.getRealName());
        if (0<jsonArray.size()){
            liquidatedDamagesDO.setLiquidatedDamages(jsonArray.toJSONString());
            liquidatedDamagesDO.setQuarterlyCycleTime(JSONObject.parseObject(jsonArray.get(0).toString()).getString("dueTimeFront"));
        }else {
            liquidatedDamagesDO.setQuarterlyCycleTime(quarterlyCycleTime);
        }

        /**
         * 封装计算违约金条件的参数
         */
        //循环周期  ——> quarterlyCycleTime

        //房间编号
        liquidatedDamagesDO.setRoomCode(houseAndProprietorDTOS.get(0).getRoomCode());
        //物业费整月以外多余金额
        liquidatedDamagesDO.setSuperfluousAmount(superfluousAmount);
        //物业费开始时间
        liquidatedDamagesDO.setStartTime(startTime);
        //物业费到期时间即违约金开始时间
        liquidatedDamagesDO.setDueTime(dueTime);
        //单月物业费
        liquidatedDamagesDO.setOneMonthPropertyFeeAmount(billDetailedBackDO.getAmountReceivable());
        //违约金到期时间
        liquidatedDamagesDO.setLiquidatedDamageDueTime(liquidatedDamageDueTime);
        return liquidatedDamagesDO;
    }


}
