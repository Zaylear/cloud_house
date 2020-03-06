package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.rbi.interactive.dao.HistoryDataPropertyDueTimeDAO;
import com.rbi.interactive.dao.IFrontOfficeCashierDao;
import com.rbi.interactive.dao.RoomAndCustomerDAO;
import com.rbi.interactive.entity.HistoryDataPropertyDueTimeDO;
import com.rbi.interactive.entity.RoomAndCustomerDO;
import com.rbi.interactive.service.PropertyFeeDueTimeService;
import com.rbi.interactive.utils.Constants;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.RedisUtil;
import com.rbi.interactive.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class PropertyFeeDueTimeServiceImpl implements PropertyFeeDueTimeService {

    private final static Logger logger = LoggerFactory.getLogger(PropertyFeeDueTimeServiceImpl.class);

    @Autowired(required = false)
    IFrontOfficeCashierDao iFrontOfficeCashierDao;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    HistoryDataPropertyDueTimeDAO historyDataPropertyDueTimeDAO;

    @Override
    public String propertyFeeDueTime(String roomCode, String organizationId) {
        String dueTime = "";
        try {
            dueTime = redisUtil.get(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode).toString();
            if (null!=dueTime&&!"".equals(dueTime)&&!"NAN".equals(dueTime)){
                return dueTime;
            }
        } catch (Exception e) {
            dueTime = iFrontOfficeCashierDao.findMaxDueTime(roomCode,organizationId);
            if ("null".equals(dueTime)||null==dueTime||"".equals(dueTime)){
                HistoryDataPropertyDueTimeDO historyDataPropertyDueTimeDO = historyDataPropertyDueTimeDAO.findByOrganizationIdAndRoomCode(organizationId,roomCode);
                if (null!=historyDataPropertyDueTimeDO){
                    dueTime = historyDataPropertyDueTimeDO.getDueTime();
                }else {
                    List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findByOrganizationIdAndRoomCodeAndLoggedOffState(organizationId,roomCode,0);
//                查询房子开始计物业费时间
                    dueTime = roomAndCustomerDOS.get(0).getStartBillingTime();
                }
            }
            redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,dueTime);
            return dueTime;
        }
        dueTime = iFrontOfficeCashierDao.findMaxDueTime(roomCode,organizationId);
        if ("null".equals(dueTime)||null==dueTime||"".equals(dueTime)){
            HistoryDataPropertyDueTimeDO historyDataPropertyDueTimeDO = historyDataPropertyDueTimeDAO.findByOrganizationIdAndRoomCode(organizationId,roomCode);
            if (null!=historyDataPropertyDueTimeDO){
                dueTime = historyDataPropertyDueTimeDO.getDueTime();
            }else {
                List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findByOrganizationIdAndRoomCodeAndLoggedOffState(organizationId,roomCode,0);
//                查询房子开始计物业费时间
                dueTime = roomAndCustomerDOS.get(0).getStartBillingTime();
            }
        }
        redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,dueTime);
        return dueTime;
    }

    @Override
    public String findPropertyFeeDueTime(String roomCode, String organizationId) {
//        List<String> chargeCodeList = iFrontOfficeCashierDao.findChargeCodesByRoomCodeAndChargeStatus(roomCode,1);
//        String chargeCodes = Joiner.on(",").join(chargeCodeList);
//        String dueTime = iFrontOfficeCashierDao.findMaxDueTime(roomCode,chargeCodes);
//
//        if ("null".equals(dueTime)||null==dueTime||"".equals(dueTime)){
//            RoomAndCustomerDO roomAndCustomerDO = roomAndCustomerDAO.findByUserIdAndRoomCodeAndLoggedOffState(userId,roomCode,0);
////                查询房子开始计物业费时间
//            dueTime = roomAndCustomerDO.getStartBillingTime();
//        }
//
//        try {
//            dueTime = DateUtil.getTimeDay(dueTime,DateUtil.YEAR_MONTH_DAY,-1);
//        } catch (ParseException e) {
//            logger.error("【前台缴费服务类】物业费过期时间减一天计算错误！ERROR：{}",e);
//        }
//        return dueTime;
        String dueTime = "";
        dueTime = iFrontOfficeCashierDao.findMaxDueTime(roomCode,organizationId);
        if ("null".equals(dueTime)||null==dueTime||"".equals(dueTime)){
            HistoryDataPropertyDueTimeDO historyDataPropertyDueTimeDO = historyDataPropertyDueTimeDAO.findByOrganizationIdAndRoomCode(organizationId,roomCode);
            if (null!=historyDataPropertyDueTimeDO){
                dueTime = historyDataPropertyDueTimeDO.getDueTime();
            }else {
                List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findByOrganizationIdAndRoomCodeAndLoggedOffState(organizationId,roomCode,0);
//                查询房子开始计物业费时间
                dueTime = roomAndCustomerDOS.get(0).getStartBillingTime();
            }
        }
        redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,dueTime);
        return dueTime;
    }

    @Override
    public void findAllPropertyFeeDueTime() {
        JSONArray jsonArray = new JSONArray();
        List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findAllByLoggedOffState(0);
        for (RoomAndCustomerDO roomAndCustomerDO:roomAndCustomerDOS) {
            try {
                String dueTime = findPropertyFeeDueTime(roomAndCustomerDO.getRoomCode(),roomAndCustomerDO.getOrganizationId());
                redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+roomAndCustomerDO.getOrganizationId()+"-"+roomAndCustomerDO.getRoomCode(),dueTime);
            } catch (Exception e) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roomCode",roomAndCustomerDO.getRoomCode());
                jsonObject.put("userId",roomAndCustomerDO.getCustomerUserId());
                jsonObject.put("error",e.getMessage());
                jsonArray.add(jsonObject);
                logger.error("【物业费过期时间服务类】保存过期时间异常,roomCode:{},userId:{}，ERROR：{}",roomAndCustomerDO.getRoomCode(),roomAndCustomerDO.getCustomerUserId(),e);
                continue;
            }
        }
        logger.info("保存物业费过期时间成功！出现的错误有：{}",jsonArray);
    }
}
