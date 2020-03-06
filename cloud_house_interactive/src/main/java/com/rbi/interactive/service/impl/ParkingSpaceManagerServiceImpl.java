package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.service.ParkingSpaceManagerService;
import com.rbi.interactive.service.impl.charge.ParkingSpaceManagerFeeImpl;
import com.rbi.interactive.service.impl.strategy.ICostStrategyImpl;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作废
 */

@Service
public class ParkingSpaceManagerServiceImpl implements ParkingSpaceManagerService {

    private final static Logger logger = LoggerFactory.getLogger(ParkingSpaceManagerServiceImpl.class);

    @Autowired
    VehicleInformationDAO vehicleInformationDAO;

    @Autowired
    ParkingSpaceInfoDAO parkingSpaceInfoDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Autowired(required = false)
    IParkingSpaceManagerDAO iParkingSpaceManagerDAO;

    @Autowired
    ParkingSpaceManagerDAO parkingSpaceManagerDAO;

    @Autowired(required = false)
    IBillDAO iBillDAO;

    @Override
    public List<VehicleInformationDO> findLicensePlateNumberByRoomCode(String roomCode) {
        List<VehicleInformationDO> vehicleInformationDOS = vehicleInformationDAO.findByRoomCode(roomCode);
        return vehicleInformationDOS;
    }

    @Override
    public List<ChargeItemDO> findChargeItem(String userId) {

//        String parkingSpaceCode = jsonObject.getString("parkingSpaceCode");
//        String
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<Integer> chargeType = new ArrayList<>();
        chargeType.add(5);
        chargeType.add(6);
        chargeType.add(10);
        chargeType.add(11);
        List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findByOrganizationIdAndEnableAndChargeTypeIn(organizationId,1,chargeType);
        return chargeItemDOS;
    }

    @Override
    public String findParkingSpaceDueTime(String roomCode, String parkingSpaceCode, String mobilePhone,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String maxDueTime = iParkingSpaceManagerDAO.findParkingSpacceMaxDueTime(userInfoDO.getOrganizationId(),
                roomCode,parkingSpaceCode,mobilePhone);
        return maxDueTime;
    }

    @Override
    public BillDetailedDO calculationCost(JSONObject jsonObject) {

        //计算车位到期时间
        JSONObject json = new JSONObject();

        json.put("chargeCode",jsonObject.getString("chargeCode"));
        json.put("chargeName",jsonObject.getString("chargeName"));
        json.put("datedif",jsonObject.getInteger("datedif"));
        json.put("chargeType",jsonObject.getString("chargeType"));
        json.put("dueTime",jsonObject.getString("dueTime"));
        json.put("rentalRenewalStatus",jsonObject.getString("rentalRenewalStatus"));  //续租状态

        ICostStrategy iCostStrategy = new ParkingSpaceManagerFeeImpl();
        ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
        BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCodeAndParkingSpaceNatureAndParkingSpaceType(
                jsonObject.getString("chargeCode"),jsonObject.getString("parkingSpaceNature"),
                jsonObject.getString("parkingSpaceType"));
        try {
            BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
            System.out.println(billDetailedBackDO);
            billDetailedBackDO.setDueTime(DateUtil.getTimeDay(billDetailedBackDO.getDueTime(),DateUtil.YEAR_MONTH_DAY,-1));
            return billDetailedBackDO;
//            jsonArray.add(chargeItemBackCostDTO);
        } catch (ConnectException e) {
            logger.error("【定值计算类】服务器未响应！e:{}",e);
            return null;
        } catch (Exception e) {
            logger.error("【定值计算类】计算错误，e：{}",e);
        }
        return null;
    }


    @Override
    public List<ParkingSpaceInfoDO> findParkingSpaceCodeByRegionCode(String regionCode) {
        List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = parkingSpaceInfoDAO.findByRegionCodeAndVehicleCapacityGreaterThan(regionCode,0);
        return parkingSpaceInfoDOS;
    }

    @Override
    public void add(JSONObject jsonObject, String userId) {

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        ParkingSpaceBillDO parkingSpaceBillDO = JSONObject.parseObject(jsonObject.toJSONString(),ParkingSpaceBillDO.class);
        String orderId;
        String time = DateUtil.timeStamp()+"00";
        Integer orderIdsCount = iBillDAO.findOrderIdsByorganizationId(organizationId,time);
        if (orderIdsCount<9){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"000"+orderIdsCount;
        }else if (orderIdsCount>=9&&orderIdsCount<99){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"00"+orderIdsCount;
        }else if (orderIdsCount>=99&&orderIdsCount<999){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"0"+orderIdsCount;
        }else {
            orderIdsCount = orderIdsCount+1;
            orderId = time+orderIdsCount;
        }
        parkingSpaceBillDO.setOrderId(orderId);
        parkingSpaceBillDO.setTollCollectorId(userId);
        parkingSpaceBillDO.setInvalidState(0);
        parkingSpaceBillDO.setOrganizationId(organizationId);
        parkingSpaceBillDO.setOrganizationName(organizationDO.getOrganizationName());
//        parkingSpaceBillDO.setSurplusMoney(0d);
        parkingSpaceBillDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        parkingSpaceBillDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        parkingSpaceManagerDAO.save(parkingSpaceBillDO);
    }

    @Override
    public PageData findByPage(int pageNum, int pageSize, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);

        int pageNo = (pageNum-1)*pageSize;
        List<ParkingSpaceBillDO> parkingSpaceBillDOS = iParkingSpaceManagerDAO.findByPage(userInfoDO.getOrganizationId(),pageNo,pageSize);
        int totalCount = iParkingSpaceManagerDAO.findByPageCount(userInfoDO.getOrganizationId());
        int totalPage;
        if (totalCount%pageSize==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = totalCount/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,totalCount,parkingSpaceBillDOS);
    }
}
