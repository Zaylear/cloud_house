package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.rbi.interactive.abnormal.*;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.*;
import com.rbi.interactive.service.*;
import com.rbi.interactive.service.impl.deduction.DeductionEnum;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.service.impl.charge.*;
import com.rbi.interactive.service.impl.strategy.ICostStrategyImpl;
import com.rbi.interactive.utils.*;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.*;

import com.google.common.base.Joiner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.itextpdf.text.PageSize.A4;

@Service
public class FrontOfficeCashierServiceImpl implements FrontOfficeCashierService {

    private final static Logger logger = LoggerFactory.getLogger(FrontOfficeCashierServiceImpl.class);

    @Autowired(required = false)
    IFrontOfficeCashierDao iFrontOfficeCashierDao;

    @Autowired
    ThisSystemOrderIdService thisSystemOrderIdService;

    @Autowired
    BillDAO billDAO;

    @Autowired
    BillDetailedDAO billDetailedDAO;

    @Autowired
    RefundApplicationDAO refundApplicationDAO;

    @Autowired
    RoomAndCouponDAO roomAndCouponDAO;

    @Autowired
    CostDeductionDAO costDeductionDAO;

    @Autowired
    OriginalBillDAO originalBillDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Autowired(required = false)
    IBillDAO iBillDAO;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired(required = false)
    IRoomAndCustomerDAO iRoomAndCustomerDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    PropertyFeeDueTimeService propertyFeeDueTimeService;

    @Autowired
    LiquidatedDamagesService liquidatedDamagesService;

    @Autowired
    RefundDAO refundDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    LogOldBillsDAO logOldBillsDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    EventRecordDAO eventRecordDAO;

    @Autowired
    ParkingSpaceManagementDAO parkingSpaceManagementDAO;

    @Autowired
    ParkingSpaceDueTimeService parkingSpaceDueTimeService;

    @Autowired
    ParkingSpaceInfoDAO parkingSpaceInfoDAO;

    @Autowired
    ParkingSpaceCostDetailDAO parkingSpaceCostDetailDAO;

    @Autowired
    QueryExclusiveParkingSpaceService queryExclusiveParkingSpaceService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired
    HistoryDataDAO historyDataDAO;

    @Autowired
    HistoryDataPropertyDueTimeDAO historyDataPropertyDueTimeDAO;

    @Autowired
    PinkingSpaceContractNumberService pinkingSpaceContractNumberService;

    @Override
    public List<HouseAndProprietorDTO> findHouseByRoomCode(JSONObject jsonObject, String token) {

        String organizationId = jsonObject.getString("organizationId");
        String organizationName = jsonObject.getString("organizationName");
        String roomCode = jsonObject.getString("roomCode");

        List<HouseAndProprietorDTO> houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByRoomCode(roomCode,organizationId);
        List<String> roomCodeS = new ArrayList<>();
        List<String> userIdS = new ArrayList<>();

        houseAndProprietorDTOS.forEach((HouseAndProprietorDTO houseAndProprietorDTO) ->{
            /**
             * 统计物业费到期时间
             */
            String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(houseAndProprietorDTO.getRoomCode(),houseAndProprietorDTO.getOrganizationId());
            if (0<=DateUtil.compareDate(dueTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY))){
                houseAndProprietorDTO.setMinMonth(0);
            }else {
                try {
                    houseAndProprietorDTO.setMinMonth(DateUtil.getMonthDiff(dueTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY)));
                } catch (ParseException e) {
                    logger.error("【收费房间查询类】计算缴费月数异常，ERROR：{}",e);
                }
            }
            try {
                dueTime = DateUtil.getTimeDay(dueTime,DateUtil.YEAR_MONTH_DAY,-1);
            } catch (ParseException e) {
                logger.error("【前台缴费服务类】物业费过期时间减一天计算错误！ERROR：{}",e);
            }
            houseAndProprietorDTO.setDueTime(dueTime);
            roomCodeS.add(houseAndProprietorDTO.getRoomCode());
            userIdS.add(houseAndProprietorDTO.getCustomerUserId());
        });
        return houseAndProprietorDTOS;
    }

    @Autowired
    CouponDAO couponDAO;

    @Override
    public PageData<T> findHousePageS(int pageNum, int pageSize, String token, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) throws Exception {

        String userId = JwtToken.getClaim(token,"userId");

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);

        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        List<String> villageList = new ArrayList<>();
        for (VillageDO villageDO:villageDOS) {
            villageList.add(villageDO.getVillageCode());
        }

        String villageCodes = Joiner.on("','").join(villageList);

        int pageNo = pageSize*(pageNum-1);

        int totalCount = 0;
        List<HouseAndProprietorDTO> houseAndProprietorDTOS = new ArrayList<>();
        if (StringUtils.isBlank(villageCode)&&StringUtils.isBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isBlank(mobilePhone)&&StringUtils.isBlank(idNumber)&&StringUtils.isBlank(surname)){
            totalCount = iFrontOfficeCashierDao.findCount(organizationId,villageCodes);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouse(organizationId,villageCodes,pageNo,pageSize);
        }else if(StringUtils.isNotBlank(idNumber)){
            totalCount = iFrontOfficeCashierDao.findHouseByIdNumberCount(organizationId,idNumber,villageCodes);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByIdNumber(organizationId,idNumber,villageCodes,pageNo,pageSize);
        }else if(StringUtils.isNotBlank(surname)){
            totalCount = iFrontOfficeCashierDao.findHouseBySurnameCount(organizationId,surname,villageCodes);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseBySurname(organizationId,surname,villageCodes,pageNo,pageSize);
        }else if (StringUtils.isBlank(villageCode)&&StringUtils.isBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isNotBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByMobilePhoneCount(organizationId,mobilePhone,villageCodes);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByMobilePhone(organizationId,mobilePhone,villageCodes,pageNo,pageSize);
        }else if (StringUtils.isBlank(villageCode)&&StringUtils.isBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isNotBlank(roomCode)&&StringUtils.isBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByRoomCodeCount(organizationId,roomCode,villageCodes);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByRoomCodePage(organizationId,roomCode,villageCodes,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeCount(organizationId,villageCode);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCode(organizationId,villageCode,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isNotBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndMobilePhoneCount(organizationId,villageCode,mobilePhone);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndMobilePhone(organizationId,villageCode,mobilePhone,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeCount(organizationId,villageCode,regionCode);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCode(organizationId,villageCode,regionCode,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isNotBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndMobilePhoneCount(organizationId,villageCode,regionCode,mobilePhone);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndMobilePhone(organizationId,villageCode,regionCode,mobilePhone,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isNotBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeCount(organizationId,villageCode,regionCode,buildingCode);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCode(organizationId,villageCode,regionCode,buildingCode,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isNotBlank(buildingCode)&&StringUtils.isBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isNotBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndMobilePhoneCount(organizationId,villageCode,regionCode,buildingCode,mobilePhone);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndMobilePhone(organizationId,villageCode,regionCode,buildingCode,mobilePhone,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isNotBlank(buildingCode)&&StringUtils.isNotBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeCount(organizationId,villageCode,regionCode,buildingCode,unitCode);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCode(organizationId,villageCode,regionCode,buildingCode,unitCode,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isNotBlank(buildingCode)&&StringUtils.isNotBlank(unitCode)&&StringUtils.isBlank(roomCode)&&StringUtils.isNotBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndMobilePhoneCount(organizationId,villageCode,regionCode,buildingCode,unitCode,mobilePhone);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndMobilePhone(organizationId,villageCode,regionCode,buildingCode,unitCode,mobilePhone,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isNotBlank(buildingCode)&&StringUtils.isNotBlank(unitCode)&&StringUtils.isNotBlank(roomCode)&&StringUtils.isBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomCodeCount(organizationId,villageCode,regionCode,buildingCode,unitCode,roomCode);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomCode(organizationId,villageCode,regionCode,buildingCode,unitCode,roomCode,pageNo,pageSize);
        }else if (StringUtils.isNotBlank(villageCode)&&StringUtils.isNotBlank(regionCode)&&StringUtils.isNotBlank(buildingCode)&&StringUtils.isNotBlank(unitCode)&&StringUtils.isNotBlank(roomCode)&&StringUtils.isNotBlank(mobilePhone)){
            totalCount = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomAndMobilePhoneCount(organizationId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone);
            houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByVillageCodeAndRegionCodeAndBuildingCodeAndUnitCodeAndRoomAndMobilePhone(organizationId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,pageNo,pageSize);
        }
        int totalPage;
        if (totalCount%pageSize==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = totalCount/pageSize+1;
        }

        List<String> roomCodeS = new ArrayList<>();
        List<String> userIdS = new ArrayList<>();


        for (HouseAndProprietorDTO houseAndProprietorDTO:houseAndProprietorDTOS) {

            /**
             * 统计物业费到期时间
             */
            String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(houseAndProprietorDTO.getRoomCode(),houseAndProprietorDTO.getOrganizationId());



            //计算单月物业费金额
            ChargeItemDO chargeItemDO = null;
            try {
                chargeItemDO = iChargeDAO.findChargeItemProperty(houseAndProprietorDTO.getRoomCode(),organizationId);
            } catch (MyBatisSystemException e) {
                logger.error("【查询收费项目异常】ERROR：{}",e);
                throw new RoomConfigException(roomCode);
            }

            BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeItemDO.getChargeCode(),1);
            JSONObject json = new JSONObject();
            json.put("roomSize",houseAndProprietorDTO.getRoomSize());
            json.put("datedif",1);
            json.put("chargeCode",chargeItemDO.getChargeCode());
            json.put("chargeName",chargeItemDO.getChargeName());
            json.put("chargeType",chargeItemDO.getChargeType());
            json.put("roomCode",roomCode);
            json.put("customerUserId",houseAndProprietorDTO.getCustomerUserId());
            json.put("startTime",DateUtil.getFirstDayOfMonth(Integer.parseInt(Tools.analysisStr(dueTime,"-",1)),Integer.parseInt(Tools.analysisStr(dueTime,"-",2))));
            ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
            ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
            BillDetailedDO billDetailedBackDO = null;
            try {
                billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json, billDetailedDO);
            }catch (Exception e){
                logger.error("【物业缴费服务类】查询预缴金额异常，ERROR：{}",e);
            }

            if (0<=DateUtil.compareDate(dueTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY))){
                String nextMonthFirstDate = DateUtil.nextMonthFirstDate();
                Integer prepaidMonths = 0;
                try {
                    prepaidMonths = DateUtil.getMonthDiff(nextMonthFirstDate,dueTime);
                } catch (ParseException e) {
                    logger.error("【物业缴费服务类】推算时间异常，ERROR：{}",e);
                }
                if (prepaidMonths == 0){
                    prepaidMonths = 1;
                }

                houseAndProprietorDTO.setPrepaidAmount(Tools.moneyHalfAdjust(billDetailedBackDO.getAmountReceivable()*prepaidMonths));
                houseAndProprietorDTO.setPrepaidMonths(prepaidMonths);
                houseAndProprietorDTO.setMinMonth(0);
            }else {
                try {
                    houseAndProprietorDTO.setPrepaidAmount(0d);
                    houseAndProprietorDTO.setMinMonth(DateUtil.getMonthDiff(dueTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY)));
                } catch (ParseException e) {
                    logger.error("【收费房间查询类】计算缴费月数异常，ERROR：{}",e);
                }
            }
            try {
                dueTime = DateUtil.getTimeDay(dueTime,DateUtil.YEAR_MONTH_DAY,-1);
            } catch (ParseException e) {
                logger.error("【前台缴费服务类】物业费过期时间减一天计算错误！ERROR：{}",e);
            }
            List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = queryExclusiveParkingSpaceService.queryExclusiveParkingSpaceService(roomCode,userInfoDO.getOrganizationId());
            houseAndProprietorDTO.setParkingSpaceManagementDOS(parkingSpaceManagementDOS);
            houseAndProprietorDTO.setOneMonthPropertyFee(billDetailedBackDO.getAmountReceivable());
            houseAndProprietorDTO.setDueTime(dueTime);
            houseAndProprietorDTO.setAmountOfArrears(Tools.moneyHalfAdjust(billDetailedBackDO.getAmountReceivable()*houseAndProprietorDTO.getMinMonth()));
            roomCodeS.add(houseAndProprietorDTO.getRoomCode());
            userIdS.add(houseAndProprietorDTO.getCustomerUserId());
        }
        return new PageData(pageNum,pageSize,totalPage,totalCount,houseAndProprietorDTOS);
    }

    @Override
    public List<ChargeItemBackDTO> findChargeItem(String roomCode,String token) throws Exception {
        String userId = JwtToken.getClaim(token,"userId");
        List<ChargeItemBackDTO> chargeItemBackDTOS = iFrontOfficeCashierDao.findChargeCodeList(roomCode);
        if (chargeItemBackDTOS.size()==0){
            throw new NullPointerException();
        }
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
//        List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = queryExclusiveParkingSpaceService.queryExclusiveParkingSpaceService(roomCode,userInfoDO.getOrganizationId());
        int count = 0;
        String chargeName = "";

        for (ChargeItemBackDTO chargeItemBackDTO:chargeItemBackDTOS) {
            if ("6".equals(chargeItemBackDTO.getChargeType())){
                chargeItemBackDTOS.remove(chargeItemBackDTO);
                break;
            }
        }

        for (ChargeItemBackDTO chargeItemBackDTO:chargeItemBackDTOS) {
            if ("14".equals(chargeItemBackDTO.getChargeType()) || "15".equals(chargeItemBackDTO.getChargeType()) || "16".equals(chargeItemBackDTO.getChargeType())) {
                List<BillDetailedDO> billDetailedDOS = iChargeDAO.findDetailByChargeCode(chargeItemBackDTO.getChargeCode());
                JSONArray chargeStandards = new JSONArray();
                for (BillDetailedDO billDetailedDO : billDetailedDOS) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("label", billDetailedDO.getChargeStandard());
                    jsonObject.put("value", billDetailedDO.getChargeStandard());
                    chargeStandards.add(jsonObject);
                }
                chargeItemBackDTO.setChargeStandards(chargeStandards.toJSONString());
            }
        }
//        if (parkingSpaceManagementDOS.size()>0){
//            for (ChargeItemBackDTO chargeItemBackDTO:chargeItemBackDTOS) {
//                if ("6".equals(chargeItemBackDTO.getChargeType())){
//                    if (parkingSpaceManagementDOS.size()>0){
//                        chargeName = chargeItemBackDTO.getChargeName();
//                        for (ParkingSpaceManagementDO parkingSpaceManagementDO:parkingSpaceManagementDOS) {
//                            if (count>0){
//                                ChargeItemBackDTO chargeItemBackDTO1 = new ChargeItemBackDTO();
////                            BeanUtils.copyProperties(chargeItemBackDTO1,chargeItemBackDTO);
//                                chargeItemBackDTO1.setChargeName(chargeName+"("+parkingSpaceManagementDO.getParkingSpaceCode()+")");
//                                chargeItemBackDTO1.setParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
//                                chargeItemBackDTO1.setChargeCode(chargeItemBackDTO.getChargeCode());
//                                chargeItemBackDTO1.setChargeType(chargeItemBackDTO.getChargeType());
//                                chargeItemBackDTO1.setDatedif(1);
//                                chargeItemBackDTOS.add(chargeItemBackDTO1);
//                            }else {
//                                chargeItemBackDTO.setParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
//                                chargeItemBackDTO.setChargeName(chargeItemBackDTO.getChargeName()+"("+parkingSpaceManagementDO.getParkingSpaceCode()+")");
//                                count++;
//                            }
//                        }
//                        break;
//                    }
//                }
//            }
//        }

        return chargeItemBackDTOS;
    }

    @Override
    public List<ParkingSpaceCostDetailDO> parkingSpaceRentalFeeCost(JSONObject jsonObject,String organizationId) throws ParseException, ConnectException, RentalParkingSpaceException {
//        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = JSONObject.toJavaObject(jsonObject.getJSONObject("parkingSpaceCostDetailDO"), ParkingSpaceCostDetailDO.class);
        String roomCode = jsonObject.getString("roomCode");
        JSONObject json = new JSONObject();
        json.put("datedif",parkingSpaceCostDetailDO.getDatedif());
        json.put("rentalRenewalStatus",parkingSpaceCostDetailDO.getRentalRenewalStatus());
        ParkingSpaceCostDetailDO parkingSpaceCostDetailDO1 = parkingSpaceDueTimeService.findRentalParkingSpaceDueTime(parkingSpaceCostDetailDO.getLicensePlateNumber(),roomCode,organizationId);
        if (1==parkingSpaceCostDetailDO.getRentalRenewalStatus()){
            if (null == parkingSpaceCostDetailDO1){
                throw new RentalParkingSpaceException();
            }
            parkingSpaceCostDetailDO = parkingSpaceCostDetailDO1;
            parkingSpaceCostDetailDO.setDatedif(json.getInteger("datedif"));
            parkingSpaceCostDetailDO.setRentalRenewalStatus(json.getInteger("rentalRenewalStatus"));

            //是续租查询上次租赁车位到期时间
            json.put("dueTime",parkingSpaceCostDetailDO1.getDueTime());
        }else {

            json.put("dueTime",parkingSpaceCostDetailDO.getStartTime());
        }
//        ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCodeAndOrganizationId(chargeItemBackDTO.getParkingSpaceCode(),organizationId);
//        ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(chargeItemBackDTO.getParkingSpaceCode(),organizationId,0);
        json.put("chargeCode",parkingSpaceCostDetailDO.getChargeCode());
        json.put("chargeName",parkingSpaceCostDetailDO.getChargeName());
        json.put("chargeType",parkingSpaceCostDetailDO.getChargeType());

        ICostStrategy iCostStrategy = new ParkingSpaceRentalFeeImpl();
        ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
        BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCodeAndParkingSpaceNatureAndParkingSpaceType(
                json.getString("chargeCode"),parkingSpaceCostDetailDO.getParkingSpacePlace().toString(),
                parkingSpaceCostDetailDO.getParkingSpaceType());

        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json, billDetailedDO);
        parkingSpaceCostDetailDO.setActualMoneyCollection(billDetailedBackDO.getActualMoneyCollection());
        parkingSpaceCostDetailDO.setAmountReceivable(billDetailedBackDO.getAmountReceivable());
        parkingSpaceCostDetailDO.setChargeStandard(billDetailedBackDO.getChargeStandard());
        parkingSpaceCostDetailDO.setDatedif(billDetailedBackDO.getDatedif());
        parkingSpaceCostDetailDO.setDiscount(billDetailedBackDO.getDiscount());
        parkingSpaceCostDetailDO.setDueTime(DateUtil.getAfterDay(billDetailedBackDO.getDueTime(),-1));
        parkingSpaceCostDetailDO.setStartTime(billDetailedBackDO.getStartTime());
        parkingSpaceCostDetailDO.setChargeUnit(billDetailedBackDO.getChargeUnit());

        String contractNumber = pinkingSpaceContractNumberService.contractNumber(parkingSpaceCostDetailDO.getRentalRenewalStatus(),roomCode,parkingSpaceCostDetailDO.getLicensePlateNumber(),organizationId);
        parkingSpaceCostDetailDO.setContractNumber(contractNumber);
        parkingSpaceCostDetailDO.setParkingSpaceCostDetailId(null);


        Double money = iChargeDAO.findMoneyBychargeTypeAndParkingSpacePlaceAndParkingSpaceTypeAndOrganizationId(6,parkingSpaceCostDetailDO.getParkingSpacePlace(),parkingSpaceCostDetailDO.getParkingSpaceType(),parkingSpaceCostDetailDO.getOrganizationId());
        ChargeItemBackDTO chargeItemBackDTO = iFrontOfficeCashierDao.findParkingManagementChargeItem(parkingSpaceCostDetailDO.getOrganizationId());
        ParkingSpaceCostDetailDO parkingSpaceCostDetailDO2 = new ParkingSpaceCostDetailDO();
        parkingSpaceCostDetailDO2.setOrganizationId(parkingSpaceCostDetailDO.getOrganizationId());
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(parkingSpaceCostDetailDO.getOrganizationId());
        parkingSpaceCostDetailDO2.setOrganizationName(organizationDO.getOrganizationName());
        parkingSpaceCostDetailDO2.setChargeType(billDetailedBackDO.getChargeType());
        parkingSpaceCostDetailDO2.setChargeName(billDetailedBackDO.getChargeName());
        parkingSpaceCostDetailDO2.setChargeCode(billDetailedBackDO.getChargeCode());
        parkingSpaceCostDetailDO2.setActualMoneyCollection(Tools.moneyHalfAdjust(money*parkingSpaceCostDetailDO.getDatedif()));
        parkingSpaceCostDetailDO2.setAmountReceivable(Tools.moneyHalfAdjust(money*parkingSpaceCostDetailDO.getDatedif()));
        parkingSpaceCostDetailDO2.setChargeStandard(money);
        parkingSpaceCostDetailDO2.setDatedif(billDetailedBackDO.getDatedif());
        parkingSpaceCostDetailDO2.setDiscount(billDetailedBackDO.getDiscount());
        parkingSpaceCostDetailDO2.setDueTime(parkingSpaceCostDetailDO.getDueTime());
        parkingSpaceCostDetailDO2.setStartTime(parkingSpaceCostDetailDO.getStartTime());
        parkingSpaceCostDetailDO2.setFloor(parkingSpaceCostDetailDO.getFloor());
        parkingSpaceCostDetailDO2.setChargeUnit(billDetailedBackDO.getChargeUnit());
        parkingSpaceCostDetailDO2.setLicensePlateColor(parkingSpaceCostDetailDO.getLicensePlateColor());
        parkingSpaceCostDetailDO2.setLicensePlateNumber(parkingSpaceCostDetailDO.getLicensePlateNumber());
        parkingSpaceCostDetailDO2.setLicensePlateType(parkingSpaceCostDetailDO.getLicensePlateType());
        parkingSpaceCostDetailDO2.setParkingSpaceCode(parkingSpaceCostDetailDO.getParkingSpaceCode());
        parkingSpaceCostDetailDO2.setParkingSpacePlace(parkingSpaceCostDetailDO.getParkingSpacePlace());
        parkingSpaceCostDetailDO2.setParkingSpaceType(parkingSpaceCostDetailDO.getParkingSpaceType());
        parkingSpaceCostDetailDO2.setVehicleOriginalType(parkingSpaceCostDetailDO.getVehicleOriginalType());
        parkingSpaceCostDetailDO2.setContractNumber(parkingSpaceCostDetailDO.getContractNumber());
        parkingSpaceCostDetailDO2.setVillageCode(parkingSpaceCostDetailDO.getVillageCode());
        parkingSpaceCostDetailDO2.setVillageName(parkingSpaceCostDetailDO.getVillageName());
        parkingSpaceCostDetailDO2.setRegionCode(parkingSpaceCostDetailDO.getRegionCode());
        parkingSpaceCostDetailDO2.setRegionName(parkingSpaceCostDetailDO.getRegionName());
        parkingSpaceCostDetailDO2.setBuildingCode(parkingSpaceCostDetailDO.getBuildingCode());
        parkingSpaceCostDetailDO2.setBuildingName(parkingSpaceCostDetailDO.getBuildingName());
        parkingSpaceCostDetailDO2.setFloor(parkingSpaceCostDetailDO.getFloor());
        parkingSpaceCostDetailDO2.setAuthorizedPersonIdNumber(parkingSpaceCostDetailDO.getAuthorizedPersonIdNumber());
        parkingSpaceCostDetailDO2.setAuthorizedPersonName(parkingSpaceCostDetailDO.getAuthorizedPersonName());
        parkingSpaceCostDetailDO2.setAuthorizedPersonPhone(parkingSpaceCostDetailDO.getAuthorizedPersonPhone());
        parkingSpaceCostDetailDO2.setSurname(parkingSpaceCostDetailDO.getSurname());
        parkingSpaceCostDetailDO2.setIdNumber(parkingSpaceCostDetailDO.getIdNumber());
        parkingSpaceCostDetailDO2.setMobilePhone(parkingSpaceCostDetailDO.getMobilePhone());

        parkingSpaceCostDetailDO.setAmountReceivable(parkingSpaceCostDetailDO.getAmountReceivable()-parkingSpaceCostDetailDO2.getAmountReceivable());
        parkingSpaceCostDetailDO.setActualMoneyCollection(parkingSpaceCostDetailDO.getActualMoneyCollection()-parkingSpaceCostDetailDO2.getActualMoneyCollection());

        List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOList = new ArrayList<>();
        parkingSpaceCostDetailDOList.add(parkingSpaceCostDetailDO);
        parkingSpaceCostDetailDOList.add(parkingSpaceCostDetailDO2);
        return parkingSpaceCostDetailDOList;
    }

    @Override
    public ParkingSpaceCostDetailDO parkingManagementFeeCost(JSONObject jsonObject, String organizationId) throws ParkingSpaceDueTimeException, ParseException, ConnectException {
        ChargeItemBackDTO chargeItemBackDTO = iFrontOfficeCashierDao.findParkingManagementChargeItem(organizationId);
        String parkingSpaceCode = jsonObject.getString("parkingSpaceCode");
        int datedif = jsonObject.getInteger("datedif");
        chargeItemBackDTO.setParkingSpaceCode(parkingSpaceCode);
        chargeItemBackDTO.setDatedif(datedif);
        /**
         * 车位管理费
         */
        JSONObject json = new JSONObject();
        json.put("chargeCode",chargeItemBackDTO.getChargeCode());
        json.put("chargeName",chargeItemBackDTO.getChargeName());
        json.put("chargeType",chargeItemBackDTO.getChargeType());
        json.put("datedif",datedif);
        String parkingSpaceDueTime = parkingSpaceDueTimeService.findExclusiveParkingSpaceDueTime(chargeItemBackDTO.getParkingSpaceCode(),organizationId);
        if (StringUtils.isBlank(parkingSpaceDueTime)){
            throw new ParkingSpaceDueTimeException();
        }
        //查询车位到期时间
        json.put("startTime",parkingSpaceDueTime);

        ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCodeAndOrganizationId(chargeItemBackDTO.getParkingSpaceCode(),organizationId);
        logger.info("【专有车位费用计算】查询车位基础信息成功：{}",parkingSpaceInfoDO);
        ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(chargeItemBackDTO.getParkingSpaceCode(),organizationId,0);
        logger.info("【专有车位费用计算】查询专有车位信息成功：{}",parkingSpaceManagementDO);

        ICostStrategy iCostStrategy = new ParkingSpaceManagerFeeImpl();
        ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
        BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCodeAndParkingSpaceNatureAndParkingSpaceType(
                json.getString("chargeCode"),parkingSpaceInfoDO.getParkingSpacePlace().toString(),
                parkingSpaceInfoDO.getParkingSpaceType());
        logger.info("【专有车位费用计算】查询收费项目信息成功：{}",billDetailedDO);
        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json, billDetailedDO);
        ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = new ParkingSpaceCostDetailDO();
        parkingSpaceCostDetailDO.setOrganizationId(organizationId);
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        parkingSpaceCostDetailDO.setOrganizationName(organizationDO.getOrganizationName());
        parkingSpaceCostDetailDO.setChargeType(billDetailedBackDO.getChargeType());
        parkingSpaceCostDetailDO.setChargeName(billDetailedBackDO.getChargeName());
        parkingSpaceCostDetailDO.setChargeCode(billDetailedBackDO.getChargeCode());
        parkingSpaceCostDetailDO.setActualMoneyCollection(billDetailedBackDO.getActualMoneyCollection());
        parkingSpaceCostDetailDO.setAmountReceivable(billDetailedBackDO.getAmountReceivable());
        parkingSpaceCostDetailDO.setChargeStandard(billDetailedBackDO.getChargeStandard());
        parkingSpaceCostDetailDO.setDatedif(billDetailedBackDO.getDatedif());
        parkingSpaceCostDetailDO.setDiscount(billDetailedBackDO.getDiscount());
        parkingSpaceCostDetailDO.setDueTime(DateUtil.getAfterDay(billDetailedBackDO.getDueTime(),-1));
        parkingSpaceCostDetailDO.setStartTime(billDetailedBackDO.getStartTime());
        parkingSpaceCostDetailDO.setFloor(parkingSpaceInfoDO.getFloor());
        parkingSpaceCostDetailDO.setChargeUnit(billDetailedBackDO.getChargeUnit());
        parkingSpaceCostDetailDO.setLicensePlateColor(parkingSpaceManagementDO.getLicensePlateColor());
        parkingSpaceCostDetailDO.setLicensePlateNumber(parkingSpaceManagementDO.getLicensePlateNumber());
        parkingSpaceCostDetailDO.setLicensePlateType(parkingSpaceManagementDO.getLicensePlateType());
        parkingSpaceCostDetailDO.setParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
        parkingSpaceCostDetailDO.setParkingSpacePlace(parkingSpaceInfoDO.getParkingSpacePlace());
        parkingSpaceCostDetailDO.setParkingSpaceType(parkingSpaceInfoDO.getParkingSpaceType());
        parkingSpaceCostDetailDO.setVehicleOriginalType(parkingSpaceManagementDO.getVehicleOriginalType());
        parkingSpaceCostDetailDO.setContractNumber(parkingSpaceManagementDO.getContractNumber());
        parkingSpaceCostDetailDO.setVillageCode(parkingSpaceInfoDO.getVillageCode());
        parkingSpaceCostDetailDO.setVillageName(parkingSpaceInfoDO.getVillageName());
        parkingSpaceCostDetailDO.setRegionCode(parkingSpaceInfoDO.getRegionCode());
        parkingSpaceCostDetailDO.setRegionName(parkingSpaceInfoDO.getRegionName());
        parkingSpaceCostDetailDO.setBuildingCode(parkingSpaceInfoDO.getBuildingCode());
        parkingSpaceCostDetailDO.setBuildingName(parkingSpaceInfoDO.getBuildingName());
        parkingSpaceCostDetailDO.setFloor(parkingSpaceInfoDO.getFloor());
        parkingSpaceCostDetailDO.setAuthorizedPersonIdNumber(parkingSpaceManagementDO.getAuthorizedPersonIdNumber());
        parkingSpaceCostDetailDO.setAuthorizedPersonName(parkingSpaceManagementDO.getAuthorizedPersonName());
        parkingSpaceCostDetailDO.setAuthorizedPersonPhone(parkingSpaceManagementDO.getAuthorizedPersonPhone());
        parkingSpaceCostDetailDO.setSurname(parkingSpaceManagementDO.getSurname());
        parkingSpaceCostDetailDO.setIdNumber(parkingSpaceManagementDO.getIdNumber());
        parkingSpaceCostDetailDO.setMobilePhone(parkingSpaceManagementDO.getMobilePhone());
        return parkingSpaceCostDetailDO;
    }




    @Override
    public Map<String,Object> findCost(JSONObject jsonObject, String userId) throws ParseException, ConnectException, ParkingSpaceDueTimeException {
        String villageCode = jsonObject.getString("villageCode");
        String villageName = jsonObject.getString("villageName");
        String regionCode = jsonObject.getString("regionCode");
        String regionName = jsonObject.getString("regionName");
        String roomCode = jsonObject.getString("roomCode");

        Integer identity = jsonObject.getInteger("identity");

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        Map<String,Object> map = new HashMap<>();
        double roomSize = jsonObject.getDouble("roomSize");
        //保存抵扣明细
        List<CostDeductionDO> costDeductionDTOS = new ArrayList<>();
        //账户余额
        Double surplus = jsonObject.getDouble("surplus");
        String customerUserId = jsonObject.getString("customerUserId");
        CustomerInfoDO customerInfoDO = customerDAO.findByUserId(customerUserId);

        Double actualTotalMoneyCollection = 0d;

        //物业费结束时间
//        String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(roomCode,customerUserId);
        //存储费用
        List<BillDetailedDO> billDetailedDOArrayList = new ArrayList<>();

        //存储车位相关费用
        List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOList = new ArrayList<>();

        JSONArray chargeItem = jsonObject.getJSONArray("chargeItem");
        List<ChargeItemBackDTO> chargeItemBackDTOS = JSONObject.parseArray(chargeItem.toJSONString(),ChargeItemBackDTO.class);
        for (ChargeItemBackDTO chargeItemBackDTO:chargeItemBackDTOS) {
            switch (Integer.parseInt(chargeItemBackDTO.getChargeType())){
                /**
                 * 计算 元/月.平方米
                 * 物业费：住宅、商业、办公
                 */
                case 1: case 2: case 3:{
//                    String propertyFeeDueTime = propertyFeeDueTimeService.propertyFeeDueTime(roomCode,organizationId);
                    //物业费传输房间面积，月数，收费项目编号
                    JSONObject json = new JSONObject();
                    json.put("roomSize",roomSize);
                    json.put("datedif",chargeItemBackDTO.getDatedif());
                    json.put("chargeCode",chargeItemBackDTO.getChargeCode());
                    json.put("chargeName",chargeItemBackDTO.getChargeName());
                    json.put("chargeType",chargeItemBackDTO.getChargeType());
                    json.put("roomCode",roomCode);
                    json.put("customerUserId",customerUserId);
                    json.put("startTime",propertyFeeDueTimeService.propertyFeeDueTime(roomCode,organizationId));
                    BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeItemBackDTO.getChargeCode(),chargeItemBackDTO.getDatedif());
                    ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedBackDO = null;
                    try {
                        billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
                        billDetailedBackDO.setDueTime(DateUtil.getTimeDay(billDetailedBackDO.getDueTime(),DateUtil.YEAR_MONTH_DAY,-1));
                        Double originalActualMoneyCollection = billDetailedBackDO.getActualMoneyCollection();

                        //三通费
//                        List<CostDeductionDO> threeWayFeeCostDeductionDOS = iChargeDAO.findThreeWayFee(roomCode,organizationId);
//                        if (0<threeWayFeeCostDeductionDOS.size()){
//                            Double monthlyPropertyFee = billDetailedBackDO.getAmountReceivable()/chargeItemBackDTO.getDatedif();//每月物业费
//                            int monthCount = 0;
//                            int daysCount = 0;
//                            String currentTime = DateUtil.date(DateUtil.YEAR_MONTH_DAY);
//                            String movementTime = billDetailedBackDO.getStartTime();
//                            if (-1==DateUtil.compareDate(movementTime,currentTime)){
//                                //计算物业费欠费时长
//                                for (int i = 0;;i++){
//                                    if (1==DateUtil.compareDate(movementTime,currentTime)){
//                                        break;
//                                    }
//                                    movementTime = DateUtil.getAfterMonth(movementTime,1);
//                                    monthCount++;
//                                }
//                            }
//                            if (-1==DateUtil.compareDate(DateUtil.getAfterMonth(movementTime,-1),currentTime)){
//                                daysCount = DateUtil.longOfTwoDate(DateUtil.getAfterMonth(movementTime,-1),currentTime);
//                            }
//                            int monthlyDay = DateUtil.daysMonth(Integer.parseInt(DateUtil.date(DateUtil.YEAR)),Integer.parseInt(DateUtil.date(DateUtil.MONTH)));//当月天数
//                            Double dailyCost = Tools.moneyHalfAdjust(monthlyPropertyFee/monthlyDay);//每天费用
//
//                            for (CostDeductionDO costDeductionDO:threeWayFeeCostDeductionDOS) {
//                                costDeductionDO.setDeductionCode(DeductionEnum.THREE_WAY_FEE.toString());
//                                costDeductionDO.setAmountDeductedThisTime(0d);
//                                costDeductionDO.setDeductionStatus(1);
//                                costDeductionDO.setOriginalDeductionStatus(1);
//                                Double arrearsMoney =  monthlyPropertyFee*monthCount + dailyCost*daysCount;
//                                if (costDeductionDO.getSurplusDeductibleMoney()<arrearsMoney){
//                                    if (billDetailedBackDO.getActualMoneyCollection()<costDeductionDO.getSurplusDeductibleMoney()){
//                                        costDeductionDO.setAmountDeductedThisTime(billDetailedBackDO.getActualMoneyCollection());
//                                        costDeductionDO.setSurplusDeductibleMoney(Tools.moneyHalfAdjust(costDeductionDO.getSurplusDeductibleMoney()-billDetailedBackDO.getActualMoneyCollection()));
//                                        costDeductionDO.setDeductionMethod("部分抵扣");
//                                        billDetailedBackDO.setActualMoneyCollection(0d);
//                                    }else {
//                                        billDetailedBackDO.setActualMoneyCollection(Tools.moneyHalfAdjust(billDetailedBackDO.getActualMoneyCollection()-costDeductionDO.getSurplusDeductibleMoney()));
//                                        costDeductionDO.setAmountDeductedThisTime(costDeductionDO.getSurplusDeductibleMoney());
//                                        costDeductionDO.setDeductionMethod("全额抵扣");
//                                        costDeductionDO.setSurplusDeductibleMoney(0d);
//                                    }
//                                }else {
//                                    Double superfluousMoney = costDeductionDO.getSurplusDeductibleMoney()-arrearsMoney;
//                                    if (billDetailedBackDO.getActualMoneyCollection()<costDeductionDO.getSurplusDeductibleMoney()){
//                                        costDeductionDO.setDeductionMethod("物业费金额不足以被抵扣");
//                                        costDeductionDO.setDeductionStatus(0);
//                                        logger.error("【物业费费用计算类】所缴物业费不足以抵扣三通费");
//                                    }else {
//                                        billDetailedBackDO.setActualMoneyCollection(Tools.moneyHalfAdjust(billDetailedBackDO.getActualMoneyCollection()-arrearsMoney-superfluousMoney/(costDeductionDO.getDiscount()/10d)));
//                                        costDeductionDO.setAmountDeductedThisTime(Tools.moneyHalfAdjust(arrearsMoney+superfluousMoney/(costDeductionDO.getDiscount()/10d)));
//                                        costDeductionDO.setDeductionMethod("全额抵扣");
//                                        costDeductionDO.setSurplusDeductibleMoney(0d);
//                                    }
//                                }
//                                costDeductionDO.setUniqueId(Integer.parseInt(Tools.random(1000,99999)));
//                                costDeductionDTOS.add(costDeductionDO);
//                            }
//                        }
                        if (1==identity){
                            //三通费
                            List<CostDeductionDO> threeWayFeeCostDeductionDOS = iChargeDAO.findThreeWayFee(roomCode,organizationId);
                            if (threeWayFeeCostDeductionDOS.size()>0){
                                for (CostDeductionDO threeWayCostDeductionDO:threeWayFeeCostDeductionDOS) {
                                    threeWayCostDeductionDO.setDeductionCode(DeductionEnum.THREE_WAY_FEE.toString());
                                    threeWayCostDeductionDO.setDeductionStatus(1);
                                    threeWayCostDeductionDO.setOriginalDeductionStatus(1);
                                    if (billDetailedBackDO.getActualMoneyCollection()<threeWayCostDeductionDO.getSurplusDeductibleMoney()){
                                        threeWayCostDeductionDO.setAmountDeductedThisTime(billDetailedBackDO.getActualMoneyCollection());
                                        threeWayCostDeductionDO.setSurplusDeductibleMoney(Tools.moneyHalfAdjust(threeWayCostDeductionDO.getSurplusDeductibleMoney()-billDetailedBackDO.getActualMoneyCollection()));
                                        threeWayCostDeductionDO.setDeductionMethod("部分抵扣");
                                        billDetailedBackDO.setActualMoneyCollection(0d);
                                    }else {
                                        billDetailedBackDO.setActualMoneyCollection(Tools.moneyHalfAdjust(billDetailedBackDO.getActualMoneyCollection()-threeWayCostDeductionDO.getSurplusDeductibleMoney()));
                                        threeWayCostDeductionDO.setAmountDeductedThisTime(threeWayCostDeductionDO.getSurplusDeductibleMoney());
                                        threeWayCostDeductionDO.setDeductionMethod("全额抵扣");
                                        threeWayCostDeductionDO.setSurplusDeductibleMoney(0d);
                                    }
                                    if (threeWayCostDeductionDO.getAmountDeductedThisTime()==0){
                                        threeWayCostDeductionDO.setDeductionStatus(0);
                                    }
                                    threeWayCostDeductionDO.setUniqueId(Integer.parseInt(Tools.random(1000,99999)));
                                    costDeductionDTOS.add(threeWayCostDeductionDO);
                                }
                            }


                            if (0<billDetailedBackDO.getActualMoneyCollection()){
                                //退款
                                List<CostDeductionDO> refundCostDeductionDOS = iChargeDAO.findRefundFee(roomCode,organizationId);
                                if (0<refundCostDeductionDOS.size()){
                                    for (CostDeductionDO refundCostDeductionDO:refundCostDeductionDOS) {
                                        refundCostDeductionDO.setDeductionCode(DeductionEnum.REFUND.toString());
                                        refundCostDeductionDO.setDeductionStatus(1);
                                        refundCostDeductionDO.setOriginalDeductionStatus(1);
                                        if (billDetailedBackDO.getActualMoneyCollection()<refundCostDeductionDO.getSurplusDeductibleMoney()){
                                            refundCostDeductionDO.setAmountDeductedThisTime(billDetailedBackDO.getActualMoneyCollection());
                                            refundCostDeductionDO.setSurplusDeductibleMoney(Tools.moneyHalfAdjust(refundCostDeductionDO.getSurplusDeductibleMoney()-billDetailedBackDO.getActualMoneyCollection()));
                                            refundCostDeductionDO.setDeductionMethod("部分抵扣");
                                            billDetailedBackDO.setActualMoneyCollection(0d);
                                        }else {
                                            billDetailedBackDO.setActualMoneyCollection(Tools.moneyHalfAdjust(billDetailedBackDO.getActualMoneyCollection()-refundCostDeductionDO.getSurplusDeductibleMoney()));
                                            refundCostDeductionDO.setAmountDeductedThisTime(refundCostDeductionDO.getSurplusDeductibleMoney());
                                            refundCostDeductionDO.setDeductionMethod("全额抵扣");
                                            refundCostDeductionDO.setSurplusDeductibleMoney(0d);
                                        }
                                        if (refundCostDeductionDO.getAmountDeductedThisTime()==0){
                                            refundCostDeductionDO.setDeductionStatus(0);
                                        }
                                        refundCostDeductionDO.setUniqueId(Integer.parseInt(Tools.random(1000,99999)));
                                        costDeductionDTOS.add(refundCostDeductionDO);
                                    }
                                }
                            }

                            if (0<billDetailedBackDO.getActualMoneyCollection()){
                                //优惠券
                                List<CostDeductionDO> couponCostDeductionDOS = iChargeDAO.findCouponFee(roomCode,organizationId);
                                if (0<couponCostDeductionDOS.size()){
                                    for (CostDeductionDO couponCostDeductionDO:couponCostDeductionDOS) {
                                        couponCostDeductionDO.setDeductionCode(DeductionEnum.COUPON.toString());
                                        couponCostDeductionDO.setDeductionStatus(1);
                                        couponCostDeductionDO.setOriginalDeductionStatus(1);
                                        if (billDetailedBackDO.getActualMoneyCollection()<couponCostDeductionDO.getSurplusDeductibleMoney()){
                                            couponCostDeductionDO.setAmountDeductedThisTime(billDetailedBackDO.getActualMoneyCollection());
                                            couponCostDeductionDO.setSurplusDeductibleMoney(Tools.moneyHalfAdjust(couponCostDeductionDO.getSurplusDeductibleMoney()-billDetailedBackDO.getActualMoneyCollection()));
                                            couponCostDeductionDO.setDeductionMethod("部分抵扣");
                                            billDetailedBackDO.setActualMoneyCollection(0d);
                                        }else {
                                            billDetailedBackDO.setActualMoneyCollection(Tools.moneyHalfAdjust(billDetailedBackDO.getActualMoneyCollection()-couponCostDeductionDO.getSurplusDeductibleMoney()));
                                            couponCostDeductionDO.setAmountDeductedThisTime(couponCostDeductionDO.getSurplusDeductibleMoney());
                                            couponCostDeductionDO.setDeductionMethod("全额抵扣");
                                            couponCostDeductionDO.setSurplusDeductibleMoney(0d);
                                        }
                                        if (couponCostDeductionDO.getAmountDeductedThisTime()==0){
                                            couponCostDeductionDO.setDeductionStatus(0);
                                        }
                                        couponCostDeductionDO.setUniqueId(Integer.parseInt(Tools.random(1000,99999)));
                                        costDeductionDTOS.add(couponCostDeductionDO);
                                    }
                                }
                            }
                        }
                        //账户余额
                        CostDeductionDO surplusCostDeductionDTO = new CostDeductionDO();
                        surplusCostDeductionDTO.setDeductibleMoney(surplus);
                        surplusCostDeductionDTO.setDeductibledMoney(0d);
                        surplusCostDeductionDTO.setSurplusDeductibleMoney(0d);
                        surplusCostDeductionDTO.setAmountDeductedThisTime(0d);
                        surplusCostDeductionDTO.setDeductionStatus(1);
                        surplusCostDeductionDTO.setOriginalDeductionStatus(1);
                        surplusCostDeductionDTO.setDeductionItem("账户余额");
                        surplusCostDeductionDTO.setDeductionCode(DeductionEnum.SURPLUS.toString());
                        surplusCostDeductionDTO.setUniqueId(Integer.parseInt(Tools.random(1000,99999)));

                        //处理抵扣余额
                        Double actualMoneyCollection = billDetailedBackDO.getActualMoneyCollection()-surplus;
                        if (surplus>0){
                            if (actualMoneyCollection>0){
                                surplusCostDeductionDTO.setAmountDeductedThisTime(surplus);
                                surplusCostDeductionDTO.setDeductionMethod("全额抵扣");
                                surplusCostDeductionDTO.setSurplusDeductibleMoney(0d);
                                billDetailedBackDO.setActualMoneyCollection(actualMoneyCollection);
                            }else {
                                actualMoneyCollection = 0d;
                                surplusCostDeductionDTO.setAmountDeductedThisTime(billDetailedBackDO.getActualMoneyCollection());
                                surplusCostDeductionDTO.setDeductionMethod("部分抵扣");
                                surplusCostDeductionDTO.setSurplusDeductibleMoney(Tools.moneyHalfAdjust(surplus-billDetailedBackDO.getActualMoneyCollection()));
                                billDetailedBackDO.setActualMoneyCollection(0d);
                            }
                        }else {
                            surplusCostDeductionDTO.setDeductionStatus(0);
                            surplusCostDeductionDTO.setOriginalDeductionStatus(0);
                        }
                        if (surplusCostDeductionDTO.getAmountDeductedThisTime()==0){
                            surplusCostDeductionDTO.setDeductionStatus(0);
                        }

                        costDeductionDTOS.add(surplusCostDeductionDTO);
                        billDetailedBackDO.setPayerUserId(customerInfoDO.getUserId());
                        billDetailedBackDO.setPayerName(customerInfoDO.getSurname());
                        billDetailedBackDO.setPayerPhone(customerInfoDO.getMobilePhone());
                        billDetailedBackDO.setStateOfArrears(0);
//                        billDetailedBackDO.setOriginalStateOfArrears(0);
                        billDetailedBackDO.setAmountDeductedThisTime(0d);
                        billDetailedBackDO.setSurplusDeductibleMoney(0d);
                        billDetailedBackDO.setDeductibledMoney(0d);
                        billDetailedBackDO.setDeductibleMoney(0d);
                        billDetailedBackDO.setDeductionRecord("0");
                        actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedBackDO.getActualMoneyCollection();
                        billDetailedBackDO.setActualMoneyCollection(originalActualMoneyCollection);
                        billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());
                        billDetailedDOArrayList.add(billDetailedBackDO);
                    } catch (ConnectException e) {
                        logger.error("【物业费费用计算类】服务器未响应！e:{}",e);
                    } catch (ParseException e) {
                        logger.error("【物业费费用计算类】时间计算错误，e：{}",e);
                    }


                    /**
                     * 违约金处理
                     */
//                    JSONObject jsonTime = null;
//                    try {
//                        jsonTime = liquidatedDamagesService.liquidatedDamagesExistence(roomCode,organizationId,dueTime);
//                    } catch (ParseException e) {
//                        logger.info("【物业费费用计算类】判断是否存在违约金异常，ERROR：{}",e);
//                    }if (1==jsonTime.getInteger("status")){
//                        //计算违约金
//                        Double actualMoneyCollection = Tools.moneyHalfAdjust(chargeItemBackCostDTO.getAmountReceivable()/chargeItemBackDTO.getDatedif());
//                        int monthCount = 0;
//                        try {
//                            monthCount = DateUtil.getMonthSpace(dueTime,jsonTime.getString("dueTimeAfter"));
//                        } catch (ParseException e) {
//                            logger.info("【物业费费用计算类】计算日期相差月数异常，ERROR：{}",e);
//                        }
//                        int days = 0;
//                        int count=0;
//                        String startTime = jsonTime.getString("dueTimeFront");
//                        int compareDateCount;
//                        String endTime = startTime;
//                        while (count!=-100){
//                            try {
//                                startTime = DateUtil.getAfterMonth(startTime,3);
//                            } catch (ParseException e) {
//                                logger.info("【物业费费用计算类】季度推移异常，ERROR：{}",e);
//                            }
//                            compareDateCount = DateUtil.compareDate(startTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY));
//                            JSONObject liquidatedDamages = new JSONObject();
//                            if (compareDateCount==0){
//                                try {
////                                    days = DateUtil.longOfTwoDate(endTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY));
//                                    days = DateUtil.longOfTwoDate(startTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY))+1;
//                                } catch (ParseException e) {
//                                    logger.info("【物业费费用计算类】计算日期相差天数异常，ERROR：{}",e);
//                                }
//                                liquidatedDamages.put("dueTimeFront",endTime);
//                                liquidatedDamages.put("dueTimeAfter",startTime);
//                                liquidatedDamages.put("amountMoney",Tools.moneyHalfAdjust(actualMoneyCollection*3*days*0.003));
//                                liquidatedDamages.put("days",days);
//                                liquidatedDamagesList.add(liquidatedDamages);
//                                endTime = startTime;
//                                count++;
//                                break;
//                            }else if (compareDateCount==1){
//                                break;
//                            }
//                            if (count==0){
//                                try {
////                                    days = DateUtil.longOfTwoDate(dueTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY));
//                                    days = DateUtil.longOfTwoDate(startTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY))+1;
//                                } catch (ParseException e) {
//                                    logger.info("【物业费费用计算类】计算日期相差天数异常，ERROR：{}",e);
//                                }
//                                liquidatedDamages.put("amountMoney",Tools.moneyHalfAdjust(actualMoneyCollection*monthCount*days*0.003));
//                                liquidatedDamages.put("dueTimeFront",jsonTime.getString("dueTimeFront"));
//                                liquidatedDamages.put("dueTimeAfter",jsonTime.getString("dueTimeAfter"));
//                                liquidatedDamages.put("days",days);
//                                liquidatedDamagesList.add(liquidatedDamages);
//                            }else {
//                                try {
////                                    days = DateUtil.longOfTwoDate(endTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY));
//                                    days = DateUtil.longOfTwoDate(startTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY))+1;
//                                } catch (ParseException e) {
//                                    logger.info("【物业费费用计算类】计算日期相差天数异常，ERROR：{}",e);
//                                }
//                                liquidatedDamages.put("dueTimeFront",endTime);
//                                liquidatedDamages.put("dueTimeAfter",startTime);
//                                liquidatedDamages.put("amountMoney",Tools.moneyHalfAdjust(actualMoneyCollection*3*days*0.003));
//                                liquidatedDamages.put("days",days);
//                                liquidatedDamagesList.add(liquidatedDamages);
//                            }
//                            endTime = startTime;
//                            count++;
//                        }
//                        logger.info("计算滞纳金金额，打印结果如下：\n{}",liquidatedDamagesList);
//                    }
                }break;
                /**
                 * 装修管理费
                 * 计算 元/平方米
                 *
                 */
                case 12:{
                    JSONObject json = new JSONObject();
                    json.put("roomSize",roomSize);
                    json.put("chargeCode",chargeItemBackDTO.getChargeCode());
                    json.put("chargeName",chargeItemBackDTO.getChargeName());
                    json.put("chargeType",chargeItemBackDTO.getChargeType());
                    ICostStrategy iCostStrategy = new DecorationManagementCostImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCode(chargeItemBackDTO.getChargeCode());
                    try {
                        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
                        billDetailedBackDO.setPayerUserId(customerInfoDO.getUserId());
                        billDetailedBackDO.setPayerName(customerInfoDO.getSurname());
                        billDetailedBackDO.setPayerPhone(customerInfoDO.getMobilePhone());
                        billDetailedBackDO.setStateOfArrears(0);
//                        billDetailedBackDO.setOriginalStateOfArrears(0);
                        billDetailedBackDO.setAmountDeductedThisTime(0d);
                        billDetailedBackDO.setSurplusDeductibleMoney(0d);
                        billDetailedBackDO.setDeductibledMoney(0d);
                        billDetailedBackDO.setDeductibleMoney(0d);
                        billDetailedBackDO.setDeductionRecord("0");
                        billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());

                        actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedBackDO.getActualMoneyCollection();

                        billDetailedDOArrayList.add(billDetailedBackDO);
                    } catch (ConnectException e) {
                        logger.error("【元/平方米费用计算类】服务器未响应！e:{}",e);
                    } catch (Exception e) {
                        logger.error("【元/平方米费用计算类】计算错误，e：{}",e);
                    }
                }break;
                /**
                 * 根据范围取金额
                 * 装修垃圾清运费
                 */
                case 13:{
                    JSONObject json = new JSONObject();
                    json.put("roomSize",roomSize);
                    json.put("chargeCode",chargeItemBackDTO.getChargeCode());
                    json.put("chargeName",chargeItemBackDTO.getChargeName());
                    json.put("chargeType",chargeItemBackDTO.getChargeType());
                    ICostStrategy iCostStrategy = new DecorationGarbageClearanceAndTransportationFeeImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCodeAndRangeOfValues(chargeItemBackDTO.getChargeCode(),roomSize);
                    try {
                        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
                        billDetailedBackDO.setPayerUserId(customerInfoDO.getUserId());
                        billDetailedBackDO.setPayerName(customerInfoDO.getSurname());
                        billDetailedBackDO.setPayerPhone(customerInfoDO.getMobilePhone());
                        billDetailedBackDO.setStateOfArrears(0);
//                        billDetailedBackDO.setOriginalStateOfArrears(0);
                        billDetailedBackDO.setAmountDeductedThisTime(0d);
                        billDetailedBackDO.setSurplusDeductibleMoney(0d);
                        billDetailedBackDO.setDeductibledMoney(0d);
                        billDetailedBackDO.setDeductibleMoney(0d);
                        billDetailedBackDO.setDeductionRecord("0");
                        billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());

                        actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedBackDO.getActualMoneyCollection();

                        billDetailedDOArrayList.add(billDetailedBackDO);
                    } catch (ConnectException e) {
                        logger.error("【装修垃圾清运费费用计算类】服务器未响应！e:{}",e);
                    } catch (Exception e) {
                        logger.error("【装修垃圾清运费费用计算类】计算错误，e：{}",e);
                    }
                }break;
                /**
                 * 元/户
                 * 定值缴费 保证金
                 *
                 */
                case 4:{
                    Double chargeStandard = chargeItemBackDTO.getChargeStandard();
                    JSONObject json = new JSONObject();
                    json.put("roomSize",roomSize);
                    json.put("chargeCode",chargeItemBackDTO.getChargeCode());
                    json.put("chargeName",chargeItemBackDTO.getChargeName());
                    json.put("chargeType",chargeItemBackDTO.getChargeType());
                    ICostStrategy iCostStrategy = new FixedValueComputationImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCode(chargeItemBackDTO.getChargeCode());
                    try {
                        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
                        billDetailedBackDO.setPayerUserId(customerInfoDO.getUserId());
                        billDetailedBackDO.setPayerName(customerInfoDO.getSurname());
                        billDetailedBackDO.setPayerPhone(customerInfoDO.getMobilePhone());
                        billDetailedBackDO.setStateOfArrears(0);
//                        billDetailedBackDO.setOriginalStateOfArrears(0);
                        billDetailedBackDO.setAmountDeductedThisTime(0d);
                        billDetailedBackDO.setSurplusDeductibleMoney(0d);
                        billDetailedBackDO.setDeductibledMoney(0d);
                        billDetailedBackDO.setDeductibleMoney(0d);
                        billDetailedBackDO.setDeductionRecord("0");
                        billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());
                        billDetailedBackDO.setActualMoneyCollection(chargeStandard);

                        actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedBackDO.getActualMoneyCollection();

                        billDetailedDOArrayList.add(billDetailedBackDO);
                    } catch (ConnectException e) {
                        logger.error("【定值计算类】服务器未响应！e:{}",e);
                    } catch (Exception e) {
                        logger.error("【定值计算类】计算错误，e：{}",e);
                    }
                }break;
                /**
                 * 车位租赁费
                 */
                case 5:{
                    ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = new ParkingSpaceCostDetailDO();
                    parkingSpaceCostDetailDO.setVillageCode(villageCode);
                    parkingSpaceCostDetailDO.setVillageName(villageName);
                    parkingSpaceCostDetailDO.setRegionCode(regionCode);
                    parkingSpaceCostDetailDO.setRegionName(regionName);
                    parkingSpaceCostDetailDO.setChargeCode(chargeItemBackDTO.getChargeCode());
                    parkingSpaceCostDetailDO.setChargeName(chargeItemBackDTO.getChargeName());
                    parkingSpaceCostDetailDO.setChargeType(chargeItemBackDTO.getChargeType());
                    parkingSpaceCostDetailDO.setOrganizationId(organizationId);
                    OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
                    parkingSpaceCostDetailDO.setOrganizationName(organizationDO.getOrganizationName());
                    parkingSpaceCostDetailDOList.add(parkingSpaceCostDetailDO);
                }break;
                /**
                 * 车位管理费
                 */
                case 6:{
                    JSONObject json = new JSONObject();
                    json.put("chargeCode",chargeItemBackDTO.getChargeCode());
                    json.put("chargeName",chargeItemBackDTO.getChargeName());
                    json.put("chargeType",chargeItemBackDTO.getChargeType());
                    json.put("datedif",chargeItemBackDTO.getDatedif());
                    String parkingSpaceDueTime = parkingSpaceDueTimeService.findExclusiveParkingSpaceDueTime(chargeItemBackDTO.getParkingSpaceCode(),organizationId);
                    if (StringUtils.isBlank(parkingSpaceDueTime)){
                        throw new ParkingSpaceDueTimeException();
                    }
                    //查询车位到期时间
                    json.put("startTime",parkingSpaceDueTime);

                    ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCodeAndOrganizationId(chargeItemBackDTO.getParkingSpaceCode(),organizationId);
                    ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(chargeItemBackDTO.getParkingSpaceCode(),organizationId,0);

                    ICostStrategy iCostStrategy = new ParkingSpaceManagerFeeImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCodeAndParkingSpaceNatureAndParkingSpaceType(
                            json.getString("chargeCode"),parkingSpaceInfoDO.getParkingSpacePlace().toString(),
                            parkingSpaceInfoDO.getParkingSpaceType());
                    BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json, billDetailedDO);
                    ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = new ParkingSpaceCostDetailDO();
                    parkingSpaceCostDetailDO.setOrganizationId(organizationId);
                    OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
                    parkingSpaceCostDetailDO.setOrganizationName(organizationDO.getOrganizationName());
                    parkingSpaceCostDetailDO.setChargeType(billDetailedBackDO.getChargeType());
                    parkingSpaceCostDetailDO.setChargeName(billDetailedBackDO.getChargeName());
                    parkingSpaceCostDetailDO.setChargeCode(billDetailedBackDO.getChargeCode());
                    parkingSpaceCostDetailDO.setActualMoneyCollection(billDetailedBackDO.getActualMoneyCollection());
                    parkingSpaceCostDetailDO.setAmountReceivable(billDetailedBackDO.getAmountReceivable());
                    parkingSpaceCostDetailDO.setChargeStandard(billDetailedBackDO.getChargeStandard());
                    parkingSpaceCostDetailDO.setDatedif(billDetailedBackDO.getDatedif());
                    parkingSpaceCostDetailDO.setDiscount(billDetailedBackDO.getDiscount());
                    parkingSpaceCostDetailDO.setDueTime(DateUtil.getAfterDay(billDetailedBackDO.getDueTime(),-1));
                    parkingSpaceCostDetailDO.setStartTime(billDetailedBackDO.getStartTime());
                    parkingSpaceCostDetailDO.setFloor(parkingSpaceInfoDO.getFloor());
                    parkingSpaceCostDetailDO.setChargeUnit(billDetailedBackDO.getChargeUnit());
                    parkingSpaceCostDetailDO.setLicensePlateColor(parkingSpaceManagementDO.getLicensePlateColor());
                    parkingSpaceCostDetailDO.setLicensePlateNumber(parkingSpaceManagementDO.getLicensePlateNumber());
                    parkingSpaceCostDetailDO.setLicensePlateType(parkingSpaceManagementDO.getLicensePlateType());
                    parkingSpaceCostDetailDO.setParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
                    parkingSpaceCostDetailDO.setParkingSpacePlace(parkingSpaceInfoDO.getParkingSpacePlace());
                    parkingSpaceCostDetailDO.setParkingSpaceType(parkingSpaceInfoDO.getParkingSpaceType());
                    parkingSpaceCostDetailDO.setVehicleOriginalType(parkingSpaceManagementDO.getVehicleOriginalType());
                    parkingSpaceCostDetailDO.setContractNumber(parkingSpaceManagementDO.getContractNumber());
                    parkingSpaceCostDetailDO.setVillageCode(parkingSpaceInfoDO.getVillageCode());
                    parkingSpaceCostDetailDO.setVillageName(parkingSpaceInfoDO.getVillageName());
                    parkingSpaceCostDetailDO.setRegionCode(parkingSpaceInfoDO.getRegionCode());
                    parkingSpaceCostDetailDO.setRegionName(parkingSpaceInfoDO.getRegionName());
                    parkingSpaceCostDetailDO.setBuildingCode(parkingSpaceInfoDO.getBuildingCode());
                    parkingSpaceCostDetailDO.setBuildingName(parkingSpaceInfoDO.getBuildingName());
                    parkingSpaceCostDetailDO.setFloor(parkingSpaceInfoDO.getFloor());
                    actualTotalMoneyCollection = actualTotalMoneyCollection + parkingSpaceCostDetailDO.getActualMoneyCollection();
                    parkingSpaceCostDetailDOList.add(parkingSpaceCostDetailDO);
                }break;
                /**
                 * 电费
                 */
//                case 7:{
//                    List<BillDetailedDO> billDetailedDOS = iChargeDAO.findWaterElectricFee(roomCode,"7");
//                    if (0<billDetailedDOS.size()){
//                        for (BillDetailedDO billDetailedBackDO:billDetailedDOS) {
//                            billDetailedBackDO.setOriginalStateOfArrears(0);
//                            billDetailedBackDO.setStateOfArrears(0);
////                            billDetailedBackDO.setAmountDeductedThisTime(0d);
////                            billDetailedBackDO.setSurplusDeductibleMoney(0d);
////                            billDetailedBackDO.setDeductibledMoney(0d);
////                            billDetailedBackDO.setDeductibleMoney(0d);
////                            billDetailedBackDO.setDeductionRecord("0");
//                            billDetailedDOArrayList.add(billDetailedBackDO);
//                        }
//                    }
//                }break;
                /**
                 * 水费
                 */
//                case 17: {
//                    List<BillDetailedDO> billDetailedDOS = iChargeDAO.findWaterElectricFee(roomCode);
//                    if (0<billDetailedDOS.size()){
//                        for (BillDetailedDO billDetailedBackDO:billDetailedDOS) {
//                            billDetailedBackDO.setOriginalStateOfArrears(0);
//                            billDetailedBackDO.setStateOfArrears(0);
//                            billDetailedDOArrayList.add(billDetailedBackDO);
//                        }
//                    }
//                }break;
                /**
                 * 欠款
                 */
                case 18: {
                    List<BillDetailedDO> billDetailedDOS = iChargeDAO.findWaterElectricFee(roomCode);
                    if (0<billDetailedDOS.size()){
                        for (BillDetailedDO billDetailedBackDO:billDetailedDOS) {
//                            billDetailedBackDO.setOriginalStateOfArrears(0);
                            billDetailedBackDO.setStateOfArrears(0);

                            actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedBackDO.getActualMoneyCollection();

                            billDetailedDOArrayList.add(billDetailedBackDO);
                        }
                    }
                }break;
                /**
                 * 门禁卡费，临时卡费....
                 */
                case 8: case 9:{
                    JSONObject json = new JSONObject();
                    json.put("roomSize",roomSize);
                    json.put("chargeCode",chargeItemBackDTO.getChargeCode());
                    json.put("chargeName",chargeItemBackDTO.getChargeName());
                    json.put("chargeType",chargeItemBackDTO.getChargeType());
                    json.put("datedif",chargeItemBackDTO.getDatedif());
                    ICostStrategy iCostStrategy = new QuantityChargeImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedDO = iChargeDAO.findByChargeCode(chargeItemBackDTO.getChargeCode());
                    try {
                        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
                        billDetailedBackDO.setPayerUserId(customerInfoDO.getUserId());
                        billDetailedBackDO.setPayerName(customerInfoDO.getSurname());
                        billDetailedBackDO.setPayerPhone(customerInfoDO.getMobilePhone());
                        billDetailedBackDO.setStateOfArrears(0);
//                        billDetailedBackDO.setOriginalStateOfArrears(0);
                        billDetailedBackDO.setAmountDeductedThisTime(0d);
                        billDetailedBackDO.setSurplusDeductibleMoney(0d);
                        billDetailedBackDO.setDeductibledMoney(0d);
                        billDetailedBackDO.setDeductibleMoney(0d);
                        billDetailedBackDO.setDeductionRecord("0");
                        billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());

                        actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedBackDO.getActualMoneyCollection();

                        billDetailedDOArrayList.add(billDetailedBackDO);
                    } catch (ConnectException e) {
                        logger.error("【元/月 或 元/张费用计算类】服务器未响应！e:{}",e);
                    } catch (Exception e) {
                        logger.error("【元/月 或 元/张费用计算类】计算错误，e：{}",e);
                    }
                }break;
                /**
                 * 三通费.电视开户费.....
                 */
                case 14: case 15: case 16:{

                    //三通费
//                    List<CostDeductionDO> threeWayFeeCostDeductionDOS = iChargeDAO.findThreeWayFee(roomCode,organizationId);
//                    if (0<threeWayFeeCostDeductionDOS.size()){
//                        Double monthlyPropertyFee = billDetailedBackDO.getAmountReceivable()/chargeItemBackDTO.getDatedif();//每月物业费

                    //查询与该房间有关的物业费的收费项目
                    ChargeItemDO chargeItemDO = iChargeDAO.findChargeItemProperty(roomCode,organizationId);

                    String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(roomCode,organizationId);
                    JSONObject json = new JSONObject();
                    json.put("roomSize",roomSize);
                    json.put("datedif",1);
                    json.put("chargeCode",chargeItemDO.getChargeCode());
                    json.put("chargeName",chargeItemDO.getChargeName());
                    json.put("chargeType",chargeItemDO.getChargeType());
                    json.put("roomCode",roomCode);
                    json.put("customerUserId",customerUserId);
                    json.put("startTime",dueTime);
                    BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeItemDO.getChargeCode(),1);
                    ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
                    ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
                    BillDetailedDO billDetailedBackDO1 = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
//                    billDetailedBackDO1.setDueTime(DateUtil.getTimeDay(billDetailedBackDO1.getDueTime(),DateUtil.YEAR_MONTH_DAY,-1));
//                    Double originalActualMoneyCollection = billDetailedBackDO1.getActualMoneyCollection();

                    //算出一个月的费用
                    double monthlyPropertyFee = billDetailedBackDO1.getAmountReceivable();

                    int monthCount = 0;
                    int daysCount = 0;
                    String currentTime = DateUtil.date(DateUtil.YEAR_MONTH_DAY);
                    String movementTime = billDetailedBackDO1.getStartTime();
                    if (-1==DateUtil.compareDate(movementTime,currentTime)){
                        //计算物业费欠费时长
                        for (int i = 0;;i++){
                            if (1==DateUtil.compareDate(movementTime,currentTime)){
                                break;
                            }
                            movementTime = DateUtil.getAfterMonth(movementTime,1);
                            monthCount++;
                        }
                    }
                    if (-1==DateUtil.compareDate(DateUtil.getAfterMonth(movementTime,-1),currentTime)){
                        daysCount = DateUtil.longOfTwoDate(DateUtil.getAfterMonth(movementTime,-1),currentTime);
                    }
                    int monthlyDay = DateUtil.daysMonth(Integer.parseInt(DateUtil.date(DateUtil.YEAR)),Integer.parseInt(DateUtil.date(DateUtil.MONTH)));//当月天数
                    Double dailyCost = Tools.moneyHalfAdjust(monthlyPropertyFee/monthlyDay);//每天费用
                    Double arrearsMoney =  monthlyPropertyFee*monthCount + dailyCost*daysCount;//欠费金额

                    List<BillDetailedDO> billDetailedDOS = iChargeDAO.findDetailByChargeCode(chargeItemBackDTO.getChargeCode());

                    BillDetailedDO billDetailedBackDO = new BillDetailedDO();

                    for (BillDetailedDO billDetailedDO1:billDetailedDOS) {
                        if (chargeItemBackDTO.getChargeStandard() == billDetailedDO1.getChargeStandard()){
                            billDetailedBackDO = billDetailedDO1;
                        }
                    }

//                    billDetailedBackDO.setDisplayModeStatus(1);
                    billDetailedBackDO.setDatedif(0);

                    billDetailedBackDO.setChargeStandard(chargeItemBackDTO.getChargeStandard());

                    double threeWayFeeExcessAmount = chargeItemBackDTO.getChargeStandard()-arrearsMoney; //算出三通费超出欠费的金额，这部分金额需要打折

                    ChargeDetailDO chargeDetailDO = iChargeDAO.findChargeThreeWayDetail(chargeItemBackDTO.getChargeCode(),chargeItemBackDTO.getChargeStandard(),organizationId);

                    double amountReceivable = 0d;
                    double actualMoneyCollection = 0d;
                    double reductionIncome = 0d;//减冲金额

                    if (threeWayFeeExcessAmount>0){
                        amountReceivable = Tools.moneyHalfAdjust(arrearsMoney+threeWayFeeExcessAmount/(chargeDetailDO.getDiscount()/10d));
                        actualMoneyCollection = chargeItemBackDTO.getChargeStandard();
                        reductionIncome = Tools.moneyHalfAdjust(amountReceivable - actualMoneyCollection);
                    }else {
                        amountReceivable = chargeItemBackDTO.getChargeStandard();
                        actualMoneyCollection = chargeItemBackDTO.getChargeStandard();
                    }

                    billDetailedBackDO.setChargeCode(chargeItemBackDTO.getChargeCode());
                    billDetailedBackDO.setChargeType(chargeItemBackDTO.getChargeType());
                    billDetailedBackDO.setChargeName(chargeItemBackDTO.getChargeName());
                    billDetailedBackDO.setDiscount(chargeDetailDO.getDiscount());

                    billDetailedBackDO.setAmountReceivable(amountReceivable);
                    billDetailedBackDO.setActualMoneyCollection(actualMoneyCollection);
                    billDetailedBackDO.setReductionIncome(reductionIncome);
                    billDetailedBackDO.setPayerUserId(customerInfoDO.getUserId());
                    billDetailedBackDO.setPayerName(customerInfoDO.getSurname());
                    billDetailedBackDO.setPayerPhone(customerInfoDO.getMobilePhone());
                    billDetailedBackDO.setStateOfArrears(0);
//                    billDetailedBackDO.setOriginalStateOfArrears(0);
                    billDetailedBackDO.setAmountDeductedThisTime(0d);
                    billDetailedBackDO.setSurplusDeductibleMoney(amountReceivable);
                    billDetailedBackDO.setDeductibledMoney(0d);
                    billDetailedBackDO.setDeductibleMoney(amountReceivable);
                    billDetailedBackDO.setDeductionRecord("0");
                    billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());

                    actualTotalMoneyCollection = actualTotalMoneyCollection + billDetailedBackDO.getActualMoneyCollection();

                    billDetailedDOArrayList.add(billDetailedBackDO);
                }break;
            }
        }
        /**
         * 计算应收总金额和实收总金额
         */
        Double amountTotalReceivable = 0d;
        int id = Integer.parseInt(Tools.random(10000,99999));
        for (BillDetailedDO billDetailedDO:billDetailedDOArrayList) {
            billDetailedDO.setCode(id);
            id = id+1;
            billDetailedDO.setParentCode(0);
            billDetailedDO.setSplitState(0);
            amountTotalReceivable+=billDetailedDO.getAmountReceivable();
//            actualTotalMoneyCollection+=billDetailedDO.getActualMoneyCollection();
        }
        for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOList){
            if ("6".equals(parkingSpaceCostDetailDO.getChargeType()))
            amountTotalReceivable+=parkingSpaceCostDetailDO.getAmountReceivable();
        }
        map.put("costDeduction",costDeductionDTOS);
        map.put("billDetailedDOArrayList",billDetailedDOArrayList);
        map.put("parkingSpaceCostDetailDOList",parkingSpaceCostDetailDOList);
        map.put("amountTotalReceivable",Tools.moneyHalfAdjust(amountTotalReceivable));
        map.put("actualTotalMoneyCollection",Tools.moneyHalfAdjust(actualTotalMoneyCollection));
        return map;
    }

    @Override
    @Transactional
    public synchronized List<BillsBackDTO> addBill(JSONObject jsonObject,String userId) throws Exception {
        //获取机构信息
        double oneMonthPropertyFee = jsonObject.getDouble("oneMonthPropertyFee");
        String organizationId = jsonObject.getString("organizationId");
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationName = organizationDO.getOrganizationName();
        //生成订单编号
        String orderId = thisSystemOrderIdService.thisSystemOrderId(organizationId);
//        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
//        Integer orderIdsCount = iBillDAO.findOrderIdsByorganizationId(organizationId,time);
//        if (orderIdsCount<9){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"000"+orderIdsCount;
//        }else if (orderIdsCount>=9&&orderIdsCount<99){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"00"+orderIdsCount;
//        }else if (orderIdsCount>=99&&orderIdsCount<999){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"0"+orderIdsCount;
//        }else {
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+orderIdsCount;
//        }
        //组装房间基础信息
        Double roomSize = jsonObject.getDouble("roomSize");
        String villageCode = jsonObject.getString("villageCode");
        String villageName = jsonObject.getString("villageName");
        String regionCode = jsonObject.getString("regionCode");
        String regionName = jsonObject.getString("regionName");
        String buildingCode = jsonObject.getString("buildingCode");
        String buildingName = jsonObject.getString("buildingName");
        String unitCode = jsonObject.getString("unitCode");
        String unitName = jsonObject.getString("unitName");
        String roomCode = jsonObject.getString("roomCode");

        //组装客户基础信息
        String idNumber = jsonObject.getString("idNumber");
        String surname = jsonObject.getString("surname");
        String mobilePhone = jsonObject.getString("mobilePhone");
        String payerPhone = jsonObject.getString("payerPhone");
        String payerName = jsonObject.getString("payerName");
        String paymentMethod = jsonObject.getString("paymentMethod");

        //token解析当前工作人员ID
        String tollCollectorId = userId;
        String remark = jsonObject.getString("remark");
        String customerUserId = jsonObject.getString("customerUserId");//指客户ID
        Double correctedAmount = jsonObject.getDouble("correctedAmount");//修正金额

        //订单涉及状态
        Integer stateOfArrears = 0;//欠费状态
        Integer refundStatus = 0;//退款状态
        Integer invalidState = 0;//注销状态

        //存放返回单据
        List<BillsBackDTO> billsBackDTOS = new ArrayList<>();

        //组装时间
        String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        String udt = DateUtil.date(DateUtil.FORMAT_PATTERN);

        //接收应收、实收金额、优惠金额
        Double amountTotalReceivable = jsonObject.getDouble("amountTotalReceivable");
        Double actualTotalMoneyCollection = jsonObject.getDouble("actualTotalMoneyCollection");
//        Double preferentialTotalAmount = amountTotalReceivable-actualTotalMoneyCollection;

        List<BillDetailedDO> billDetailedDOS = JSONObject.parseArray(jsonObject.getJSONArray("billDetailedDOArrayList").toJSONString(),BillDetailedDO.class);
        List<CostDeductionDO> costDeductionDOS = JSONObject.parseArray(jsonObject.getJSONArray("costDeduction").toJSONString(),CostDeductionDO.class);

        //车位管理缴费，暂时注释
        List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOList = JSONObject.parseArray(jsonObject.getJSONArray("parkingSpaceCostDetailDOList").toJSONString(),ParkingSpaceCostDetailDO.class);
        if (parkingSpaceCostDetailDOList.size()>0){
            for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOList){
                //效验车位过期时间
                if ("5".equals(parkingSpaceCostDetailDO.getChargeType())){
                    ParkingSpaceCostDetailDO parkingSpaceCostDetailDO1 = parkingSpaceDueTimeService.findRentalParkingSpaceDueTime(parkingSpaceCostDetailDO.getLicensePlateNumber(),roomCode,organizationId);
                    if (null!=parkingSpaceCostDetailDO1){
                        String parkingSpaceDueTime = parkingSpaceCostDetailDO1.getDueTime();
                        if (1==DateUtil.compareDate(parkingSpaceDueTime,parkingSpaceCostDetailDO.getStartTime())){
                            throw new ParkingSpaceDueTimeException();
                        }
                    }

                }else {
                    String parkingSpaceDueTime = parkingSpaceDueTimeService.findExclusiveParkingSpaceDueTime(parkingSpaceCostDetailDO.getParkingSpaceCode(),organizationId);
                    if (null!=parkingSpaceDueTime){
                        if (1==DateUtil.compareDate(parkingSpaceDueTime,parkingSpaceCostDetailDO.getStartTime())){
                            throw new ParkingSpaceDueTimeException();
                        }
                    }
                }
                parkingSpaceCostDetailDO.setOrderId(orderId);
                parkingSpaceCostDetailDO.setDueTime(DateUtil.getAfterDay(parkingSpaceCostDetailDO.getDueTime(),1));
            }
            parkingSpaceCostDetailDAO.saveAll(parkingSpaceCostDetailDOList);
        }


//        String detailed = chargeItemCostDTOS.toString();
//        String liquidatedDamages = jsonObject.getJSONArray("liquidatedDamages").toJSONString();

        //组装账单
        BillDO billDO = new BillDO();
        billDO.setOrderId(orderId);
        billDO.setIdNumber(idNumber);
        billDO.setCustomerUserId(customerUserId);
        billDO.setTollCollectorName(userInfoDO.getRealName());
        billDO.setRoomSize(roomSize);
        billDO.setOrganizationId(organizationId);
        billDO.setOrganizationName(organizationName);
        billDO.setVillageCode(villageCode);
        billDO.setVillageName(villageName);
        billDO.setRegionCode(regionCode);
        billDO.setRegionName(regionName);
        billDO.setBuildingCode(buildingCode);
        billDO.setBuildingName(buildingName);
        billDO.setUnitCode(unitCode);
        billDO.setUnitName(unitName);
        billDO.setRoomCode(roomCode);
        billDO.setSurname(surname);
        billDO.setMobilePhone(mobilePhone);
        billDO.setPayerName(payerName);
        billDO.setPayerPhone(payerPhone);
        billDO.setPaymentMethod(paymentMethod);
        billDO.setTollCollectorId(tollCollectorId);
//        billDO.setStateOfArrears(stateOfArrears);
        billDO.setRefundStatus(refundStatus);
        billDO.setInvalidState(invalidState);
        billDO.setCorrectedAmount(correctedAmount);
        billDO.setRemark(remark);
        billDO.setRealGenerationTime(idt);
        billDO.setIdt(idt);
        billDO.setUdt(udt);
        billDO.setAmountTotalReceivable(amountTotalReceivable);
        billDO.setActualTotalMoneyCollection(actualTotalMoneyCollection);

        //组装抵扣信息
        List<CostDeductionDO> costDeductionDOList = new ArrayList<>();
        for (CostDeductionDO costDeductionDO:costDeductionDOS) {
            costDeductionDO.setOrderId(orderId);
            if (1==costDeductionDO.getDeductionStatus()){
                costDeductionDOList.add(costDeductionDO);
            }
        }

//        List<OriginalBillDO> originalBillDOS = new ArrayList<OriginalBillDO>();

        List<BillDetailedDO> billDetailedDOList = new ArrayList<>();

        List<BillDO> billDOList = new ArrayList<>();

        //组装账单明细
        for (BillDetailedDO billDetailedDO:billDetailedDOS) {
            billDetailedDO.setOrganizationId(organizationId);
            //处理上次欠费的单
            if ("18".equals(billDetailedDO.getArrearsType())){
                billDetailedDAO.saveAndFlush(billDetailedDO);
                billDetailedDO.setBillDetailedId(null);
                billDetailedDO.setOrderId(orderId);
                billDetailedDOList.add(billDetailedDO);
            }else {
                billDetailedDO.setOrderId(orderId);
                Integer datedif = billDetailedDO.getDatedif();
                if ("1".equals(billDetailedDO.getChargeType())||"2".equals(billDetailedDO.getChargeType())||"3".equals(billDetailedDO.getChargeType())){
                    String validationDataDueTme = propertyFeeDueTimeService.propertyFeeDueTime(roomCode,organizationId);
                    billDetailedDO.setDueTime(DateUtil.getAfterDay(billDetailedDO.getDueTime(),1));
                    if (2!=billDetailedDO.getSplitState()){
                        if (!validationDataDueTme.equals(billDetailedDO.getStartTime())){
                            throw new ValidationDataAbnormalException();
                        }
                    }
                }
                //处理本次欠费的单
                if (1==billDetailedDO.getStateOfArrears()){
                    billDetailedDO.setArrearsType("18");
                    billDetailedDOList.add(billDetailedDO);
                }else {
                    billDetailedDOList.add(billDetailedDO);
                }
                /**
                 * 判断保证金，加入退款处理
                 */
                if("4".equals(billDetailedDO.getChargeType())){
                    RefundHistoryDO refundHistoryDO = new RefundHistoryDO();
                    refundHistoryDO.setOrganizationId(organizationId);
                    refundHistoryDO.setOrganizationName(organizationName);
                    refundHistoryDO.setOrderId(orderId);
                    refundHistoryDO.setActualMoneyCollection(billDetailedDO.getAmountReceivable());
                    refundHistoryDO.setBuildingCode(buildingCode);
                    refundHistoryDO.setBuildingName(buildingName);
                    refundHistoryDO.setChargeCode(billDetailedDO.getChargeCode());
                    refundHistoryDO.setChargeName(billDetailedDO.getChargeName());
                    refundHistoryDO.setChargeUnit(billDetailedDO.getChargeUnit());
                    refundHistoryDO.setDelayTime(0);
//                    refundHistoryDO.setDueTime(dueTime);
                    refundHistoryDO.setIdt(idt);
                    refundHistoryDO.setInvalidState(invalidState);
                    refundHistoryDO.setMobilePhone(mobilePhone);
                    refundHistoryDO.setIdNumber(idNumber);
                    refundHistoryDO.setMortgageAmount(0d);
                    refundHistoryDO.setPayerName(payerName);
                    refundHistoryDO.setPayerPhone(payerPhone);
                    refundHistoryDO.setPaymentMethod(paymentMethod);
                    refundHistoryDO.setChargeType(billDetailedDO.getChargeType());
//                    refundHistoryDO.setPersonLiable(personLiable);//责任人
//                    refundHistoryDO.setPersonLiablePhone(personLiablePhone);//责任人电话
                    refundHistoryDO.setRefundableAmount(billDetailedDO.getAmountReceivable());
                    refundHistoryDO.setRefundStatus(refundStatus);
                    refundHistoryDO.setRegionCode(regionCode);
                    refundHistoryDO.setRegionName(regionName);
                    refundHistoryDO.setRemark(remark);
//                    refundHistoryDO.setResponsibleAgencies(responsibleAgencies);//负责机构
                    refundHistoryDO.setRoomCode(roomCode);
                    refundHistoryDO.setRoomSize(roomSize);
                    RoomDO roomDO = roomDAO.findByRoomCode(roomCode);
                    if (roomDO.getRenovationStatus()==1){
                        refundHistoryDO.setStartTime(roomDO.getRenovationStartTime());//装修开始时间
                        refundHistoryDO.setDueTime(roomDO.getRenovationDeadline());
                    }
                    refundHistoryDO.setSurname(surname);
                    refundHistoryDO.setTollCollectorId(tollCollectorId);
                    refundHistoryDO.setUdt(udt);
                    refundHistoryDO.setUnitCode(unitCode);
                    refundHistoryDO.setUnitName(unitName);
                    refundHistoryDO.setCustomerUserId(customerUserId);
                    refundHistoryDO.setVillageCode(villageCode);
                    refundHistoryDO.setVillageName(villageName);
                    refundDAO.save(refundHistoryDO);
                }
            }





            //判断水电费
//            if ("7".equals(billDetailedDO.getChargeType())||"17".equals(billDetailedDO.getChargeType())){
//                billDetailedDAO.saveAndFlush(billDetailedDO);
//            }else {
//                //判断按月数/张数缴费的费用
//                if (datedif>0){
//                    String startTime = billDetailedDO.getStartTime();
//                    for (int i = 0; i < datedif; i++) {
//                        //判断有开始结束时间的费用
//                        if ("1".equals(billDetailedDO.getChargeType())||"2".equals(billDetailedDO.getChargeType())||"3".equals(billDetailedDO.getChargeType())){
////                            OriginalBillDO originalBillDO = new OriginalBillDO();
//                            originalBillDO.setOrderId(orderId);
//                            originalBillDO.setCustomerUserId(customerUserId);
//                            originalBillDO.setOrganizationId(organizationId);
//                            originalBillDO.setOrganizationName(organizationName);
//
//                            originalBillDO.setVillageCode(villageCode);
//                            originalBillDO.setVillageName(villageName);
//                            originalBillDO.setRegionCode(regionCode);
//                            originalBillDO.setRegionName(regionName);
//                            originalBillDO.setBuildingCode(buildingCode);
//                            originalBillDO.setBuildingName(buildingName);
//                            originalBillDO.setUnitCode(unitCode);
//                            originalBillDO.setUnitName(unitName);
//                            originalBillDO.setRoomCode(roomCode);
//                            originalBillDO.setRoomSize(roomSize);
//
//                            originalBillDO.setSurname(surname);
//                            originalBillDO.setMobilePhone(mobilePhone);
//                            originalBillDO.setPayerName(payerName);
//                            originalBillDO.setPayerPhone(payerPhone);
//                            originalBillDO.setPaymentMethod(paymentMethod);
//
//                            originalBillDO.setTollCollectorId(tollCollectorId);
//                            originalBillDO.setStateOfArrears(stateOfArrears);
//                            originalBillDO.setRefundStatus(refundStatus);
//                            originalBillDO.setInvalidState(invalidState);
//                            originalBillDO.setRemark(remark);
//                            originalBillDO.setIdt(idt);
//                            originalBillDO.setUdt(udt);
//
//                            originalBillDO.setChargeCode(billDetailedDO.getChargeCode());
//                            originalBillDO.setChargeName(billDetailedDO.getChargeName());
//                            originalBillDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                            originalBillDO.setChargeStandard(billDetailedDO.getChargeStandard());
//                            originalBillDO.setCurrentReadings(billDetailedDO.getCurrentReadings());
//                            originalBillDO.setLastReading(billDetailedDO.getLastReading());
//                            originalBillDO.setUsageAmount(billDetailedDO.getUsageAmount());
//                            originalBillDO.setDiscount(billDetailedDO.getDiscount());
//                            originalBillDO.setDatedif(1);
//
//                            originalBillDO.setAmountReceivable(billDetailedDO.getAmountReceivable()/datedif);
//                            originalBillDO.setActualMoneyCollection(billDetailedDO.getActualMoneyCollection()/datedif);
//
//                            originalBillDO.setStartTime(startTime);
//                            startTime = DateUtil.getAfterMonth(startTime,1);
//                            originalBillDO.setDueTime(startTime);
//                            originalBillDOS.add(originalBillDO);
//                        }else {
////                            OriginalBillDO originalBillDO = new OriginalBillDO();
//                            originalBillDO.setOrderId(orderId);
//                            originalBillDO.setCustomerUserId(customerUserId);
//                            originalBillDO.setOrganizationId(organizationId);
//                            originalBillDO.setOrganizationName(organizationName);
//
//                            originalBillDO.setVillageCode(villageCode);
//                            originalBillDO.setVillageName(villageName);
//                            originalBillDO.setRegionCode(regionCode);
//                            originalBillDO.setRegionName(regionName);
//                            originalBillDO.setBuildingCode(buildingCode);
//                            originalBillDO.setBuildingName(buildingName);
//                            originalBillDO.setUnitCode(unitCode);
//                            originalBillDO.setUnitName(unitName);
//                            originalBillDO.setRoomCode(roomCode);
//                            originalBillDO.setRoomSize(roomSize);
//
//                            originalBillDO.setSurname(surname);
//                            originalBillDO.setMobilePhone(mobilePhone);
//                            originalBillDO.setPayerName(payerName);
//                            originalBillDO.setPayerPhone(payerPhone);
//                            originalBillDO.setPaymentMethod(paymentMethod);
//
//                            originalBillDO.setTollCollectorId(tollCollectorId);
//                            originalBillDO.setStateOfArrears(stateOfArrears);
//                            originalBillDO.setRefundStatus(refundStatus);
//                            originalBillDO.setInvalidState(invalidState);
//                            originalBillDO.setRemark(remark);
//                            originalBillDO.setIdt(idt);
//                            originalBillDO.setUdt(udt);
//
//                            originalBillDO.setChargeCode(billDetailedDO.getChargeCode());
//                            originalBillDO.setChargeName(billDetailedDO.getChargeName());
//                            originalBillDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                            originalBillDO.setChargeStandard(billDetailedDO.getChargeStandard());
//                            originalBillDO.setCurrentReadings(billDetailedDO.getCurrentReadings());
//                            originalBillDO.setLastReading(billDetailedDO.getLastReading());
//                            originalBillDO.setUsageAmount(billDetailedDO.getUsageAmount());
//                            originalBillDO.setDiscount(billDetailedDO.getDiscount());
//                            originalBillDO.setDatedif(1);
//
//                            originalBillDO.setAmountReceivable(billDetailedDO.getAmountReceivable()/datedif);
//                            originalBillDO.setActualMoneyCollection(billDetailedDO.getActualMoneyCollection()/datedif);
//                            originalBillDOS.add(originalBillDO);
//                        }
//                    }
//                } else {
//                    /**
//                     * 判断保证金，加入退款处理
//                     */
//                    if("4".equals(billDetailedDO.getChargeType())){
//                        RefundHistoryDO refundHistoryDO = new RefundHistoryDO();
//                        refundHistoryDO.setOrganizationId(organizationId);
//                        refundHistoryDO.setOrganizationName(organizationName);
//                        refundHistoryDO.setOrderId(orderId);
//                        refundHistoryDO.setActualMoneyCollection(billDetailedDO.getAmountReceivable());
//                        refundHistoryDO.setBuildingCode(buildingCode);
//                        refundHistoryDO.setBuildingName(buildingName);
//                        refundHistoryDO.setChargeCode(billDetailedDO.getChargeCode());
//                        refundHistoryDO.setChargeName(billDetailedDO.getChargeName());
//                        refundHistoryDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                        refundHistoryDO.setDelayTime(0);
////                    refundHistoryDO.setDueTime(dueTime);
//                        refundHistoryDO.setIdt(idt);
//                        refundHistoryDO.setInvalidState(invalidState);
//                        refundHistoryDO.setMobilePhone(mobilePhone);
//                        refundHistoryDO.setIdNumber(idNumber);
//                        refundHistoryDO.setMortgageAmount(0d);
//                        refundHistoryDO.setPayerName(payerName);
//                        refundHistoryDO.setPayerPhone(payerPhone);
//                        refundHistoryDO.setPaymentMethod(paymentMethod);
//                        refundHistoryDO.setChargeType(billDetailedDO.getChargeType());
////                    refundHistoryDO.setPersonLiable(personLiable);//责任人
////                    refundHistoryDO.setPersonLiablePhone(personLiablePhone);//责任人电话
//                        refundHistoryDO.setRefundableAmount(billDetailedDO.getAmountReceivable());
//                        refundHistoryDO.setRefundStatus(refundStatus);
//                        refundHistoryDO.setRegionCode(regionCode);
//                        refundHistoryDO.setRegionName(regionName);
//                        refundHistoryDO.setRemark(remark);
////                    refundHistoryDO.setResponsibleAgencies(responsibleAgencies);//负责机构
//                        refundHistoryDO.setRoomCode(roomCode);
//                        refundHistoryDO.setRoomSize(roomSize);
//                        RoomDO roomDO = roomDAO.findByRoomCode(roomCode);
//                        if (roomDO.getRenovationStatus()==1){
//                            refundHistoryDO.setStartTime(roomDO.getRenovationStartTime());//装修开始时间
//                            refundHistoryDO.setDueTime(roomDO.getRenovationDeadline());
//                        }
//                        refundHistoryDO.setSurname(surname);
//                        refundHistoryDO.setTollCollectorId(tollCollectorId);
//                        refundHistoryDO.setUdt(udt);
//                        refundHistoryDO.setUnitCode(unitCode);
//                        refundHistoryDO.setUnitName(unitName);
//                        refundHistoryDO.setCustomerUserId(customerUserId);
//                        refundHistoryDO.setVillageCode(villageCode);
//                        refundHistoryDO.setVillageName(villageName);
//                        refundDAO.save(refundHistoryDO);
//                    }
////                    OriginalBillDO originalBillDO = new OriginalBillDO();
//                    originalBillDO.setOrderId(orderId);
//                    originalBillDO.setCustomerUserId(customerUserId);
//                    originalBillDO.setOrganizationId(organizationId);
//                    originalBillDO.setOrganizationName(organizationName);
//                    originalBillDO.setVillageCode(villageCode);
//                    originalBillDO.setVillageName(villageName);
//                    originalBillDO.setRegionCode(regionCode);
//                    originalBillDO.setRegionName(regionName);
//                    originalBillDO.setBuildingCode(buildingCode);
//                    originalBillDO.setBuildingName(buildingName);
//                    originalBillDO.setUnitCode(unitCode);
//                    originalBillDO.setUnitName(unitName);
//                    originalBillDO.setRoomCode(roomCode);
//                    originalBillDO.setRoomSize(roomSize);
//                    originalBillDO.setSurname(surname);
//                    originalBillDO.setMobilePhone(mobilePhone);
//                    originalBillDO.setPayerName(payerName);
//                    originalBillDO.setChargeType(billDetailedDO.getChargeType());
//                    originalBillDO.setPayerPhone(payerPhone);
//                    originalBillDO.setPaymentMethod(paymentMethod);
//                    originalBillDO.setTollCollectorId(tollCollectorId);
//                    originalBillDO.setStateOfArrears(stateOfArrears);
//                    originalBillDO.setRefundStatus(refundStatus);
//                    originalBillDO.setInvalidState(invalidState);
//                    originalBillDO.setRemark(remark);
//                    originalBillDO.setIdt(idt);
//                    originalBillDO.setUdt(udt);
//
//                    originalBillDO.setChargeCode(billDetailedDO.getChargeCode());
//                    originalBillDO.setChargeName(billDetailedDO.getChargeName());
//                    originalBillDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                    originalBillDO.setChargeStandard(billDetailedDO.getChargeStandard());
//                    originalBillDO.setCurrentReadings(billDetailedDO.getCurrentReadings());
//                    originalBillDO.setLastReading(billDetailedDO.getLastReading());
//                    originalBillDO.setUsageAmount(billDetailedDO.getUsageAmount());
//                    originalBillDO.setDiscount(billDetailedDO.getDiscount());
//                    originalBillDO.setDatedif(billDetailedDO.getDatedif());
//
//                    originalBillDO.setAmountReceivable(billDetailedDO.getAmountReceivable());
//                    originalBillDO.setActualMoneyCollection(billDetailedDO.getActualMoneyCollection());
////                    originalBillDO.setPreferentialAmount(billDetailedDO.getAmountReceivable()-billDetailedDO.getActualMoneyCollection());
//                    originalBillDO.setStartTime(billDetailedDO.getStartTime());
//                    originalBillDO.setDueTime(billDetailedDO.getDueTime());
//                    originalBillDOS.add(originalBillDO);
//                }
//            }
        }

        //处理修正金额
        if (0d<correctedAmount){
            Double sqlSurplus = iRoomAndCustomerDAO.findByRoomCodeAndUserIdSurplus(billDO.getRoomCode(),billDO.getCustomerUserId());
            iRoomAndCustomerDAO.updateSurplus(sqlSurplus+correctedAmount,billDO.getRoomCode(),billDO.getCustomerUserId());
        }

        //处理抵扣项目
        for (CostDeductionDO costDeductionDO:costDeductionDOList) {
            costDeductionDO.setOrganizationId(organizationId);
            if (costDeductionDO.getDeductionCode().equals(DeductionEnum.SURPLUS.toString())){
                CostDeductionDO surplusCostDeductionDTO = costDeductionDO;
                if (0<surplusCostDeductionDTO.getAmountDeductedThisTime()){
                    Double sqlSurplus = iRoomAndCustomerDAO.findByRoomCodeAndUserIdSurplus(billDO.getRoomCode(),billDO.getCustomerUserId());
                    iRoomAndCustomerDAO.updateSurplus(sqlSurplus-surplusCostDeductionDTO.getAmountDeductedThisTime(),billDO.getRoomCode(),billDO.getCustomerUserId());
                }
            }else if (costDeductionDO.getDeductionCode().equals(DeductionEnum.THREE_WAY_FEE.toString())){
                BillDetailedDO billDetailedDO = billDetailedDAO.findByBillDetailedId(costDeductionDO.getDeductionOrderId());
                billDetailedDO.setSurplusDeductibleMoney(costDeductionDO.getSurplusDeductibleMoney());
                billDetailedDO.setAmountDeductedThisTime(costDeductionDO.getAmountDeductedThisTime());
                billDetailedDO.setDeductibleMoney(costDeductionDO.getDeductibleMoney());
                billDetailedDO.setDeductibledMoney(Tools.moneyHalfAdjust(billDetailedDO.getDeductibledMoney()+costDeductionDO.getAmountDeductedThisTime()));
                if ("0".equals(billDetailedDO.getDeductionRecord())){
                    billDetailedDO.setDeductionRecord(billDetailedDO.getAmountDeductedThisTime().toString());
                }else {
                    billDetailedDO.setDeductionRecord(billDetailedDO.getDeductionRecord()+","+billDetailedDO.getAmountDeductedThisTime().toString());
                }
                billDetailedDAO.saveAndFlush(billDetailedDO);
            }else if (costDeductionDO.getDeductionCode().equals(DeductionEnum.REFUND.toString())){
                RefundApplicationDO refundApplicationDO = refundApplicationDAO.findById((int)costDeductionDO.getDeductionOrderId());
                refundApplicationDO.setSurplusDeductibleMoney(costDeductionDO.getSurplusDeductibleMoney());
                refundApplicationDO.setAmountDeductedThisTime(costDeductionDO.getAmountDeductedThisTime());
                refundApplicationDO.setDeductibleMoney(costDeductionDO.getDeductibleMoney());
                refundApplicationDO.setDeductibledMoney(Tools.moneyHalfAdjust(refundApplicationDO.getDeductibledMoney()+costDeductionDO.getAmountDeductedThisTime()));
                if (0==refundApplicationDO.getSurplusDeductibleMoney()){
                    RefundHistoryDO refundHistoryDO = refundDAO.findByOrderId(refundApplicationDO.getOrderId());
                    refundHistoryDO.setRefundStatus(1);
                    refundDAO.saveAndFlush(refundHistoryDO);
                }
                if ("0".equals(refundApplicationDO.getDeductionRecord())){
                    refundApplicationDO.setDeductionRecord(refundApplicationDO.getAmountDeductedThisTime().toString());
                }else {
                    refundApplicationDO.setDeductionRecord(refundApplicationDO.getDeductionRecord()+","+refundApplicationDO.getAmountDeductedThisTime().toString());
                }
                refundApplicationDAO.saveAndFlush(refundApplicationDO);
            }else if (costDeductionDO.getDeductionCode().equals(DeductionEnum.COUPON.toString())){
                RoomAndCouponDO roomAndCouponDO = roomAndCouponDAO.findById((int)costDeductionDO.getDeductionOrderId());
                roomAndCouponDO.setSurplusDeductibleMoney(costDeductionDO.getSurplusDeductibleMoney());
                roomAndCouponDO.setAmountDeductedThisTime(costDeductionDO.getAmountDeductedThisTime());
                roomAndCouponDO.setDeductibleMoney(costDeductionDO.getDeductibleMoney());
                roomAndCouponDO.setDeductibledMoney(Tools.moneyHalfAdjust(roomAndCouponDO.getDeductibledMoney()+costDeductionDO.getAmountDeductedThisTime()));
                if (0==roomAndCouponDO.getSurplusDeductibleMoney()){
                    roomAndCouponDO.setUsageState(2);
                }
                if ("0".equals(roomAndCouponDO.getDeductionRecord())){
                    roomAndCouponDO.setDeductionRecord(roomAndCouponDO.getAmountDeductedThisTime().toString());
                }else {
                    roomAndCouponDO.setDeductionRecord(roomAndCouponDO.getDeductionRecord()+","+roomAndCouponDO.getAmountDeductedThisTime().toString());
                }
                roomAndCouponDAO.saveAndFlush(roomAndCouponDO);
            }else {
                throw new CostDeductionException();
            }
        }
        billDOList.add(billDO);

        if (costDeductionDOList.size()>0){
            costDeductionDAO.saveAll(costDeductionDOList);
        }
//        originalBillDAO.saveAll(originalBillDOS);

        EventRecordDO eventRecordDO = new EventRecordDO();
        eventRecordDO.setOrganizationId(organizationId);
        eventRecordDO.setEventCode(orderId);
        eventRecordDO.setEventName("缴费提醒");
        eventRecordDO.setEventDescripte("房间号："+roomCode+",业主："+surname+",缴费成功！");
        eventRecordDO.setEventType("1");
        eventRecordDO.setOccurTime(DateUtil.date(DateUtil.FORMAT_PATTERN));
        eventRecordDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        eventRecordDAO.save(eventRecordDO);

        /**
         * 处理三通费抵扣
         * oneMonthPropertyFee
         */
        Double threeWayFeeTotalReceivables = 0d;
        Double threeWayFeeTotalActualIncome = 0d;

        int containThreeCount = 0;

        double discountThreeWayFee = 10d;

        List<BillDetailedDO> billDetailedDOList1 = new ArrayList<>();
        int count = 1;

        for (BillDetailedDO billDetailedDO:billDetailedDOList) {
            if ("14".equals(billDetailedDO.getChargeType())||"15".equals(billDetailedDO.getChargeType())||"16".equals(billDetailedDO.getChargeType())){
                threeWayFeeTotalReceivables = Tools.moneyHalfAdjust(threeWayFeeTotalReceivables+billDetailedDO.getAmountReceivable());
                threeWayFeeTotalActualIncome = Tools.moneyHalfAdjust(threeWayFeeTotalActualIncome+billDetailedDO.getActualMoneyCollection());
                discountThreeWayFee = billDetailedDO.getDiscount();
                billDetailedDO.setDeductibledMoney(billDetailedDO.getAmountReceivable());
                billDetailedDO.setSurplusDeductibleMoney(0d);
                billDetailedDO.setDeductionRecord(billDetailedDO.getAmountReceivable().toString());
                containThreeCount = 1;
            }
        }

        if (1==containThreeCount){
            String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(billDO.getRoomCode(),organizationId);
            /**
             * 效验三通费抵扣方式
             */
            if (!"30".equals(billDO.getPaymentMethod())){
                throw new PaymentMethodException();
            }

            String newOrderId = "";
            String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
            newOrderId = time+String.format("%08d", Integer.parseInt(orderId.substring(orderId.length()-8))+count);
            BillDO billDO1 = new BillDO();
            billDO1.setOrderId(newOrderId);
            billDO1.setRefundStatus(billDO.getRefundStatus());
            billDO1.setInvalidState(billDO.getInvalidState());
            billDO1.setOrganizationName(billDO.getOrganizationName());
            billDO1.setOrganizationId(billDO.getOrganizationId());
            String paymentMethod1 = "33";
//        if ("14".equals(billDetailedDO.getChargeType())){
//            //电视
//            paymentMethod1 = "20";
//        }else if ("15".equals(billDetailedDO.getChargeType())){
//            //天然气
//            paymentMethod1 = "21";
//        }else if ("16".equals(billDetailedDO.getChargeType())){
//            //电表
//            paymentMethod1 = "22";
//        }
            billDO1.setPaymentMethod(paymentMethod1);
            billDO1.setMobilePhone(billDO.getMobilePhone());
            billDO1.setIdNumber(billDO.getIdNumber());
            billDO1.setSurname(billDO.getSurname());
            billDO1.setCustomerUserId(billDO.getCustomerUserId());
            billDO1.setRoomSize(billDO.getRoomSize());
            billDO1.setVillageCode(billDO.getVillageCode());
            billDO1.setVillageName(billDO.getVillageName());
            billDO1.setRegionCode(billDO.getRegionCode());
            billDO1.setRegionName(billDO.getRegionName());
            billDO1.setBuildingCode(billDO.getBuildingCode());
            billDO1.setBuildingName(billDO.getBuildingName());
            billDO1.setUnitCode(billDO.getUnitCode());
            billDO1.setUnitName(billDO.getUnitName());
            billDO1.setRoomCode(billDO.getRoomCode());
            billDO1.setRealGenerationTime(DateUtil.date(DateUtil.FORMAT_PATTERN));
            billDO1.setCorrectedAmount(0d);
            billDO1.setTollCollectorId(tollCollectorId);
            billDO1.setTollCollectorName(userInfoDO.getRealName());
            billDO1.setOffsetOriginalDocNo(orderId);


            //计算抵扣费用
            /**
             * 统计物业费到期时间
             */
            for (BillDetailedDO billDetailedDO1:billDetailedDOList) {
                if ("1".equals(billDetailedDO1.getChargeType())||"2".equals(billDetailedDO1.getChargeType())||"3".equals(billDetailedDO1.getChargeType())){
                    dueTime = DateUtil.getAfterMonth(dueTime,billDetailedDO1.getDatedif());
                }
            }


            ChargeItemDO chargeItemDO = iChargeDAO.findChargeItemProperty(billDO.getRoomCode(),organizationId);

//                JSONObject json = new JSONObject();
//                json.put("roomSize",billDO.getRoomSize());
//                json.put("datedif",1);
//                json.put("chargeCode",chargeItemDO.getChargeCode());
//                json.put("chargeName",chargeItemDO.getChargeName());
//                json.put("chargeType",chargeItemDO.getChargeType());
//                json.put("startTime",dueTime);
            BillDetailedDO billDetailedDO1 = iChargeDAO.findPropertyCharge(chargeItemDO.getChargeCode(),1);
//                ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
//                ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
//                BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO1);
            double money = threeWayFeeTotalReceivables;
//                billDetailedBackDO.setDeductibleMoney(money);
//                billDetailedDO.setDeductibleMoney(money);
//                double amountReceivable1 = billDetailedBackDO.getAmountReceivable();

            int monthCount = 0;
            double startMonthAmount = 0d;
            int startDaysAmount = 0;
            String firstDayOfMonth = "";
            //判断不是月份的第一天抛出异常
            if (!DateUtil.isFirstDayOfMonth(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY))){
//                throw new DueTimeException();
                //获取物业费到期时间的月的天数
                monthCount = DateUtil.daysMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY)));
                firstDayOfMonth = DateUtil.getLastDayOfMonth(DateUtil.dateOfYear(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY)),DateUtil.dateOfMonth(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY)));
                startDaysAmount = DateUtil.longOfTwoDate(dueTime,firstDayOfMonth);
                //计算每天的物业费
                double everyDayPropertyFee = Tools.moneyHalfAdjust(oneMonthPropertyFee/monthCount);
                money = Tools.moneyHalfAdjust(money-everyDayPropertyFee*startDaysAmount);
            }

            /**
             * 计算物业费到期时间
             */
            BillDetailedDO billDetailedBackDO = new BillDetailedDO();
            billDetailedBackDO.setChargeCode(billDetailedDO1.getChargeCode());
            billDetailedBackDO.setChargeName(billDetailedDO1.getChargeName());
            billDetailedBackDO.setChargeType(billDetailedDO1.getChargeType());
            billDetailedBackDO.setChargeStandard(billDetailedDO1.getChargeStandard());
            billDetailedBackDO.setChargeUnit(billDetailedDO1.getChargeUnit());
            int datedif = (int) (money/oneMonthPropertyFee);
            billDetailedBackDO.setDatedif(datedif);
            billDetailedBackDO.setStartTime(dueTime);
            if ("".equals(firstDayOfMonth)){
                String dueTime0 = DateUtil.getAfterMonth(dueTime, datedif);
                billDetailedBackDO.setDueTime(dueTime0);
            }else {
                firstDayOfMonth = DateUtil.getAfterDay(firstDayOfMonth,1);
                String dueTime0 = DateUtil.getAfterMonth(firstDayOfMonth, datedif);
                billDetailedBackDO.setDueTime(dueTime0);
            }
            dueTime = billDetailedBackDO.getDueTime();
            if(datedif>0) {
                double amountReceivable = Tools.moneyHalfAdjust(oneMonthPropertyFee * datedif+startMonthAmount);
                double surplusDeductibleMoney = money - amountReceivable;
//            billDetailedDO.setDeductibledMoney(amountReceivable);
//            billDetailedDO.setSurplusDeductibleMoney(surplusDeductibleMoney);
//            if (amountReceivable > 0) {
//                billDetailedDO.setDeductionRecord(String.valueOf(amountReceivable));
//            } else {
//                billDetailedDO.setDeductionRecord("0");
//            }
                /**
                 * 计算应收实收金额
                 */
                billDetailedBackDO.setActualMoneyCollection(amountReceivable);
                billDetailedBackDO.setAmountReceivable(amountReceivable);
                billDetailedBackDO.setOrderId(orderId);
                billDetailedBackDO.setPayerPhone(billDO.getMobilePhone());
                billDetailedBackDO.setPayerName(billDO.getSurname());
                billDetailedBackDO.setPayerUserId(billDO.getCustomerUserId());
                billDetailedBackDO.setDeductionRecord("0");
                billDetailedBackDO.setDeductibledMoney(0d);
                billDetailedBackDO.setDeductibleMoney(0d);
                billDetailedBackDO.setCode(Integer.parseInt(Tools.random(100000, 999999)));
                billDetailedBackDO.setParentCode(0);
                billDetailedBackDO.setSplitState(0);
                billDetailedBackDO.setStateOfArrears(0);
                billDetailedBackDO.setOrganizationId(organizationId);
                billDetailedBackDO.setSurplusDeductibleMoney(0d);
                billDetailedBackDO.setAmountDeductedThisTime(0d);
                billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());
                billDetailedBackDO.setOrderId(newOrderId);
                billDetailedBackDO.setDeductibleSurplus(Tools.moneyHalfAdjust(surplusDeductibleMoney));
                billDetailedBackDO.setDiscount(discountThreeWayFee);

                billDO1.setAmountTotalReceivable(billDetailedBackDO.getAmountReceivable());
                billDO1.setActualTotalMoneyCollection(billDetailedBackDO.getActualMoneyCollection());

                billDO1.setIdt(billDO.getIdt());
//                BillsBackDTO billsBackDTO1 = new BillsBackDTO();
//                billsBackDTO1.setOrderId(billDO1.getOrderId());
//                billsBackDTO1.setOrganizationId(organizationId);
//                billsBackDTOS.add(billsBackDTO1);
                billDetailedDOList1.add(billDetailedBackDO);
                billDO1.setRemark("三通费金额"+Tools.moneyHalfAdjust(threeWayFeeTotalActualIncome)+"元97折后金额为："+threeWayFeeTotalReceivables+"元，本次抵扣金额为："+Tools.moneyHalfAdjust(billDetailedBackDO.getActualMoneyCollection())+"，剩余金额为："+Tools.moneyHalfAdjust(threeWayFeeTotalReceivables-billDetailedBackDO.getActualMoneyCollection())+"元续扣物业费。");
                billDO1.setCorrectedAmount(Tools.moneyHalfAdjust(threeWayFeeTotalReceivables-billDetailedBackDO.getActualMoneyCollection()));
                billDOList.add(billDO1);

                //处理修正金额
                Double sqlSurplus = iRoomAndCustomerDAO.findByRoomCodeAndUserIdSurplus(billDO.getRoomCode(),billDO.getCustomerUserId());
                iRoomAndCustomerDAO.updateSurplus(sqlSurplus+Tools.moneyHalfAdjust(threeWayFeeTotalReceivables-billDetailedBackDO.getActualMoneyCollection()),billDO.getRoomCode(),billDO.getCustomerUserId());
            }
        }

//        else {
//            double surplusDeductibleMoney = money;
//            billDetailedDO.setDeductionRecord("0");
//            billDetailedDO.setAmountDeductedThisTime(0d);
//            billDetailedDO.setDeductibledMoney(0d);
//            billDetailedDO.setSurplusDeductibleMoney(surplusDeductibleMoney);
//            billDO1.setRemark(billDetailedDO.getChargeName()+"抵扣，总金额为："+billDetailedDO.getAmountReceivable()+"，本次抵扣金额为：0.00，剩余金额为："+Tools.moneyHalfAdjust(surplusDeductibleMoney)+",\n剩余金额会自动存入账户，下次缴物业费时可作抵扣。");
//        }
//        count ++;



        billDetailedDOList.addAll(billDetailedDOList1);
        billDetailedDAO.saveAll(billDetailedDOList);
        redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,"NAN");
        billDAO.saveAll(billDOList);

        BillsBackDTO billsBackDTO = new BillsBackDTO();
        billsBackDTO.setOrderId(billDO.getOrderId());
        billsBackDTO.setOrganizationId(billDO.getOrganizationId());
        billsBackDTOS.add(billsBackDTO);
        return billsBackDTOS;
    }

    @Override
    public BillDetailedDO findChargeItemCostDTO(String chargeCode,int datedif) {
        return iChargeDAO.findPropertyCharge(chargeCode,datedif);
    }

    @Override
    public String printBilles(JSONObject jsonObject) throws ParseException {
        String orderId = jsonObject.getString("orderId");
        String organizationId = jsonObject.getString("organizationId");

        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        BillDO billDO = billDAO.findByOrderIdAndOrganizationId(orderId,organizationId);

//        List<String> chargeCodeList = iFrontOfficeCashierDao.findChargeCodesByRoomCodeAndChargeStatus(billDO.getRoomCode(),1);
//        String chargeCodes = Joiner.on(",").join(chargeCodeList);


//        List<ChargeItemCostDTO> chargeItemCostDTOS = JSONObject.parseArray(billDO.getDetailed(),ChargeItemCostDTO.class);
        List<BillDetailedDO> billDetailedDOS = billDetailedDAO.findAllByOrderIdAndSplitStateAndOrganizationIdAndStateOfArrears(billDO.getOrderId(),0,organizationId,0);
        List<BillDetailedDO> billDetailedDOList = billDetailedDAO.findAllByOrderIdAndSplitStateAndOrganizationIdAndStateOfArrears(billDO.getOrderId(),2,organizationId,0);
        billDetailedDOS.addAll(billDetailedDOList);
        for (BillDetailedDO billDetailedDO:billDetailedDOS) {
            if ("1".equals(billDetailedDO.getChargeType())||"2".equals(billDetailedDO.getChargeType())||"3".equals(billDetailedDO.getChargeType())){
                billDetailedDO.setDueTime(DateUtil.getAfterDay(billDetailedDO.getDueTime(),-1));
            }
        }

        BillHeaderDTO billHeaderDTO = new BillHeaderDTO();
        billHeaderDTO.setTitle(organizationDO.getBillName());
        billHeaderDTO.setOrganizationId(organizationDO.getOrganizationName());
        billHeaderDTO.setPrintDate(DateUtil.date(DateUtil.FORMAT_PATTERN));
        billHeaderDTO.setBuildingName(billDO.getBuildingName());
        billHeaderDTO.setTollCollectorName(billDO.getTollCollectorName());
        billHeaderDTO.setSurname(billDO.getSurname());
        billHeaderDTO.setRoomCode(billDO.getRoomCode());
        billHeaderDTO.setMobilePhone(billDO.getMobilePhone());
        billHeaderDTO.setSurplus(billDO.getCorrectedAmount());
        billHeaderDTO.setPayerName(billDO.getPayerName());
        billHeaderDTO.setPayerPhone(billDO.getPayerPhone());
        billHeaderDTO.setRoomSize(billDO.getRoomSize());
        billHeaderDTO.setRealGenerationTime(billDO.getRealGenerationTime());
        billHeaderDTO.setAmountTotalReceivable(billDO.getAmountTotalReceivable());
        billHeaderDTO.setActualTotalMoneyCollection(billDO.getActualTotalMoneyCollection());
        billHeaderDTO.setRemark(billDO.getRemark());
//        if (billDetailedDOS.size()>0){
//            if (null!=billDetailedDOS.get(0).getDeductibleSurplus()){
//                billHeaderDTO.setDeductibleSurplus(billDetailedDOS.get(0).getDeductibleSurplus());
//            }else {
//                billHeaderDTO.setDeductibleSurplus(-1d);
//            }
//        }
        ArrayList<BillSubjectDTO> billSubjectDTOS = new ArrayList<>();

        for (BillDetailedDO billDetailedDO:billDetailedDOS) {
            BillSubjectDTO billSubjectDTO = new BillSubjectDTO();
            billSubjectDTO.setChargeName(billDetailedDO.getChargeName());
            billSubjectDTO.setChargeStandard(billDetailedDO.getChargeStandard().toString());
            billSubjectDTO.setUsageAmount(billDetailedDO.getUsageAmount());
            billSubjectDTO.setDatedif(billDetailedDO.getDatedif());
            billSubjectDTO.setDiscount(billDetailedDO.getDiscount());
            String billingPeriod = " ";
            if (null==billDetailedDO.getStartTime()||"".equals(billDetailedDO.getStartTime())){

            }else {
                billingPeriod = billDetailedDO.getStartTime()+"至"+billDetailedDO.getDueTime();
            }
            billSubjectDTO.setPayableParty(billDetailedDO.getPayerName());
            billSubjectDTO.setBillingPeriod(billingPeriod);
            billSubjectDTO.setStateOfArrears("否");
            billSubjectDTO.setAmountReceivable(billDetailedDO.getAmountReceivable());
            billSubjectDTO.setActualMoneyCollection(billDetailedDO.getActualMoneyCollection());
            billSubjectDTOS.add(billSubjectDTO);
        }

        /**
         * 专有车位打印单据不用查房子到期时间
         */
        if (billDetailedDOS.size()>0){
            String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(billDO.getRoomCode(),organizationId);
            dueTime = DateUtil.getAfterDay(dueTime,-1);
            billHeaderDTO.setDueTime(dueTime);
        }


        /**
         * 抵扣项目处理
         */
//        ArrayList<LiquidatedDamagesDTO> liquidatedDamagesDTOS = (ArrayList<LiquidatedDamagesDTO>) JSONObject.parseArray(billDO.getLiquidatedDamages(),LiquidatedDamagesDTO.class);
        ArrayList<CostDeductionDO> costDeductionDOS = costDeductionDAO.findAllByOrderIdAndOrganizationId(billDO.getOrderId(),organizationId);
        ArrayList<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOS =(ArrayList<ParkingSpaceCostDetailDO>) parkingSpaceCostDetailDAO.findAllByOrderIdAndOrganizationId(billDO.getOrderId(),organizationId);
        for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOS) {
            parkingSpaceCostDetailDO.setDueTime(DateUtil.getAfterDay(parkingSpaceCostDetailDO.getDueTime(),-1));
        }
        billHeaderDTO.setBillSubjectDTOS(billSubjectDTOS);
        billHeaderDTO.setCostDeductionDOS(costDeductionDOS);
        billHeaderDTO.setParkingSpaceCostDetailDOS(parkingSpaceCostDetailDOS);

        try {
            if ("30".equals(billDO.getPaymentMethod())){
                //处理三通费抵扣单据
                BillDO billDO1 = billDAO.findByOffsetOriginalDocNoAndOrganizationId(orderId,organizationId);
                billHeaderDTO.setSurplus(billDO1.getCorrectedAmount());
                List<BillDetailedDO> billDetailedDOList1 = billDetailedDAO.findAllByOrderIdAndSplitStateAndOrganizationIdAndStateOfArrears(billDO1.getOrderId(),0,organizationId,0);
                int count = 0;
                ArrayList<BillSubjectDTO> billSubjectDTOS1 = billHeaderDTO.getBillSubjectDTOS();
                for (BillSubjectDTO billSubjectDTO:billSubjectDTOS1) {
                    if (count == 0){
                        billSubjectDTO.setDatedif(billDetailedDOList1.get(0).getDatedif());
                        billSubjectDTO.setDiscount(billDetailedDOList1.get(0).getDiscount());
                        billSubjectDTO.setBillingPeriod(billDetailedDOList1.get(0).getStartTime()+"至"+billDetailedDOList1.get(0).getDueTime());
                        count++;
                    }else {
                        billSubjectDTO.setDatedif(null);
                        billSubjectDTO.setDiscount(null);
                        billSubjectDTO.setBillingPeriod(null);
                    }
                }
                billHeaderDTO.setBillSubjectDTOS(billSubjectDTOS1);
                billHeaderDTO.setRemark(billDO1.getRemark());
            }
            Document document=new Document();
            FileOutputStream fileOutputStream = new FileOutputStream(Constants.SAVE_BILLS_PATH + orderId + "-" + organizationId+".pdf");
            PdfWriter pdfWriter = PdfWriter.getInstance(document,fileOutputStream);
            pdfWriter.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
            document.open();
            Rectangle pagesize=new Rectangle(A4);
            document.setPageSize(pagesize);
            document.setMargins(0.1F,0.1F,0.1F,0.1F);
            PrintPDFTable printPDFTable=new PrintPDFTable();
            printPDFTable.printTable(billHeaderDTO,document);
            document.close();
            return Constants.READ_BILLS_PATH+orderId+"-"+organizationId+".pdf";
        } catch (DocumentException e) {
            logger.error("【单据打印类】生成文档异常！e：{}",e);
        } catch (IOException e) {
            logger.error("【单据打印类】IO流异常！e：{}",e);
        }catch (Exception e){
            logger.error("【单据打印类】服务异常！e：{}",e);
        }
        return Constants.READ_BILLS_PATH+orderId+"-"+organizationId+".pdf";
//        return null;
    }

    @Override
    @Transactional
    public Map<String,Object> importOldBills(MultipartFile multipartFile, String userId) {
//
//        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
//        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
//
//        Workbook wb =null;
//        Sheet sheet = null;
//        Row row = null;
//        List<Map<String,String>> list = null;
//        String cellData = null;
////        String filePath = "D:\\newExcel.xlsx";
//        String columns[] = {"orderId","roomCode","mobilePhone","chargeCode","startTime","datedif","discount",
//                "amountTotalReceivable","actualTotalMoneyCollection","surplus","payerName","payerPhone",
//                "paymentMethod","lastReading","currentReadings","usageAmount","remarks"};
//        wb = ExcelPOI.readExcel(multipartFile);
//        if(wb != null){
//            //用来存放表中数据
//            list = new ArrayList<Map<String,String>>();
//            //获取第一个sheet
//            sheet = wb.getSheetAt(0);
//            //获取最大行数
//            int rownum = sheet.getPhysicalNumberOfRows();
//            //获取第一行
//            row = sheet.getRow(0);
//            //获取最大列数
//            int colnum = row.getPhysicalNumberOfCells();
//            for (int i = 1; i<rownum; i++) {
//                Map<String,String> map = new LinkedHashMap<String,String>();
//                row = sheet.getRow(i);
//                if(row !=null){
//                    for (int j=0;j<colnum;j++){
//                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
//                        map.put(columns[j], cellData);
//                    }
//                }else{
//                    break;
//                }
//                list.add(map);
//            }
//        }
//        logger.info("旧账单数据！数据量：{}",list.size());
//
//        Random random = new Random();
//        int ends = random.nextInt(99);
//        String.format("%02d",ends);//如果不足两位，前面补0
//        String importCode = DateUtil.timeStamp().toString()+ends;
//
//        String orderId = null;
//        String roomCode = null;
//        List<LogOldBillsDO> logOldBillsDOS = new ArrayList<>();
//        int count = 0;
//        for (int i = 0; i < list.size(); i++) {
//            try {
//                Map<String, String> map = new LinkedHashMap<String, String>();
//                map = list.get(i);
//                /**
//                 * 处理账单
//                 */
//                List<HouseAndProprietorDTO> houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByRoomCode(map.get("roomCode"), organizationId);
//                List<CustomerInfoDO> customerInfoDOS = customerDAO.findByMobilePhone(map.get("mobilePhone"));
//                orderId = map.get("orderId");
//                Double roomSize = houseAndProprietorDTOS.get(0).getRoomSize();
////                String userId = customerInfoDOS.get(0).getUserId();
//                String villageCode = houseAndProprietorDTOS.get(0).getVillageCode();
//                String villageName = houseAndProprietorDTOS.get(0).getVillageName();
//                String regionCode = houseAndProprietorDTOS.get(0).getRegionCode();
//                String regionName = houseAndProprietorDTOS.get(0).getRegionName();
//                String buildingCode = houseAndProprietorDTOS.get(0).getBuildingCode();
//                String buildingName = houseAndProprietorDTOS.get(0).getBuildingName();
//                String unitCode = houseAndProprietorDTOS.get(0).getUnitCode();
//                String unitName = houseAndProprietorDTOS.get(0).getUnitName();
//                roomCode = houseAndProprietorDTOS.get(0).getRoomCode();
//                String surname = null;
//                String mobilePhone = null;
//                if (customerInfoDOS.size() > 0) {
//                    surname = customerInfoDOS.get(0).getSurname();
//                    mobilePhone = customerInfoDOS.get(0).getMobilePhone();
//                }
//                String payerPhone = map.get("payerPhone");
//                String payerName = map.get("payerName");
//                String paymentMethod = map.get("paymentMethod");
//                //token解析当前工作人员ID
//                String tollCollectorId = userId;
//                String remark = null;
//                if (StringUtils.isNotBlank(map.get("remarks"))) {
//                    remark = map.get("remarks");
//                }
//                Double surplus = Double.parseDouble(map.get("surplus"));
//                Integer stateOfArrears = 0;
//                Integer refundStatus = 0;
//                Integer invalidState = 0;
//                String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
//                String udt = DateUtil.date(DateUtil.FORMAT_PATTERN);
//                Double amountTotalReceivable = Double.parseDouble(map.get("amountTotalReceivable"));
//                Double actualTotalMoneyCollection = Double.parseDouble(map.get("actualTotalMoneyCollection"));
//                Double preferentialTotalAmount = amountTotalReceivable - actualTotalMoneyCollection;
//                /**
//                 * 拼装费用详情
//                 */
//                String chargeCode = map.get("chargeCode");
//                ChargeItemCostDTO chargeItemCostDTO = iChargeDAO.findByChargeCode(chargeCode);
//                String chargeName = chargeItemCostDTO.getChargeName();
//                String chargeType = chargeItemCostDTO.getChargeType();
//                String chargeUnit = null;
//                Integer datedif = 0;
//                if (StringUtils.isNotBlank(map.get("datedif"))) {
//                    datedif = Integer.parseInt(map.get("datedif"));
//                }
//                Double chargeStandard = null;
//                Double discount = null;
//                String parkingSpaceNature = null;
//                String parkingSpaceType = null;
//                Double amountReceivable = amountTotalReceivable;
//                Double actualMoneyCollection = actualTotalMoneyCollection;
//                Double preferentialAmount = preferentialTotalAmount;
//                //水电费
//                Double usageAmount = null;
//                Double currentReadings = null;
//                Double lastReading = null;
//                String startTime = null;
//                String dueTime = null;
//                if ("1".equals(chargeType) || "2".equals(chargeType) || "3".equals(chargeType)) {
//                    ChargeItemCostDTO chargeItemCostDTO1 = iChargeDAO.findPropertyCharge(chargeCode, datedif);
//                    chargeUnit = chargeItemCostDTO.getChargeUnit();
//                    chargeStandard = chargeItemCostDTO1.getChargeStandard();
//                    startTime = map.get("startTime");
//                    dueTime = DateUtil.getAfterMonth(startTime, datedif);
//                    discount = Double.parseDouble(map.get("discount"));
//                } else if ("4".equals(chargeType)) {
//                    chargeUnit = chargeItemCostDTO.getChargeUnit();
//                    System.out.println("dfsijoikfsdoijfdoij::::::"+chargeItemCostDTO.getChargeStandard());
//                    chargeStandard = chargeItemCostDTO.getChargeStandard();
//                } else if ("7".equals(chargeType)) {
//                    chargeUnit = chargeItemCostDTO.getChargeUnit();
//                    chargeStandard = chargeItemCostDTO.getChargeStandard();
//                    usageAmount = Double.parseDouble(map.get("usageAmount"));
//                    currentReadings = Double.parseDouble(map.get("currentReadings"));
//                    lastReading = Double.parseDouble(map.get("lastReading"));
//                } else if ("8".equals(chargeType) || "9".equals(chargeType)) {
//                    chargeStandard = chargeItemCostDTO.getChargeStandard();
//                    chargeUnit = chargeItemCostDTO.getChargeUnit();
//                } else if ("12".equals(chargeType)) {
//                    chargeStandard = chargeItemCostDTO.getChargeStandard();
//                    chargeUnit = chargeItemCostDTO.getChargeUnit();
//                } else if ("13".equals(chargeType)) {
//                    ChargeItemCostDTO chargeItemCostDTO1 = iChargeDAO.findByChargeCodeAndRangeOfValues(chargeCode, roomSize);
//                    chargeStandard = chargeItemCostDTO1.getChargeStandard();
//                    chargeUnit = chargeItemCostDTO.getChargeUnit();
//                }
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("chargeCode", chargeCode);
//                jsonObject.put("chargeName", chargeName);
//                jsonObject.put("chargeType", chargeType);
//                jsonObject.put("chargeUnit", chargeUnit);
//                jsonObject.put("datedif", datedif);
//                jsonObject.put("chargeStandard", chargeStandard);
//                jsonObject.put("discount", discount);
//                jsonObject.put("amountReceivable", amountReceivable);
//                jsonObject.put("actualMoneyCollection", actualMoneyCollection);
//                jsonObject.put("preferentialAmount", preferentialAmount);
//                jsonObject.put("usageAmount", usageAmount);
//                jsonObject.put("currentReadings", currentReadings);
//                jsonObject.put("lastReading", lastReading);
//                jsonObject.put("startTime", startTime);
//                jsonObject.put("dueTime", dueTime);
//                jsonObject.put("parkingSpaceNature", parkingSpaceNature);
//                jsonObject.put("parkingSpaceType", parkingSpaceType);
//                JSONArray jsonArray = new JSONArray();
//                jsonArray.add(jsonObject);
//                List<ChargeItemCostDTO> chargeItemCostDTOS = JSONObject.parseArray(jsonArray.toJSONString(), ChargeItemCostDTO.class);
//                BillDO billDO = new BillDO();
//                billDO.setOrderId(orderId);
//                billDO.setUserId(customerInfoDOS.get(0).getUserId());
//                billDO.setRoomSize(roomSize);
//                billDO.setOrganizationId(organizationId);
//                billDO.setOrganizationName(organizationDO.getOrganizationName());
//                billDO.setVillageCode(villageCode);
//                billDO.setVillageName(villageName);
//                billDO.setRegionCode(regionCode);
//                billDO.setRegionName(regionName);
//                billDO.setBuildingCode(buildingCode);
//                billDO.setBuildingName(buildingName);
//                billDO.setUnitCode(unitCode);
//                billDO.setUnitName(unitName);
//                billDO.setRoomCode(roomCode);
//                billDO.setSurname(surname);
//                billDO.setMobilePhone(mobilePhone);
//                billDO.setPayerName(payerName);
//                billDO.setPayerPhone(payerPhone);
//                billDO.setPaymentMethod(paymentMethod);
//                billDO.setTollCollectorId(tollCollectorId);
//                billDO.setStateOfArrears(stateOfArrears);
//                billDO.setRefundStatus(refundStatus);
//                billDO.setInvalidState(invalidState);
//                billDO.setCorrectedAmount(surplus);
//                billDO.setRemark(remark);
//                billDO.setIdt(idt);
//                billDO.setUdt(udt);
//                billDO.setDetailed(jsonArray.toJSONString());
//                billDO.setAmountTotalReceivable(amountTotalReceivable);
//                billDO.setActualTotalMoneyCollection(actualTotalMoneyCollection);
//                billDO.setPreferentialTotalAmount(preferentialTotalAmount);
//                List<OriginalBillDO> originalBillDOS = new ArrayList<OriginalBillDO>();
//                for (ChargeItemCostDTO chargeItemCostDTO1 : chargeItemCostDTOS) {
//                    datedif = chargeItemCostDTO1.getDatedif();
//                    if (datedif > 0) {
//                        startTime = chargeItemCostDTO1.getStartTime();
//                        if (datedif == null) {
//                            datedif = 0;
//                        }
//                        for (int j = 0; j < datedif; j++) {
//                            OriginalBillDO originalBillDO = new OriginalBillDO();
//                            originalBillDO.setOrderId(orderId);
//                            originalBillDO.setUserId(customerInfoDOS.get(0).getUserId());
//                            originalBillDO.setOrganizationId(organizationId);
//                            originalBillDO.setOrganizationName(organizationDO.getOrganizationName());
//                            originalBillDO.setVillageCode(villageCode);
//                            originalBillDO.setVillageName(villageName);
//                            originalBillDO.setRegionCode(regionCode);
//                            originalBillDO.setRegionName(regionName);
//                            originalBillDO.setBuildingCode(buildingCode);
//                            originalBillDO.setBuildingName(buildingName);
//                            originalBillDO.setUnitCode(unitCode);
//                            originalBillDO.setUnitName(unitName);
//                            originalBillDO.setRoomCode(roomCode);
//                            originalBillDO.setRoomSize(roomSize);
//                            originalBillDO.setSurname(surname);
//                            originalBillDO.setMobilePhone(mobilePhone);
//                            originalBillDO.setPayerName(payerName);
//                            originalBillDO.setPaymentType(chargeItemCostDTO1.getChargeType());
//                            originalBillDO.setPayerPhone(payerPhone);
//                            originalBillDO.setPaymentMethod(paymentMethod);
//                            originalBillDO.setTollCollectorId(tollCollectorId);
//                            originalBillDO.setStateOfArrears(stateOfArrears);
//                            originalBillDO.setRefundStatus(refundStatus);
//                            originalBillDO.setInvalidState(invalidState);
//                            originalBillDO.setRemark(remark);
//                            originalBillDO.setChargeStatus(chargeItemCostDTO1.getChargeType());
//                            originalBillDO.setIdt(idt);
//                            originalBillDO.setUdt(udt);
//
//                            originalBillDO.setChargeCode(chargeItemCostDTO1.getChargeCode());
//                            originalBillDO.setChargeName(chargeItemCostDTO1.getChargeName());
//                            originalBillDO.setChargeUnit(chargeItemCostDTO1.getChargeUnit());
//                            originalBillDO.setChargeStandard(chargeItemCostDTO1.getChargeStandard());
//                            originalBillDO.setCurrentReadings(chargeItemCostDTO1.getCurrentReadings());
//                            originalBillDO.setLastReading(chargeItemCostDTO1.getLastReading());
//                            originalBillDO.setUsageAmount(chargeItemCostDTO1.getUsageAmount());
//                            originalBillDO.setDiscount(chargeItemCostDTO1.getDiscount());
//                            originalBillDO.setDatedif(chargeItemCostDTO1.getDatedif());
//
//                            originalBillDO.setAmountReceivable(chargeItemCostDTO1.getAmountReceivable() / datedif);
//                            originalBillDO.setActualMoneyCollection(chargeItemCostDTO1.getActualMoneyCollection() / datedif);
//                            originalBillDO.setPreferentialAmount((chargeItemCostDTO1.getPreferentialAmount() / datedif));
//                            originalBillDO.setStartTime(startTime);
//                            if (null != startTime) {
//                                startTime = DateUtil.getAfterMonth(startTime, 1);
//                            }
//                            originalBillDO.setDueTime(startTime);
//                            originalBillDOS.add(originalBillDO);
//                        }
//                    } else {
//                        OriginalBillDO originalBillDO = new OriginalBillDO();
//                        originalBillDO.setOrderId(orderId);
//                        originalBillDO.setUserId(customerInfoDOS.get(0).getUserId());
//                        originalBillDO.setOrganizationId(organizationId);
//                        originalBillDO.setOrganizationName(organizationDO.getOrganizationName());
//                        originalBillDO.setVillageCode(villageCode);
//                        originalBillDO.setVillageName(villageName);
//                        originalBillDO.setRegionCode(regionCode);
//                        originalBillDO.setRegionName(regionName);
//                        originalBillDO.setBuildingCode(buildingCode);
//                        originalBillDO.setBuildingName(buildingName);
//                        originalBillDO.setUnitCode(unitCode);
//                        originalBillDO.setUnitName(unitName);
//                        originalBillDO.setRoomCode(roomCode);
//                        originalBillDO.setRoomSize(roomSize);
//                        originalBillDO.setSurname(surname);
//                        originalBillDO.setMobilePhone(mobilePhone);
//                        originalBillDO.setPayerName(payerName);
//                        originalBillDO.setPaymentType(chargeItemCostDTO1.getChargeType());
//                        originalBillDO.setChargeStatus(chargeItemCostDTO1.getChargeType());
//                        originalBillDO.setPayerPhone(payerPhone);
//                        originalBillDO.setPaymentMethod(paymentMethod);
//                        originalBillDO.setTollCollectorId(tollCollectorId);
//                        originalBillDO.setStateOfArrears(stateOfArrears);
//                        originalBillDO.setRefundStatus(refundStatus);
//                        originalBillDO.setInvalidState(invalidState);
//                        originalBillDO.setRemark(remark);
//                        originalBillDO.setIdt(idt);
//                        originalBillDO.setUdt(udt);
//
//                        originalBillDO.setChargeCode(chargeItemCostDTO1.getChargeCode());
//                        originalBillDO.setChargeName(chargeItemCostDTO1.getChargeName());
//                        originalBillDO.setChargeUnit(chargeItemCostDTO1.getChargeUnit());
//                        originalBillDO.setChargeStandard(chargeItemCostDTO1.getChargeStandard());
//                        originalBillDO.setCurrentReadings(chargeItemCostDTO1.getCurrentReadings());
//                        originalBillDO.setLastReading(chargeItemCostDTO1.getLastReading());
//                        originalBillDO.setUsageAmount(chargeItemCostDTO1.getUsageAmount());
//                        originalBillDO.setDiscount(chargeItemCostDTO1.getDiscount());
//                        originalBillDO.setDatedif(chargeItemCostDTO1.getDatedif());
//
//                        originalBillDO.setAmountReceivable(chargeItemCostDTO1.getAmountReceivable());
//                        originalBillDO.setActualMoneyCollection(chargeItemCostDTO1.getActualMoneyCollection());
//                        originalBillDO.setPreferentialAmount(chargeItemCostDTO1.getAmountReceivable() - chargeItemCostDTO1.getActualMoneyCollection());
//                        originalBillDO.setStartTime(chargeItemCostDTO1.getStartTime());
//                        originalBillDO.setDueTime(chargeItemCostDTO1.getDueTime());
//                        originalBillDOS.add(originalBillDO);
//                        /**
//                         * 判断保证金，加入退款处理
//                         */
////                        if("4".equals(chargeItemCostDTO1.getChargeType())){
////                            RefundHistoryDO refundHistoryDO = new RefundHistoryDO();
////                            refundHistoryDO.setOrganizationId(organizationId);
////                            refundHistoryDO.setOrganizationName(organizationDO.getOrganizationName());
////                            refundHistoryDO.setOrderId(orderId);
////                            refundHistoryDO.setActualMoneyCollection(chargeItemCostDTO1.getAmountReceivable());
////                            refundHistoryDO.setBuildingCode(buildingCode);
////                            refundHistoryDO.setBuildingName(buildingName);
////                            refundHistoryDO.setChargeCode(chargeItemCostDTO1.getChargeCode());
////                            refundHistoryDO.setChargeName(chargeItemCostDTO1.getChargeName());
////                            refundHistoryDO.setChargeUnit(chargeItemCostDTO1.getChargeUnit());
////                            refundHistoryDO.setDelayTime(0);
//////                    refundHistoryDO.setDueTime(dueTime);
////                            refundHistoryDO.setIdt(idt);
////                            refundHistoryDO.setInvalidState(invalidState);
////                            refundHistoryDO.setMobilePhone(mobilePhone);
////                            refundHistoryDO.setMortgageAmount(0d);
////                            refundHistoryDO.setPayerName(payerName);
////                            refundHistoryDO.setPayerPhone(payerPhone);
////                            refundHistoryDO.setPaymentMethod(paymentMethod);
////                            refundHistoryDO.setPaymentType(chargeItemCostDTO1.getChargeType());
//////                    refundHistoryDO.setPersonLiable(personLiable);//责任人
//////                    refundHistoryDO.setPersonLiablePhone(personLiablePhone);//责任人电话
////                            refundHistoryDO.setRefundableAmount(chargeItemCostDTO1.getAmountReceivable());
////                            refundHistoryDO.setRefundStatus(refundStatus);
////                            refundHistoryDO.setRegionCode(regionCode);
////                            refundHistoryDO.setRegionName(regionName);
////                            refundHistoryDO.setRemark(remark);
//////                    refundHistoryDO.setResponsibleAgencies(responsibleAgencies);//负责机构
////                            refundHistoryDO.setRoomCode(roomCode);
////                            refundHistoryDO.setRoomSize(roomSize);
//////                    refundHistoryDO.setStartTime(startTime);//装修开始时间
////                            refundHistoryDO.setSurname(surname);
////                            refundHistoryDO.setTollCollectorId(tollCollectorId);
////                            refundHistoryDO.setUdt(udt);
////                            refundHistoryDO.setUnitCode(unitCode);
////                            refundHistoryDO.setUnitName(unitName);
////                            refundHistoryDO.setUserId(customerInfoDOS.get(0).getUserId());
////                            refundHistoryDO.setVillageCode(villageCode);
////                            refundHistoryDO.setVillageName(villageName);
////                            refundDAO.save(refundHistoryDO);
////                        }
//                    }
//                }
//                Double sqlSurplus = iRoomAndCustomerDAO.findByRoomCodeAndUserIdSurplus(billDO.getRoomCode(), billDO.getUserId());
//                iRoomAndCustomerDAO.updateSurplus(sqlSurplus + surplus, billDO.getRoomCode(), billDO.getUserId());
//                billDAO.save(billDO);
//                originalBillDAO.saveAll(originalBillDOS);
//
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入成功");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//                count++;
//            } catch (IndexOutOfBoundsException e){
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：房间号或手机号填写错误！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            } catch (NullPointerException e){
//                logger.error("【旧账单导入服务类】导入旧账单异常，ERROR：{}", e);
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：必填字段不能为空！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            }catch(ParseException e){
//                logger.error("【旧账单导入服务类】导入旧账单异常，ERROR：{}", e);
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：时间计算错误！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            }
//            logOldBillsDAO.saveAll(logOldBillsDOS);
//        }
//        Map<String,Object> map = new HashMap<>();
//        map.put("totalNumber",list.size()); //导入数据条数
//        map.put("realNumber",count); //导入成功条数
//        map.put("logOldBillsDOS",logOldBillsDOS);
//        return map;








//        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
//        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
//
//        Workbook wb =null;
//        Sheet sheet = null;
//        Row row = null;
//        List<Map<String,String>> list = null;
//        String cellData = null;
////        String filePath = "D:\\newExcel.xlsx";
//        String columns[] = {"orderId","realGenerationTime","roomCode","chargeType","chargeStandard","startTime","dueTime",
//                "discount","amountReceivable","actualMoneyCollection","transferCardAmount","deductibledMoney",
//                "correctedAmount","paymentMethod","stateOfArrears","lastReading","currentReadings","payerPhone",
//                "payerName","remarks","deductionOrderId","couponName","money"};
//        wb = ExcelPOI.readExcel(multipartFile);
//        if(wb != null){
//            //用来存放表中数据
//            list = new ArrayList<Map<String,String>>();
//            //获取第一个sheet
//            sheet = wb.getSheetAt(0);
//            //获取最大行数
//            int rownum = sheet.getPhysicalNumberOfRows();
//            //获取第一行
//            row = sheet.getRow(0);
//            //获取最大列数
//            int colnum = row.getPhysicalNumberOfCells();
//            for (int i = 1; i<rownum; i++) {
//                Map<String,String> map = new LinkedHashMap<String,String>();
//                row = sheet.getRow(i);
//                if(row !=null){
//                    for (int j=0;j<colnum;j++){
//                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
//                        map.put(columns[j], cellData);
//                    }
//                }else{
//                    break;
//                }
//                list.add(map);
//            }
//        }
//        logger.info("读取旧账单数据成功！数据量：{}",list.size());
//
//        Random random = new Random();
//        int ends = random.nextInt(99);
//        String.format("%02d",ends);//如果不足两位，前面补0
//        String importCode = DateUtil.timeStamp().toString()+ends;
//
//        List<LogOldBillsDO> logOldBillsDOS = new ArrayList<>();
//        int count = 0;
//        String orderId = "";
//        String roomCode = "";
//        flag: for (int i = 0; i < list.size(); i++) {
//            try{
//                Map<String, String> map = new HashMap<>();
//                map = list.get(i);
//
//                orderId = map.get("orderId");
//                String realGenerationTime = map.get("realGenerationTime");
//                roomCode = map.get("roomCode");
//                String chargeType = map.get("chargeType");
//                Double chargeStandard = Double.parseDouble(map.get("chargeStandard"));
//                String startTime = null;
//                String dueTime = null;
//                if ("1".equals(chargeType)||"2".equals(chargeType)||"3".equals(chargeType)||"5".equals(chargeType)||"6".equals(chargeType)){
//                    startTime = map.get("startTime");
//                    dueTime = map.get("dueTime");
//                }
//                Double discount = Double.parseDouble(map.get("discount"));
//                Double amountReceivable = Double.parseDouble(map.get("amountReceivable"));
//                Double actualMoneyCollection = Double.parseDouble(map.get("actualMoneyCollection"));
//                Double transferCardAmount = 0d;
//                if ("4".equals(chargeType)){
//                    transferCardAmount = Double.parseDouble(map.get("transferCardAmount"));
//                }
//                Double deductibledMoney = 0d;
//                if (StringUtils.isNotBlank(map.get("deductibledMoney"))){
//                    deductibledMoney = Double.parseDouble(map.get("deductibledMoney"));
//                }
//                Double correctedAmount = Double.parseDouble(map.get("correctedAmount"));
//                String paymentMethod = map.get("paymentMethod");
//                Integer stateOfArrears = Integer.parseInt(map.get("stateOfArrears"));
//                Double lastReading = 0d;
//                Double currentReadings = 0d;
//                if ("7".equals(chargeType)||"17".equals(chargeType)){
//                    lastReading = Double.parseDouble(map.get("lastReading"));
//                    currentReadings = Double.parseDouble(map.get("currentReadings"));
//                }
//                String payerPhone = null;
//                String payerName = null;
//                String remarks = null;
//                if (StringUtils.isNotBlank(map.get("payerPhone"))){
//                    payerPhone = map.get("payerPhone");
//                }
//                if (StringUtils.isNotBlank(map.get("payerName"))){
//                    payerName = map.get("payerName");
//                }
//                if (StringUtils.isNotBlank(map.get("remarks"))){
//                    remarks = map.get("remarks");
//                }
//                String deductionOrderId = null;
//                String couponName = null;
//                Double money = 0d;
//                if ("1".equals(chargeType)||"2".equals(chargeType)||"3".equals(chargeType)){
//                    if ("5".equals(paymentMethod)||"6".equals(paymentMethod)){
//                        deductionOrderId = map.get("deductionOrderId");
//                        couponName = map.get("couponName");
//                        money = Double.parseDouble(map.get("money"));
//                    }
//                }
//
//                List<HouseAndProprietorDTO> houseAndProprietorDTOS = iFrontOfficeCashierDao.findHouseByRoomCode(roomCode,organizationId);
//                if (houseAndProprietorDTOS.size()==0){
//                    throw new CustomerException();
//                }
//
//                BillDO billDO1 = billDAO.findByOrderIdAndOrganizationId(orderId,organizationId);
//                if (null != billDO1){
//                    LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                    logOldBillsDO.setCode(importCode);
//                    logOldBillsDO.setOrderId(orderId);
//                    logOldBillsDO.setResult("导入失败");
//                    logOldBillsDO.setRoomCode(roomCode);
//                    logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：该账单已存在！");
//                    logOldBillsDO.setOrganizationId(organizationId);
//                    logOldBillsDOS.add(logOldBillsDO);
//                    continue flag;
//                }
//
//                BillDO billDO = new BillDO();
//                billDO.setVillageCode(houseAndProprietorDTOS.get(0).getVillageCode());
//                billDO.setVillageName(houseAndProprietorDTOS.get(0).getVillageName());
//                billDO.setRegionCode(houseAndProprietorDTOS.get(0).getRegionCode());
//                billDO.setRegionName(houseAndProprietorDTOS.get(0).getRegionName());
//                billDO.setBuildingCode(houseAndProprietorDTOS.get(0).getBuildingCode());
//                billDO.setBuildingName(houseAndProprietorDTOS.get(0).getBuildingName());
//                billDO.setUnitCode(houseAndProprietorDTOS.get(0).getUnitCode());
//                billDO.setUnitName(houseAndProprietorDTOS.get(0).getUnitName());
//                billDO.setRoomCode(houseAndProprietorDTOS.get(0).getRoomCode());
//                billDO.setRoomSize(houseAndProprietorDTOS.get(0).getRoomSize());
//                billDO.setCustomerUserId(houseAndProprietorDTOS.get(0).getCustomerUserId());
//                billDO.setSurname(houseAndProprietorDTOS.get(0).getSurname());
//                billDO.setIdNumber(houseAndProprietorDTOS.get(0).getIdNumber());
//                billDO.setMobilePhone(houseAndProprietorDTOS.get(0).getMobilePhone());
//                billDO.setRealGenerationTime(realGenerationTime);
//                billDO.setCorrectedAmount(correctedAmount);
//                billDO.setAmountTotalReceivable(amountReceivable);
//                billDO.setActualTotalMoneyCollection(actualMoneyCollection);
//                billDO.setOrderId(orderId);
//                billDO.setPayerName(payerName);
//                billDO.setPayerPhone(payerPhone);
//                billDO.setPaymentMethod(paymentMethod);
//                billDO.setOrganizationId(organizationId);
//                billDO.setOrganizationName(organizationDO.getOrganizationName());
//                billDO.setInvalidState(0);
////                billDO.setStateOfArrears(0);
//                billDO.setRemark(remarks);
//                billDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                billDO.setRefundStatus(0);
//                billDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//
//                ChargeItemDO chargeItemDO = chargeItemDAO.findByOrganizationIdAndChargeTypeAndEnable(organizationId,Integer.parseInt(chargeType),1);
//
//                BillDetailedDO billDetailedDO = new BillDetailedDO();
//
//                billDetailedDO.setOrderId(orderId);
//                billDetailedDO.setOrganizationId(organizationId);
//                billDetailedDO.setActualMoneyCollection(actualMoneyCollection);
//                billDetailedDO.setAmountReceivable(amountReceivable);
//                billDetailedDO.setPayerUserId(houseAndProprietorDTOS.get(0).getCustomerUserId());
//                billDetailedDO.setPayerName(payerName);
//                billDetailedDO.setPayerPhone(payerPhone);
//                billDetailedDO.setSplitState(0);
//                billDetailedDO.setStateOfArrears(stateOfArrears);
//                billDetailedDO.setDiscount(discount);
//                billDetailedDO.setChargeCode(chargeItemDO.getChargeCode());
//                billDetailedDO.setChargeName(chargeItemDO.getChargeName());
//                billDetailedDO.setChargeUnit(chargeItemDO.getChargeUnit());
//                billDetailedDO.setChargeType(chargeType);
//
//                //判断装修管理费
//                billDetailedDO.setChargeStandard(chargeStandard);//标准单价
//
//                if (1==stateOfArrears){
//                    billDetailedDO.setArrearsType("18");
//                }else{
//                    billDetailedDO.setArrearsType(chargeType);
//                }
//                billDetailedDO.setCode(Integer.parseInt(Tools.random(10000,99999)));
//                billDetailedDO.setParentCode(0);
//
//                if ("1".equals(chargeType)||"2".equals(chargeType)||"3".equals(chargeType)){
//                    redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,"NAN");
//
//                    billDetailedDO.setStartTime(startTime);
//                    billDetailedDO.setDueTime(DateUtil.getAfterDay(dueTime,1));
//                    Integer month = DateUtil.getMonthDiff(startTime,dueTime);
//                    billDetailedDO.setDatedif(month);
//                    if ("5".equals(paymentMethod)){
//                        billDetailedDO.setDeductibleMoney(money);
//                        billDetailedDO.setDeductibledMoney(deductibledMoney);
//                        billDetailedDO.setAmountDeductedThisTime(deductibledMoney);
//                        billDetailedDO.setSurplusDeductibleMoney(money-deductibledMoney);
//                        billDetailedDO.setDeductionRecord(deductibledMoney.toString());
//
//                        //涉及物业费和装修保证金使用
//                        RefundHistoryDO refundHistoryDO = new RefundHistoryDO();
//                        RefundApplicationDO refundApplicationDO = new RefundApplicationDO();
//
//                        refundHistoryDO.setOrganizationId(organizationId);
//                        refundHistoryDO.setOrganizationName(organizationDO.getOrganizationName());
//                        refundHistoryDO.setOrderId(deductionOrderId);
//                        refundHistoryDO.setActualMoneyCollection(billDetailedDO.getAmountReceivable());
//                        refundHistoryDO.setBuildingCode(houseAndProprietorDTOS.get(0).getBuildingCode());
//                        refundHistoryDO.setBuildingName(houseAndProprietorDTOS.get(0).getBuildingName());
//                        refundHistoryDO.setChargeCode(billDetailedDO.getChargeCode());
//                        refundHistoryDO.setChargeName(couponName);
//                        refundHistoryDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                        refundHistoryDO.setDelayTime(0);
////                    refundHistoryDO.setDueTime(dueTime);
//                        refundHistoryDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        refundHistoryDO.setInvalidState(0);
//                        refundHistoryDO.setMobilePhone(houseAndProprietorDTOS.get(0).getMobilePhone());
//                        refundHistoryDO.setIdNumber(houseAndProprietorDTOS.get(0).getIdNumber());
//                        refundHistoryDO.setMortgageAmount(0d);
//                        refundHistoryDO.setPayerName(payerName);
//                        refundHistoryDO.setPayerPhone(payerPhone);
//                        refundHistoryDO.setPaymentMethod(paymentMethod);
//                        refundHistoryDO.setIdNumber(houseAndProprietorDTOS.get(0).getIdNumber());
//
//                        //暂时退款项目只有保证金，先定死
//                        refundHistoryDO.setChargeType("4");
////                    refundHistoryDO.setPersonLiable(personLiable);//责任人
////                    refundHistoryDO.setPersonLiablePhone(personLiablePhone);//责任人电话
//                        refundHistoryDO.setRefundableAmount(billDetailedDO.getAmountReceivable());
//                        if (money-deductibledMoney-transferCardAmount>0){
//                            refundHistoryDO.setRefundStatus(2);
//                        }else {
//                            refundHistoryDO.setRefundStatus(1);
//                        }
//                        refundHistoryDO.setRegionCode(houseAndProprietorDTOS.get(0).getRegionCode());
//                        refundHistoryDO.setRegionName(houseAndProprietorDTOS.get(0).getRegionName());
////                        refundHistoryDO.setRemark("");
////                    refundHistoryDO.setResponsibleAgencies(responsibleAgencies);//负责机构
//                        refundHistoryDO.setRoomCode(roomCode);
//                        refundHistoryDO.setRoomSize(houseAndProprietorDTOS.get(0).getRoomSize());
//                        RoomDO roomDO = roomDAO.findByRoomCode(roomCode);
//                        if (roomDO.getRenovationStatus()==1){
//                            refundHistoryDO.setStartTime(roomDO.getRenovationStartTime());//装修开始时间
//                            refundHistoryDO.setDueTime(roomDO.getRenovationDeadline());
//                        }
//                        refundHistoryDO.setSurname(houseAndProprietorDTOS.get(0).getSurname());
////                        refundHistoryDO.setTollCollectorId(tollCollectorId);
//                        refundHistoryDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        refundHistoryDO.setUnitCode(houseAndProprietorDTOS.get(0).getUnitCode());
//                        refundHistoryDO.setUnitName(houseAndProprietorDTOS.get(0).getUnitName());
//                        refundHistoryDO.setCustomerUserId(houseAndProprietorDTOS.get(0).getCustomerUserId());
//                        refundHistoryDO.setVillageCode(houseAndProprietorDTOS.get(0).getVillageCode());
//                        refundHistoryDO.setVillageName(houseAndProprietorDTOS.get(0).getVillageName());
//                        refundDAO.saveAndFlush(refundHistoryDO);
//
//
////                        refundApplicationDO.setMortgageAmount(refundApplicationDO.getActualMoneyCollection()-refundApplicationDO.getRefundableAmount());
//                        refundApplicationDO.setMortgageAmount(money);
//                        refundApplicationDO.setActualMoneyCollection(money);
//                        refundApplicationDO.setOrganizationId(organizationId);
//                        refundApplicationDO.setOrderId(deductionOrderId);
//                        refundApplicationDO.setTransferCardAmount(transferCardAmount);
//                        refundApplicationDO.setDeductionPropertyFee(deductibledMoney);
//                        refundApplicationDO.setAuditStatus(3);
//                        refundApplicationDO.setSurplusDeductibleMoney(money-deductibledMoney-transferCardAmount);
//                        refundApplicationDO.setDeductibledMoney(deductibledMoney);
//                        refundApplicationDO.setDeductibleMoney(money-transferCardAmount);
//                        refundApplicationDO.setAmountDeductedThisTime(deductibledMoney);
//                        refundApplicationDO.setDeductionRecord(deductibledMoney.toString());
//                        refundApplicationDO.setRefundableAmount(money);
//                        refundApplicationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        refundApplicationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        refundApplicationDAO.saveAndFlush(refundApplicationDO);
//
//                        //涉及物业费抵扣使用
//                        CostDeductionDO costDeductionDO = new CostDeductionDO();
//                        costDeductionDO.setOrganizationId(organizationId);
//                        costDeductionDO.setDeductionItem("装修保证金");
//                        costDeductionDO.setChargeType("4");
//                        costDeductionDO.setDeductionCode(DeductionEnum.REFUND.toString());
//                        costDeductionDO.setDeductionStatus(1);
//                        costDeductionDO.setOriginalDeductionStatus(1);
//
//                        costDeductionDO.setOrderId(orderId);
//                        costDeductionDO.setDeductibleMoney(money-transferCardAmount);
//                        costDeductionDO.setDeductibledMoney(deductibledMoney);
//                        costDeductionDO.setAmountDeductedThisTime(deductibledMoney);
//                        costDeductionDO.setSurplusDeductibleMoney(money-deductibledMoney-transferCardAmount);
//                        costDeductionDO.setDeductionRecord(deductibledMoney.toString());
//                        costDeductionDAO.saveAndFlush(costDeductionDO);
//                    }
//                    if ("6".equals(paymentMethod)){
//                        billDetailedDO.setDeductibleMoney(money);
//                        billDetailedDO.setDeductibledMoney(deductibledMoney);
//                        billDetailedDO.setAmountDeductedThisTime(deductibledMoney);
//                        billDetailedDO.setSurplusDeductibleMoney(money-deductibledMoney);
//                        billDetailedDO.setDeductionRecord(deductibledMoney.toString());
//
//                        List<CouponDO> couponDOS = couponDAO.findByOrganizationId(organizationId);
//                        List<CouponDO> couponDOList = new ArrayList<>();
//                        CouponDO couponDO = new CouponDO();
//                        for (CouponDO couponDO1:couponDOS) {
//                            if (Double.doubleToLongBits(money)==Double.doubleToLongBits(couponDO1.getMoney())){
//                                couponDOList.add(couponDO1);
//                            }
//                        }
//                        if (couponDOList.size()==0){
//                            //新添日志
//                            LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                            logOldBillsDO.setCode(importCode);
//                            logOldBillsDO.setOrderId(orderId);
//                            logOldBillsDO.setResult("导入失败");
//                            logOldBillsDO.setRoomCode(roomCode);
//                            logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                            logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：优惠券抵扣信息导入失败！");
//                            logOldBillsDO.setOrganizationId(organizationId);
//                            logOldBillsDOS.add(logOldBillsDO);
//                            continue flag;
//                        }else {
//                            couponDO = couponDOList.get(0);
//                        }
//                        //物业费涉及优惠券使用
//                        RoomAndCouponDO roomAndCouponDO = new RoomAndCouponDO();
//                        roomAndCouponDO.setChargeCode(couponDO.getChargeCode());
//                        roomAndCouponDO.setCouponCode(deductionOrderId);
//                        roomAndCouponDO.setCouponType(couponDO.getCouponType());
//                        roomAndCouponDO.setCouponName(couponName);
//                        roomAndCouponDO.setEffectiveTime(couponDO.getEffectiveTime());
//                        roomAndCouponDO.setOrganizationId(organizationId);
//                        roomAndCouponDO.setAuditStatus(3);
//                        roomAndCouponDO.setOrganizationName(organizationDO.getOrganizationName());
//                        roomAndCouponDO.setMoney(money);
//                        roomAndCouponDO.setDeductibleMoney(money);
//                        roomAndCouponDO.setDeductibledMoney(deductibledMoney);
//                        roomAndCouponDO.setAmountDeductedThisTime(deductibledMoney);
//                        roomAndCouponDO.setSurplusDeductibleMoney(money-deductibledMoney);
//                        roomAndCouponDO.setDeductionRecord(deductibledMoney.toString());
//                        roomAndCouponDO.setPropertyFee(deductibledMoney);
//                        roomAndCouponDO.setPastDue(0);
//                        roomAndCouponDO.setVillageCode(houseAndProprietorDTOS.get(0).getVillageCode());
//                        roomAndCouponDO.setVillageName(houseAndProprietorDTOS.get(0).getVillageName());
//                        roomAndCouponDO.setRegionCode(houseAndProprietorDTOS.get(0).getRegionCode());
//                        roomAndCouponDO.setRegionName(houseAndProprietorDTOS.get(0).getRegionName());
//                        roomAndCouponDO.setBuildingCode(houseAndProprietorDTOS.get(0).getBuildingCode());
//                        roomAndCouponDO.setBuildingName(houseAndProprietorDTOS.get(0).getBuildingName());
//                        roomAndCouponDO.setUnitCode(houseAndProprietorDTOS.get(0).getUnitCode());
//                        roomAndCouponDO.setUnitName(houseAndProprietorDTOS.get(0).getUnitName());
//                        roomAndCouponDO.setRoomCode(houseAndProprietorDTOS.get(0).getRoomCode());
//                        roomAndCouponDO.setCustomerUserId(houseAndProprietorDTOS.get(0).getCustomerUserId());
//                        roomAndCouponDO.setSurname(houseAndProprietorDTOS.get(0).getSurname());
//                        roomAndCouponDO.setIdNumber(houseAndProprietorDTOS.get(0).getIdNumber());
//                        roomAndCouponDO.setMobilePhone(houseAndProprietorDTOS.get(0).getMobilePhone());
//                        roomAndCouponDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        if (money-deductibledMoney>0){
//                            roomAndCouponDO.setUsageState(2);
//                        }else {
//                            roomAndCouponDO.setUsageState(1);
//                        }
//                        roomAndCouponDAO.saveAndFlush(roomAndCouponDO);
//
////                        roomAndCouponDO.setUsageState();
//
//                        //涉及物业费抵扣使用
//                        CostDeductionDO costDeductionDO = new CostDeductionDO();
//                        costDeductionDO.setOrganizationId(organizationId);
//                        costDeductionDO.setDeductionItem(couponName);
//                        costDeductionDO.setChargeType(chargeType);
//                        costDeductionDO.setDeductionCode(DeductionEnum.COUPON.toString());
//                        costDeductionDO.setDeductionStatus(1);
//                        costDeductionDO.setOriginalDeductionStatus(1);
//
//                        costDeductionDO.setOrderId(orderId);
//                        costDeductionDO.setDeductibleMoney(money);
//                        costDeductionDO.setDeductibledMoney(deductibledMoney);
//                        costDeductionDO.setAmountDeductedThisTime(deductibledMoney);
//                        costDeductionDO.setSurplusDeductibleMoney(money-deductibledMoney);
//                        costDeductionDO.setDeductionRecord(deductibledMoney.toString());
//                        costDeductionDAO.saveAndFlush(costDeductionDO);
//                    }
//                }
//                if ("7".equals(chargeType)||"17".equals(chargeType)){
//                    billDetailedDO.setDeductibleMoney(0d);
//                    billDetailedDO.setDeductibledMoney(0d);
//                    billDetailedDO.setAmountDeductedThisTime(0d);
//                    billDetailedDO.setSurplusDeductibleMoney(0d);
//                    billDetailedDO.setDeductionRecord("0");
//
//                    billDetailedDO.setLastReading(lastReading);
//                    billDetailedDO.setCurrentReadings(currentReadings);
//                    billDetailedDO.setUsageAmount(currentReadings-lastReading);
//                }
//
//                if ("14".equals(chargeType)||"15".equals(chargeType)||"16".equals(chargeType)){
//                    billDetailedDO.setDeductibleMoney(chargeStandard);
//                    billDetailedDO.setDeductibledMoney(deductibledMoney);
//                    billDetailedDO.setAmountDeductedThisTime(deductibledMoney);
//                    billDetailedDO.setSurplusDeductibleMoney(chargeStandard-deductibledMoney);
//                    if (0d==deductibledMoney){
//                        billDetailedDO.setDeductionRecord("0");
//                    }else {
//                        billDetailedDO.setDeductionRecord(deductibledMoney.toString());
//                    }
//                }
//                if ("4".equals(chargeType)){
//                    billDetailedDO.setDeductibleMoney(0d);
//                    billDetailedDO.setDeductibledMoney(0d);
//                    billDetailedDO.setAmountDeductedThisTime(0d);
//                    billDetailedDO.setSurplusDeductibleMoney(0d);
//                    billDetailedDO.setDeductionRecord("0");
//
//                    //涉及物业费和装修保证金使用
//                    RefundHistoryDO refundHistoryDO = new RefundHistoryDO();
//                    RefundApplicationDO refundApplicationDO = new RefundApplicationDO();
//
//                    refundHistoryDO.setOrganizationId(organizationId);
//                    refundHistoryDO.setOrganizationName(organizationDO.getOrganizationName());
//                    refundHistoryDO.setOrderId(orderId);
//                    refundHistoryDO.setActualMoneyCollection(billDetailedDO.getActualMoneyCollection());
//                    refundHistoryDO.setBuildingCode(houseAndProprietorDTOS.get(0).getBuildingCode());
//                    refundHistoryDO.setBuildingName(houseAndProprietorDTOS.get(0).getBuildingName());
//                    refundHistoryDO.setChargeCode(billDetailedDO.getChargeCode());
//                    refundHistoryDO.setChargeName(billDetailedDO.getChargeName());
//                    refundHistoryDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                    refundHistoryDO.setDelayTime(0);
////                    refundHistoryDO.setDueTime(dueTime);
//                    refundHistoryDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    refundHistoryDO.setInvalidState(0);
//                    refundHistoryDO.setMobilePhone(houseAndProprietorDTOS.get(0).getMobilePhone());
//                    refundHistoryDO.setIdNumber(houseAndProprietorDTOS.get(0).getIdNumber());
//                    refundHistoryDO.setMortgageAmount(0d);
//                    refundHistoryDO.setPayerName(payerName);
//                    refundHistoryDO.setPayerPhone(payerPhone);
//                    refundHistoryDO.setPaymentMethod(paymentMethod);
//                    refundHistoryDO.setChargeType(billDetailedDO.getChargeType());
//                    refundHistoryDO.setIdNumber(houseAndProprietorDTOS.get(0).getIdNumber());
////                    refundHistoryDO.setPersonLiable(personLiable);//责任人
////                    refundHistoryDO.setPersonLiablePhone(personLiablePhone);//责任人电话
//                    refundHistoryDO.setRefundableAmount(billDetailedDO.getActualMoneyCollection());
//                    if (billDetailedDO.getActualMoneyCollection()-deductibledMoney-transferCardAmount>0){
//                        refundHistoryDO.setRefundStatus(2);
//                    }else {
//                        refundHistoryDO.setRefundStatus(1);
//                    }
//                    refundHistoryDO.setRegionCode(houseAndProprietorDTOS.get(0).getRegionCode());
//                    refundHistoryDO.setRegionName(houseAndProprietorDTOS.get(0).getRegionName());
////                        refundHistoryDO.setRemark("");
////                    refundHistoryDO.setResponsibleAgencies(responsibleAgencies);//负责机构
//                    refundHistoryDO.setRoomCode(roomCode);
//                    refundHistoryDO.setRoomSize(houseAndProprietorDTOS.get(0).getRoomSize());
//                    RoomDO roomDO = roomDAO.findByRoomCode(roomCode);
//                    if (roomDO.getRenovationStatus()==1){
//                        refundHistoryDO.setStartTime(roomDO.getRenovationStartTime());//装修开始时间
//                        refundHistoryDO.setDueTime(roomDO.getRenovationDeadline());
//                    }
//                    refundHistoryDO.setSurname(houseAndProprietorDTOS.get(0).getSurname());
////                        refundHistoryDO.setTollCollectorId(tollCollectorId);
//                    refundHistoryDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    refundHistoryDO.setUnitCode(houseAndProprietorDTOS.get(0).getUnitCode());
//                    refundHistoryDO.setUnitName(houseAndProprietorDTOS.get(0).getUnitName());
//                    refundHistoryDO.setCustomerUserId(houseAndProprietorDTOS.get(0).getCustomerUserId());
//                    refundHistoryDO.setVillageCode(houseAndProprietorDTOS.get(0).getVillageCode());
//                    refundHistoryDO.setVillageName(houseAndProprietorDTOS.get(0).getVillageName());
//                    refundDAO.saveAndFlush(refundHistoryDO);
//
//
////                        refundApplicationDO.setMortgageAmount(refundApplicationDO.getActualMoneyCollection()-refundApplicationDO.getRefundableAmount());
//                    refundApplicationDO.setMortgageAmount(refundHistoryDO.getMortgageAmount());
//                    refundApplicationDO.setActualMoneyCollection(billDetailedDO.getActualMoneyCollection());
//                    refundApplicationDO.setOrganizationId(organizationId);
//                    refundApplicationDO.setOrderId(orderId);
//                    refundApplicationDO.setTransferCardAmount(transferCardAmount);
//                    refundApplicationDO.setDeductionPropertyFee(deductibledMoney);
//                    refundApplicationDO.setAuditStatus(3);
//                    refundApplicationDO.setSurplusDeductibleMoney(refundHistoryDO.getActualMoneyCollection()-deductibledMoney-transferCardAmount);
//                    refundApplicationDO.setDeductibledMoney(deductibledMoney);
//                    refundApplicationDO.setDeductibleMoney(refundHistoryDO.getActualMoneyCollection()-transferCardAmount);
//                    refundApplicationDO.setAmountDeductedThisTime(deductibledMoney);
//                    refundApplicationDO.setDeductionRecord(deductibledMoney.toString());
//                    refundApplicationDO.setRefundableAmount(refundHistoryDO.getRefundableAmount());
//                    refundApplicationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    refundApplicationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    refundApplicationDAO.saveAndFlush(refundApplicationDO);
//                }
//
//                if ("5".equals(chargeType)||"6".equals(chargeType)){
////                    billDetailedDO.setParkingSpacePlace(par);
//
//                    billDetailedDO.setStartTime(startTime);
//                    billDetailedDO.setDueTime(DateUtil.getAfterDay(dueTime,1));
//                    Integer month = DateUtil.getMonthDiff(startTime,dueTime);
//                    billDetailedDO.setDatedif(month);
//
//                    billDetailedDO.setDeductibleMoney(0d);
//                    billDetailedDO.setDeductibledMoney(0d);
//                    billDetailedDO.setAmountDeductedThisTime(0d);
//                    billDetailedDO.setSurplusDeductibleMoney(0d);
//                    billDetailedDO.setDeductionRecord("0");
//
//                    //涉及车位费使用
//                    ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = new ParkingSpaceCostDetailDO();
//                    parkingSpaceCostDetailDO.setOrderId(orderId);
//                    parkingSpaceCostDetailDO.setStartTime(startTime);
//                    parkingSpaceCostDetailDO.setDueTime(dueTime);
//
//
////                    billDAO.saveAndFlush(billDO);
//                    LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                    logOldBillsDO.setCode(importCode);
//                    logOldBillsDO.setOrderId(orderId);
//                    logOldBillsDO.setResult("暂不支持车位相关的导入");
//                    logOldBillsDO.setRoomCode(roomCode);
//                    logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    logOldBillsDO.setOrganizationId(organizationId);
//                    logOldBillsDOS.add(logOldBillsDO);
//                    continue flag;
//                }
//                billDetailedDAO.saveAndFlush(billDetailedDO);
//                billDAO.saveAndFlush(billDO);
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入成功");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//                count++;
//            } catch (NumberFormatException e){
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：读取Excel数据为空！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            }catch (IndexOutOfBoundsException e){
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：房间号或手机号填写错误！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            } catch (NullPointerException e){
//                logger.error("【旧账单导入服务类】导入旧账单异常，ERROR：{}", e);
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：必填字段不能为空！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            }catch(ParseException e){
//                logger.error("【旧账单导入服务类】导入旧账单异常，ERROR：{}", e);
//                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
//                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
//                logOldBillsDO.setResult("导入失败");
//                logOldBillsDO.setRoomCode(roomCode);
//                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOldBillsDO.setRemarks("第" + (i + 2)+"行，订单号为："+orderId+",导入失败！原因：时间计算错误！");
//                logOldBillsDO.setOrganizationId(organizationId);
//                logOldBillsDOS.add(logOldBillsDO);
//            }
//            logOldBillsDAO.saveAll(logOldBillsDOS);
//        }
//        Map<String,Object> map = new HashMap<>();
//        map.put("totalNumber",list.size()); //导入数据条数
//        map.put("realNumber",count); //导入成功条数
//        map.put("logOldBillsDOS",logOldBillsDOS);
//        return map;
        return null;
    }

    @Override
    public Map<String, Object> expenseDeductionAndReSum(JSONObject jsonObject) {
        List<BillDetailedDO> billDetailedDOArrayList = JSONObject.parseArray(jsonObject.getJSONArray("billDetailedDOArrayList").toJSONString(),BillDetailedDO.class);
        List<CostDeductionDO> costDeduction = JSONObject.parseArray(jsonObject.getJSONArray("costDeduction").toJSONString(),CostDeductionDO.class);
        List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOList = JSONObject.parseArray(jsonObject.getJSONArray("parkingSpaceCostDetailDOList").toJSONString(),ParkingSpaceCostDetailDO.class);
//        Double actualTotalMoneyCollection = jsonObject.getDouble("actualTotalMoneyCollection");
        Double amountTotalReceivable = 0d;
        Double actualTotalMoneyCollection = 0d;
        for (BillDetailedDO billDetailedDO:billDetailedDOArrayList) {
//            if (billDetailedDO.getStateOfArrears()!=billDetailedDO.getOriginalStateOfArrears()){
//                if (0==billDetailedDO.getOriginalStateOfArrears()){
//                    actualTotalMoneyCollection = actualTotalMoneyCollection - billDetailedDO.getActualMoneyCollection();
//                    billDetailedDO.setOriginalStateOfArrears(billDetailedDO.getStateOfArrears());
//                }else if (1==billDetailedDO.getOriginalStateOfArrears()){
//                    actualTotalMoneyCollection = actualTotalMoneyCollection + billDetailedDO.getActualMoneyCollection();
//                    billDetailedDO.setOriginalStateOfArrears(billDetailedDO.getStateOfArrears());
//                }
//            }else {
//                if (0==billDetailedDO.getOriginalStateOfArrears()){
//                    amountTotalReceivable = amountTotalReceivable+billDetailedDO.getAmountReceivable();
//                }
//            }
            if (0==billDetailedDO.getStateOfArrears()&&1!=billDetailedDO.getSplitState()){
                if (null==billDetailedDO.getAmountReceivable()&&null==billDetailedDO.getActualMoneyCollection()){
                    throw new NullPointerException();
                }
                amountTotalReceivable = amountTotalReceivable+billDetailedDO.getAmountReceivable();
                actualTotalMoneyCollection = actualTotalMoneyCollection+billDetailedDO.getActualMoneyCollection();
            }

        }
        for (CostDeductionDO costDeductionDO:costDeduction) {
//            if (costDeductionDO.getDeductionStatus()!=costDeductionDO.getOriginalDeductionStatus()){
//                if (0==costDeductionDO.getOriginalDeductionStatus()){
//                    actualTotalMoneyCollection = actualTotalMoneyCollection - costDeductionDO.getAmountDeductedThisTime();
//                    costDeductionDO.setOriginalDeductionStatus(costDeductionDO.getDeductionStatus());
//                }else if (1==costDeductionDO.getOriginalDeductionStatus()){
//                    actualTotalMoneyCollection = actualTotalMoneyCollection + costDeductionDO.getAmountDeductedThisTime();
//                    costDeductionDO.setOriginalDeductionStatus(costDeductionDO.getDeductionStatus());
//                }
//            }
            if (1==costDeductionDO.getDeductionStatus()){
                actualTotalMoneyCollection = actualTotalMoneyCollection-costDeductionDO.getAmountDeductedThisTime();
            }
        }
        for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOList){
            amountTotalReceivable = amountTotalReceivable+parkingSpaceCostDetailDO.getAmountReceivable();
            actualTotalMoneyCollection = actualTotalMoneyCollection+parkingSpaceCostDetailDO.getActualMoneyCollection();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("billDetailedDOArrayList",billDetailedDOArrayList);
        map.put("costDeduction",costDeduction);
        map.put("parkingSpaceCostDetailDOList",parkingSpaceCostDetailDOList);
        map.put("amountTotalReceivable",Tools.moneyHalfAdjust(amountTotalReceivable));
        map.put("actualTotalMoneyCollection",Tools.moneyHalfAdjust(actualTotalMoneyCollection));
        return map;
    }
}
