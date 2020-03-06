package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.service.CustomerService;
import com.rbi.interactive.service.HistoryDataService;
import com.rbi.interactive.service.PropertyFeeDueTimeService;
import com.rbi.interactive.service.impl.charge.PropertyFeeCostImpl;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.service.impl.strategy.ICostStrategyImpl;
import com.rbi.interactive.utils.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.*;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {

    private final static Logger logger = LoggerFactory.getLogger(HistoryDataServiceImpl.class);

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Autowired
    PropertyFeeDueTimeService propertyFeeDueTimeService;

    @Autowired
    HistoryDataDAO historyDataDAO;

    @Autowired
    HistoryDataPropertyDueTimeDAO historyDataPropertyDueTimeDAO;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CustomerService customerService;

    @Autowired(required = false)
    IRoomAndCustomerDAO iRoomAndCustomerDAO;

    @Autowired
    LogOldBillsDAO logOldBillsDAO;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired
    ParkingSpaceManagementDAO parkingSpaceManagementDAO;

    @Autowired
    ExclusiveParkingSpaceHistoryDataDAO exclusiveParkingSpaceHistoryDataDAO;

    @Autowired
    ExclusiveParkingSpaceHistoryDataDueTimeDAO exclusiveParkingSpaceHistoryDataDueTimeDAO;

    @Autowired
    ParkingSpaceInfoDAO parkingSpaceInfoDAO;

    @Override
    public Map<String, Object> importHistoryData(MultipartFile multipartFile, String userId) {
        String currentTime = DateUtil.date(DateUtil.FORMAT_PATTERN);

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String filePath = "D:\\newExcel.xlsx";
        String columns[] = {"roomCode","deliveryTime","startTime","roomSize","paidInPropertyFee",
                "decorationManagementFee","garbageCollectionAndTransportationFee","decorationDeposit","refundedDecorationDeposit",
                "deductedDecorationDeposit","cashCoupon","oldPropertyBringsNew","deductionThreeWayFees","reductionIncome","remarks"};
        wb = ExcelPOI.readExcel(multipartFile);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        logger.info("读取旧账单数据成功！数据量：{}",list.size());
        Random random = new Random();
        int ends = random.nextInt(99);
        String importCode = DateUtil.timeStamp().toString()+ends;
        List<LogOldBillsDO> logOldBillsDOS = new ArrayList<>();
        int count = 0;
        for (int i = 0;i<list.size();i++) {

            Map<String, String> map = new HashMap<>();
            map = list.get(i);

            if (StringUtils.isBlank(map.get("roomCode"))||StringUtils.isBlank(map.get("deliveryTime"))||StringUtils.isBlank(map.get("startTime"))||StringUtils.isBlank(map.get("roomSize"))){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：必填字段不能为空！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
            String roomCode = map.get("roomCode");

            List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findAllByRoomCodeAndLoggedOffStateAndOrganizationId(roomCode,0,organizationId);
            if (roomAndCustomerDOS.size()==0){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：房间基础信息不存在！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String deliveryTime = map.get("deliveryTime");
            String startTime = map.get("startTime");
            String year = null;
            String month = null;
            String day = null;
            String status = "1";

            Double roomSize = Double.parseDouble(map.get("roomSize"));
            double paidInPropertyFee = 0d;

            if (StringUtils.isNotBlank(map.get("paidInPropertyFee"))){
                paidInPropertyFee = Double.parseDouble(map.get("paidInPropertyFee"));
            }
            Double decorationManagementFee = 0d;
            if (StringUtils.isNotBlank(map.get("decorationManagementFee"))){
                decorationManagementFee = Double.parseDouble(map.get("decorationManagementFee"));
            }
            Double garbageCollectionAndTransportationFee = 0d;
            if (StringUtils.isNotBlank(map.get("garbageCollectionAndTransportationFee"))){
                garbageCollectionAndTransportationFee = Double.parseDouble(map.get("garbageCollectionAndTransportationFee"));
            }
            double decorationDeposit = 0d;
            if (StringUtils.isNotBlank(map.get("decorationDeposit"))){
                decorationDeposit = Double.parseDouble(map.get("decorationDeposit"));
            }
            Double refundedDecorationDeposit = 0d;
            if (StringUtils.isNotBlank(map.get("refundedDecorationDeposit"))){
                refundedDecorationDeposit = Double.parseDouble(map.get("refundedDecorationDeposit"));
            }
            double deductedDecorationDeposit = 0d;
            if (StringUtils.isNotBlank(map.get("deductedDecorationDeposit"))){
                deductedDecorationDeposit = Double.parseDouble(map.get("deductedDecorationDeposit"));
            }
            double cashCoupon = 0d;
            if (StringUtils.isNotBlank(map.get("cashCoupon"))){
                cashCoupon = Double.parseDouble(map.get("cashCoupon"));
            }
            double oldPropertyBringsNew = 0d;
            if (StringUtils.isNotBlank(map.get("oldPropertyBringsNew"))){
                oldPropertyBringsNew = Double.parseDouble(map.get("oldPropertyBringsNew"));
            }
//            Double recoveryPreviousDebts = 0d;
//            if (StringUtils.isNotBlank(map.get("recoveryPreviousDebts"))){
//                recoveryPreviousDebts = Double.parseDouble(map.get("recoveryPreviousDebts"));
//            }
//            Double currentIncomeFromAdvanceReceipts = 0d;
//            if (StringUtils.isNotBlank(map.get("currentIncomeFromAdvanceReceipts"))){
//                currentIncomeFromAdvanceReceipts = Double.parseDouble(map.get("currentIncomeFromAdvanceReceipts"));
//            }
            double deductionThreeWayFees = 0d;
            if (StringUtils.isNotBlank(map.get("deductionThreeWayFees"))){
                deductionThreeWayFees = Double.parseDouble(map.get("deductionThreeWayFees"));
            }
            double reductionIncome = 0d;
            if (StringUtils.isNotBlank(map.get("reductionIncome"))){
                reductionIncome = Double.parseDouble(map.get("reductionIncome"));
            }
//            Double amountOfPropertyFee;
            String remarks = "";
            if (StringUtils.isNotBlank(map.get("remarks"))){
                remarks = map.get("remarks");
            }

            HistoryDataDO historyDataDO = historyDataDAO.findByOrganizationIdAndRoomCodeAndStatus(organizationId,roomCode,status);
            if (null==historyDataDO){
                historyDataDO = new HistoryDataDO();
            }
            double amountOfPropertyFee = paidInPropertyFee+decorationDeposit+cashCoupon+oldPropertyBringsNew+deductionThreeWayFees+reductionIncome;
            historyDataDO.setAmountOfPropertyFee(amountOfPropertyFee);
            historyDataDO.setRoomCode(roomCode);
            historyDataDO.setRoomSize(roomSize);
            historyDataDO.setDeliveryTime(deliveryTime);
            historyDataDO.setStartTime(startTime);
            historyDataDO.setStatus(status);
            historyDataDO.setPaidInPropertyFee(paidInPropertyFee);
            historyDataDO.setDecorationManagementFee(decorationManagementFee);
            historyDataDO.setGarbageCollectionAndTransportationFee(garbageCollectionAndTransportationFee);
            historyDataDO.setDecorationDeposit(decorationDeposit);
            historyDataDO.setRefundedDecorationDeposit(refundedDecorationDeposit);
            historyDataDO.setDeductedDecorationDeposit(deductedDecorationDeposit);
            historyDataDO.setCashCoupon(cashCoupon);
            historyDataDO.setOldPropertyBringsNew(oldPropertyBringsNew);
//            historyDataDO.setRecoveryPreviousDebts(recoveryPreviousDebts);
//            historyDataDO.setCurrentIncomeFromAdvanceReceipts(currentIncomeFromAdvanceReceipts);
            historyDataDO.setDeductionThreeWayFees(deductionThreeWayFees);
            historyDataDO.setReductionIncome(reductionIncome);
            historyDataDO.setRemarks(remarks);
            historyDataDO.setOrganizationId(organizationId);
            historyDataDO.setIdt(currentTime);

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
//            json.put("customerUserId",customerUserId);
            json.put("startTime",DateUtil.getFirstDayOfMonth(Integer.parseInt(Tools.analysisStr(dueTime,"-",1)),Integer.parseInt(Tools.analysisStr(dueTime,"-",2))));
            BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeItemDO.getChargeCode(),1);
            ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
            ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
            BillDetailedDO billDetailedBackDO1 = null;
            try {
                billDetailedBackDO1 = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
            } catch (ConnectException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：物业费计算失败！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            } catch (ParseException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：物业费计算失败！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
//                    billDetailedBackDO1.setDueTime(DateUtil.getTimeDay(billDetailedBackDO1.getDueTime(),DateUtil.YEAR_MONTH_DAY,-1));
//                    Double originalActualMoneyCollection = billDetailedBackDO1.getActualMoneyCollection();

            //算出一个月的费用
            double monthlyPropertyFee = billDetailedBackDO1.getAmountReceivable();
            /**
             * 计算空置费
             */
            //统计计算月的天数
            String movementTime = deliveryTime;
            int vacantMonthCount = 0;
            int vacantDayCount = 0;
            if (-1==DateUtil.compareDate(deliveryTime,startTime)){
                System.out.println("计算空置费");
                //计算物业费欠费时长
                for (int j = 0;;j++){
                    if (1==DateUtil.compareDate(movementTime,startTime)){
                        break;
                    }
                    try {
                        movementTime = DateUtil.getAfterMonth(movementTime,1);
                    } catch (ParseException e) {
                        LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                        logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                        logOldBillsDO.setResult("导入失败");
                        logOldBillsDO.setRoomCode(map.get("roomCode"));
                        logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间格式填写错误！");
                        logOldBillsDO.setOrganizationId(organizationId);
                        logOldBillsDOS.add(logOldBillsDO);
                        continue;
                    }
                    vacantMonthCount++;
                }
                vacantMonthCount = vacantMonthCount-1;
                try {
                    movementTime = DateUtil.getAfterMonth(movementTime,-1);
                    vacantDayCount = DateUtil.longOfTwoDate(movementTime,startTime);
                } catch (ParseException e) {
                    LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                    logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                    logOldBillsDO.setResult("导入失败");
                    logOldBillsDO.setRoomCode(map.get("roomCode"));
                    logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间格式填写错误！");
                    logOldBillsDO.setOrganizationId(organizationId);
                    logOldBillsDOS.add(logOldBillsDO);
                    continue;
                }
                //计算一天的物业费
                int dayMonth = DateUtil.daysMonth(Integer.parseInt(Tools.analysisStr(startTime,"-",1)),Integer.parseInt(Tools.analysisStr(startTime,"-",2)));
                double dayFee = Tools.moneyHalfAdjust(monthlyPropertyFee/dayMonth);

                //算出空置费
                historyDataDO.setVacancyCharge(Tools.moneyHalfAdjust(vacantMonthCount*monthlyPropertyFee+vacantDayCount*dayFee));
            }else {
                historyDataDO.setVacancyCharge(0d);
            }
            historyDataDAO.saveAndFlush(historyDataDO);

            /**
             * 根据物业费推算到期时间
             * 物业费金额 = 实收物业费+已抵扣装修保证金+代金券+老带新+三通费抵扣+冲减收入
             * amountOfPropertyFee
             */
            int monthCount = (int) Math.floor(amountOfPropertyFee/monthlyPropertyFee);
            double surplus = Tools.moneyHalfAdjust(amountOfPropertyFee - monthlyPropertyFee*monthCount);
            try {
                dueTime = DateUtil.getAfterMonth(startTime,monthCount);
            } catch (ParseException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间格式填写错误！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
            HistoryDataPropertyDueTimeDO historyDataPropertyDueTimeDO = historyDataPropertyDueTimeDAO.findByOrganizationIdAndRoomCode(organizationId,roomCode);
            if (null == historyDataPropertyDueTimeDO){
                historyDataPropertyDueTimeDO = new HistoryDataPropertyDueTimeDO();
            }
            historyDataPropertyDueTimeDO.setOrganizationId(organizationId);
            historyDataPropertyDueTimeDO.setRoomCode(roomCode);
            historyDataPropertyDueTimeDO.setDueTime(dueTime);
            historyDataPropertyDueTimeDO.setIdt(currentTime);

            RoomAndCustomerDO roomAndCustomerDO = customerService.findByRoomCodeAndOrganizationIdAndIdentity(roomCode,organizationId);
            //处理余额
            if (0d<surplus){
                //导入的历史数据    余额都为零   不需要累加
//                Double sqlSurplus = iRoomAndCustomerDAO.findByRoomCodeAndUserIdSurplus(roomCode,roomAndCustomerDO.getCustomerUserId());
                iRoomAndCustomerDAO.updateSurplus(surplus,roomCode,roomAndCustomerDO.getCustomerUserId());
            }
            redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,"NAN");
            historyDataPropertyDueTimeDAO.saveAndFlush(historyDataPropertyDueTimeDO);
            LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
            logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
            logOldBillsDO.setResult("导入成功");
            logOldBillsDO.setRoomCode(map.get("roomCode"));
            logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            logOldBillsDO.setRemarks("");
            logOldBillsDO.setOrganizationId(organizationId);
            logOldBillsDOS.add(logOldBillsDO);
            count++;
        }
        logOldBillsDAO.saveAll(logOldBillsDOS);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",list.size()); //导入数据条数
        map.put("realNumber",count); //导入成功条数
        map.put("logOldBillsDOS",logOldBillsDOS);
        return map;
    }

    @Override
    public Map<String, Object> importHistoryDataCumulative(MultipartFile multipartFile, String userId) {
        String currentTime = DateUtil.date(DateUtil.FORMAT_PATTERN);

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String filePath = "D:\\newExcel.xlsx";
        String columns[] = {"roomCode","deliveryTime","startTime","roomSize","paidInPropertyFee",
                "decorationManagementFee","garbageCollectionAndTransportationFee","decorationDeposit","refundedDecorationDeposit",
                "deductedDecorationDeposit","cashCoupon","oldPropertyBringsNew","deductionThreeWayFees","reductionIncome","remarks"};
        wb = ExcelPOI.readExcel(multipartFile);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        logger.info("读取旧账单数据成功！数据量：{}",list.size());
        Random random = new Random();
        int ends = random.nextInt(99);
        String importCode = DateUtil.timeStamp().toString()+ends;
        List<LogOldBillsDO> logOldBillsDOS = new ArrayList<>();
        int count = 0;
        for (int i = 0;i<list.size();i++) {

            Map<String, String> map = new HashMap<>();
            map = list.get(i);

            if (StringUtils.isBlank(map.get("roomCode")) || StringUtils.isBlank(map.get("deliveryTime")) || StringUtils.isBlank(map.get("startTime")) || StringUtils.isBlank(map.get("roomSize"))) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2) + "行，" + ",导入失败！原因：必填字段不能为空！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
            String roomCode = map.get("roomCode");

            List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findAllByRoomCodeAndLoggedOffStateAndOrganizationId(roomCode,0,organizationId);
            if (roomAndCustomerDOS.size()==0){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：房间基础信息不存在！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String status = "1";
            HistoryDataDO historyDataDO = historyDataDAO.findByOrganizationIdAndRoomCodeAndStatus(organizationId,roomCode,status);
            if (null == historyDataDO){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：未查询到以前导入的数据！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String deliveryTime = map.get("deliveryTime");
            String startTime = map.get("startTime");
            String year = null;
            String month = null;
            String day = null;


            Double roomSize = Double.parseDouble(map.get("roomSize"));
            double paidInPropertyFee = 0d;

            if (StringUtils.isNotBlank(map.get("paidInPropertyFee"))){
                paidInPropertyFee = Double.parseDouble(map.get("paidInPropertyFee"));
            }
            paidInPropertyFee = paidInPropertyFee+historyDataDO.getPaidInPropertyFee();
            Double decorationManagementFee = 0d;
            if (StringUtils.isNotBlank(map.get("decorationManagementFee"))){
                decorationManagementFee = Double.parseDouble(map.get("decorationManagementFee"));
            }
            decorationManagementFee = decorationManagementFee+historyDataDO.getDecorationManagementFee();
            Double garbageCollectionAndTransportationFee = 0d;
            if (StringUtils.isNotBlank(map.get("garbageCollectionAndTransportationFee"))){
                garbageCollectionAndTransportationFee = Double.parseDouble(map.get("garbageCollectionAndTransportationFee"));
            }
            garbageCollectionAndTransportationFee = garbageCollectionAndTransportationFee + historyDataDO.getGarbageCollectionAndTransportationFee();

            double decorationDeposit = 0d;
            if (StringUtils.isNotBlank(map.get("decorationDeposit"))){
                decorationDeposit = Double.parseDouble(map.get("decorationDeposit"));
            }
            decorationDeposit = decorationDeposit+historyDataDO.getDecorationDeposit();

            Double refundedDecorationDeposit = 0d;
            if (StringUtils.isNotBlank(map.get("refundedDecorationDeposit"))){
                refundedDecorationDeposit = Double.parseDouble(map.get("refundedDecorationDeposit"));
            }
            refundedDecorationDeposit = refundedDecorationDeposit+historyDataDO.getRefundedDecorationDeposit();

            double deductedDecorationDeposit = 0d;
            if (StringUtils.isNotBlank(map.get("deductedDecorationDeposit"))){
                deductedDecorationDeposit = Double.parseDouble(map.get("deductedDecorationDeposit"));
            }
            deductedDecorationDeposit = deductedDecorationDeposit+historyDataDO.getDeductedDecorationDeposit();

            double cashCoupon = 0d;
            if (StringUtils.isNotBlank(map.get("cashCoupon"))){
                cashCoupon = Double.parseDouble(map.get("cashCoupon"));
            }
            cashCoupon = cashCoupon + historyDataDO.getCashCoupon();

            double oldPropertyBringsNew = 0d;
            if (StringUtils.isNotBlank(map.get("oldPropertyBringsNew"))){
                oldPropertyBringsNew = Double.parseDouble(map.get("oldPropertyBringsNew"));
            }
            oldPropertyBringsNew = oldPropertyBringsNew + historyDataDO.getOldPropertyBringsNew();

//            Double recoveryPreviousDebts = 0d;
//            if (StringUtils.isNotBlank(map.get("recoveryPreviousDebts"))){
//                recoveryPreviousDebts = Double.parseDouble(map.get("recoveryPreviousDebts"));
//            }
//            Double currentIncomeFromAdvanceReceipts = 0d;
//            if (StringUtils.isNotBlank(map.get("currentIncomeFromAdvanceReceipts"))){
//                currentIncomeFromAdvanceReceipts = Double.parseDouble(map.get("currentIncomeFromAdvanceReceipts"));
//            }
            double deductionThreeWayFees = 0d;
            if (StringUtils.isNotBlank(map.get("deductionThreeWayFees"))){
                deductionThreeWayFees = Double.parseDouble(map.get("deductionThreeWayFees"));
            }
            deductionThreeWayFees = deductionThreeWayFees + historyDataDO.getDeductionThreeWayFees();

            double reductionIncome = 0d;
            if (StringUtils.isNotBlank(map.get("reductionIncome"))){
                reductionIncome = Double.parseDouble(map.get("reductionIncome"));
            }
            reductionIncome = reductionIncome + historyDataDO.getReductionIncome();

//            Double amountOfPropertyFee;
            String remarks = "";
            if (StringUtils.isNotBlank(map.get("remarks"))){
                remarks = map.get("remarks");
            }
            double amountOfPropertyFee = paidInPropertyFee+decorationDeposit+cashCoupon+oldPropertyBringsNew+deductionThreeWayFees+reductionIncome;
            historyDataDO.setAmountOfPropertyFee(amountOfPropertyFee);
            historyDataDO.setRoomCode(roomCode);
            historyDataDO.setRoomSize(roomSize);
            historyDataDO.setDeliveryTime(deliveryTime);
            historyDataDO.setStartTime(startTime);
            historyDataDO.setStatus(status);
            historyDataDO.setPaidInPropertyFee(paidInPropertyFee);
            historyDataDO.setDecorationManagementFee(decorationManagementFee);
            historyDataDO.setGarbageCollectionAndTransportationFee(garbageCollectionAndTransportationFee);
            historyDataDO.setDecorationDeposit(decorationDeposit);
            historyDataDO.setRefundedDecorationDeposit(refundedDecorationDeposit);
            historyDataDO.setDeductedDecorationDeposit(deductedDecorationDeposit);
            historyDataDO.setCashCoupon(cashCoupon);
            historyDataDO.setOldPropertyBringsNew(oldPropertyBringsNew);
//            historyDataDO.setRecoveryPreviousDebts(recoveryPreviousDebts);
//            historyDataDO.setCurrentIncomeFromAdvanceReceipts(currentIncomeFromAdvanceReceipts);
            historyDataDO.setDeductionThreeWayFees(deductionThreeWayFees);
            historyDataDO.setReductionIncome(reductionIncome);
            historyDataDO.setRemarks(remarks);
            historyDataDO.setOrganizationId(organizationId);
            historyDataDO.setUdt(currentTime);

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
//            json.put("customerUserId",customerUserId);
            json.put("startTime",DateUtil.getFirstDayOfMonth(Integer.parseInt(Tools.analysisStr(dueTime,"-",1)),Integer.parseInt(Tools.analysisStr(dueTime,"-",2))));
            BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeItemDO.getChargeCode(),1);
            ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
            ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
            BillDetailedDO billDetailedBackDO1 = null;
            try {
                billDetailedBackDO1 = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
            } catch (ConnectException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：物业费计算失败！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            } catch (ParseException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：物业费计算失败！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
//                    billDetailedBackDO1.setDueTime(DateUtil.getTimeDay(billDetailedBackDO1.getDueTime(),DateUtil.YEAR_MONTH_DAY,-1));
//                    Double originalActualMoneyCollection = billDetailedBackDO1.getActualMoneyCollection();

            //算出一个月的费用
            double monthlyPropertyFee = billDetailedBackDO1.getAmountReceivable();
            /**
             * 计算空置费
             */
            //统计计算月的天数
            String movementTime = deliveryTime;
            int vacantMonthCount = 0;
            int vacantDayCount = 0;
            if (-1==DateUtil.compareDate(deliveryTime,startTime)){
                System.out.println("计算空置费");
                //计算物业费欠费时长
                for (int j = 0;;j++){
                    if (1==DateUtil.compareDate(movementTime,startTime)){
                        break;
                    }
                    try {
                        movementTime = DateUtil.getAfterMonth(movementTime,1);
                    } catch (ParseException e) {
                        LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                        logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                        logOldBillsDO.setResult("导入失败");
                        logOldBillsDO.setRoomCode(map.get("roomCode"));
                        logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间格式填写错误！");
                        logOldBillsDO.setOrganizationId(organizationId);
                        logOldBillsDOS.add(logOldBillsDO);
                        continue;
                    }
                    vacantMonthCount++;
                }
                vacantMonthCount = vacantMonthCount-1;
                try {
                    movementTime = DateUtil.getAfterMonth(movementTime,-1);
                    vacantDayCount = DateUtil.longOfTwoDate(movementTime,startTime);
                } catch (ParseException e) {
                    LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                    logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                    logOldBillsDO.setResult("导入失败");
                    logOldBillsDO.setRoomCode(map.get("roomCode"));
                    logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间格式填写错误！");
                    logOldBillsDO.setOrganizationId(organizationId);
                    logOldBillsDOS.add(logOldBillsDO);
                    continue;
                }
                //计算一天的物业费
                int dayMonth = DateUtil.daysMonth(Integer.parseInt(Tools.analysisStr(startTime,"-",1)),Integer.parseInt(Tools.analysisStr(startTime,"-",2)));
                double dayFee = Tools.moneyHalfAdjust(monthlyPropertyFee/dayMonth);

                //算出空置费
                historyDataDO.setVacancyCharge(Tools.moneyHalfAdjust(vacantMonthCount*monthlyPropertyFee+vacantDayCount*dayFee));
            }else {
                historyDataDO.setVacancyCharge(0d);
            }
            historyDataDAO.saveAndFlush(historyDataDO);

            /**
             * 根据物业费推算到期时间
             * 物业费金额 = 实收物业费+已抵扣装修保证金+代金券+老带新+三通费抵扣+冲减收入
             * amountOfPropertyFee
             */
            int monthCount = (int) Math.floor(amountOfPropertyFee/monthlyPropertyFee);
            double surplus = Tools.moneyHalfAdjust(amountOfPropertyFee - monthlyPropertyFee*monthCount);
            try {
                dueTime = DateUtil.getAfterMonth(startTime,monthCount);
            } catch (ParseException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("roomCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间格式填写错误！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
            HistoryDataPropertyDueTimeDO historyDataPropertyDueTimeDO = historyDataPropertyDueTimeDAO.findByOrganizationIdAndRoomCode(organizationId,roomCode);
            historyDataPropertyDueTimeDO.setOrganizationId(organizationId);
            historyDataPropertyDueTimeDO.setRoomCode(roomCode);
            historyDataPropertyDueTimeDO.setDueTime(dueTime);
            historyDataPropertyDueTimeDO.setUdt(currentTime);

            RoomAndCustomerDO roomAndCustomerDO = customerService.findByRoomCodeAndOrganizationIdAndIdentity(roomCode,organizationId);
            //处理余额
            if (0d<surplus){
                //导入的历史数据    余额都为零   不需要累加
//                Double sqlSurplus = iRoomAndCustomerDAO.findByRoomCodeAndUserIdSurplus(roomCode,roomAndCustomerDO.getCustomerUserId());
                iRoomAndCustomerDAO.updateSurplus(surplus,roomCode,roomAndCustomerDO.getCustomerUserId());
            }
            redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,"NAN");
            historyDataPropertyDueTimeDAO.saveAndFlush(historyDataPropertyDueTimeDO);
            LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
            logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
            logOldBillsDO.setResult("导入成功");
            logOldBillsDO.setRoomCode(map.get("roomCode"));
            logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            logOldBillsDO.setRemarks("");
            logOldBillsDO.setOrganizationId(organizationId);
            logOldBillsDOS.add(logOldBillsDO);
            count++;
        }
        logOldBillsDAO.saveAll(logOldBillsDOS);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",list.size()); //导入数据条数
        map.put("realNumber",count); //导入成功条数
        map.put("logOldBillsDOS",logOldBillsDOS);
        return map;
    }

    @Override
    public Map<String, Object> importExclusiveParkingSpaceHistoryData(MultipartFile multipartFile, String userId) {
        String currentTime = DateUtil.date(DateUtil.FORMAT_PATTERN);

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String filePath = "D:\\newExcel.xlsx";
        String columns[] = {"parkingSpaceCode","deliveryTime","startTime","vacancyCharge",
                "amountReceivable","actualMoneyCollection","remarks"};
        wb = ExcelPOI.readExcel(multipartFile);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        logger.info("读取旧账单数据成功！数据量：{}",list.size());
        Random random = new Random();
        int ends = random.nextInt(99);
        String importCode = DateUtil.timeStamp().toString()+ends;
        List<LogOldBillsDO> logOldBillsDOS = new ArrayList<>();
        int count = 0;
        for (int i = 0;i<list.size();i++) {

            Map<String, String> map = new HashMap<>();
            map = list.get(i);

            if (StringUtils.isBlank(map.get("parkingSpaceCode"))||StringUtils.isBlank(map.get("deliveryTime"))||StringUtils.isBlank(map.get("startTime"))){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：必填字段不能为空！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String parkingSpaceCode = map.get("parkingSpaceCode");

            ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(parkingSpaceCode,organizationId,0);

            if (null==parkingSpaceManagementDO){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：车位基础信息不存在！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String deliveryTime = map.get("deliveryTime");

            String startTime = map.get("startTime");

            Double vacancyCharge = 0d;
            if (StringUtils.isNotBlank(map.get("vacancyCharge"))){
                vacancyCharge = Double.parseDouble(map.get("vacancyCharge"));
            }

            Double amountReceivable = 0d;
            if (StringUtils.isNotBlank(map.get("amountReceivable"))){
                amountReceivable = Double.parseDouble(map.get("amountReceivable"));
            }

            double actualMoneyCollection = 0d;
            if (StringUtils.isNotBlank(map.get("actualMoneyCollection"))){
                actualMoneyCollection = Double.parseDouble(map.get("actualMoneyCollection"));
            }

            String remarks = "";
            if (StringUtils.isNotBlank(map.get("remarks"))){
                remarks = map.get("remarks");
            }


            ExclusiveParkingSpaceHistoryDataDO exclusiveParkingSpaceHistoryDataDO = exclusiveParkingSpaceHistoryDataDAO.findByOrganizationIdAndParkingSpaceCode(organizationId,parkingSpaceCode);

            if (null==exclusiveParkingSpaceHistoryDataDO){
                exclusiveParkingSpaceHistoryDataDO = new ExclusiveParkingSpaceHistoryDataDO();
            }

            //计算专车位到期时间存库
            ExclusiveParkingSpaceHistoryDataDueTimeDO exclusiveParkingSpaceHistoryDataDueTimeDO = exclusiveParkingSpaceHistoryDataDueTimeDAO.findByOrganizationIdAndParkingSpaceCode(organizationId,parkingSpaceCode);
            if (null == exclusiveParkingSpaceHistoryDataDueTimeDO){
                exclusiveParkingSpaceHistoryDataDueTimeDO = new ExclusiveParkingSpaceHistoryDataDueTimeDO();
            }
            //计算到期月份推移时间
            String dueTime = null;
            ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCodeAndOrganizationId(parkingSpaceCode,organizationId);
            Double money = iChargeDAO.findMoneyBychargeTypeAndParkingSpacePlaceAndParkingSpaceTypeAndOrganizationId(6,parkingSpaceInfoDO.getParkingSpacePlace(),parkingSpaceInfoDO.getParkingSpaceType(),organizationId);
            if (null == money){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：收费项目配置错误！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
            int monthly = new Double(actualMoneyCollection/money).intValue();
            try {
                dueTime = DateUtil.getAfterMonth(startTime,monthly);
            } catch (ParseException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间计算错误！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            exclusiveParkingSpaceHistoryDataDueTimeDO.setUdt(currentTime);
            exclusiveParkingSpaceHistoryDataDueTimeDO.setOrganizationId(organizationId);
            exclusiveParkingSpaceHistoryDataDueTimeDO.setParkingSpaceCode(parkingSpaceCode);

            exclusiveParkingSpaceHistoryDataDueTimeDO.setDueTime(dueTime);

            exclusiveParkingSpaceHistoryDataDO.setParkingSpaceCode(parkingSpaceCode);
            exclusiveParkingSpaceHistoryDataDO.setOrganizationId(organizationId);
            exclusiveParkingSpaceHistoryDataDO.setRemarks(remarks);
            exclusiveParkingSpaceHistoryDataDO.setIdt(currentTime);
            exclusiveParkingSpaceHistoryDataDO.setActualMoneyCollection(actualMoneyCollection);
            exclusiveParkingSpaceHistoryDataDO.setAmountReceivable(amountReceivable);
            exclusiveParkingSpaceHistoryDataDO.setVacancyCharge(vacancyCharge);
            exclusiveParkingSpaceHistoryDataDO.setStartTime(startTime);
            exclusiveParkingSpaceHistoryDataDO.setDeliveryTime(deliveryTime);

            exclusiveParkingSpaceHistoryDataDAO.saveAndFlush(exclusiveParkingSpaceHistoryDataDO);
            exclusiveParkingSpaceHistoryDataDueTimeDAO.saveAndFlush(exclusiveParkingSpaceHistoryDataDueTimeDO);


            LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
            logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
            logOldBillsDO.setResult("导入成功");
            logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
            logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            logOldBillsDO.setRemarks("");
            logOldBillsDO.setOrganizationId(organizationId);
            logOldBillsDOS.add(logOldBillsDO);
            count++;
        }
        logOldBillsDAO.saveAll(logOldBillsDOS);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",list.size()); //导入数据条数
        map.put("realNumber",count); //导入成功条数
        map.put("logOldBillsDOS",logOldBillsDOS);
        return map;
    }

    @Override
    public Map<String, Object> importExclusiveParkingSpaceHistoryDataCumulative(MultipartFile multipartFile, String userId) throws ParseException {
        String currentTime = DateUtil.date(DateUtil.FORMAT_PATTERN);

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String filePath = "D:\\newExcel.xlsx";
        String columns[] = {"parkingSpaceCode","deliveryTime","startTime","vacancyCharge",
                "amountReceivable","actualMoneyCollection","remarks"};
        wb = ExcelPOI.readExcel(multipartFile);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        logger.info("读取旧账单数据成功！数据量：{}",list.size());
        Random random = new Random();
        int ends = random.nextInt(99);
        String importCode = DateUtil.timeStamp().toString()+ends;
        List<LogOldBillsDO> logOldBillsDOS = new ArrayList<>();
        int count = 0;
        for (int i = 0;i<list.size();i++) {

            Map<String, String> map = new HashMap<>();
            map = list.get(i);

            if (StringUtils.isBlank(map.get("parkingSpaceCode"))||StringUtils.isBlank(map.get("deliveryTime"))||StringUtils.isBlank(map.get("startTime"))){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：必填字段不能为空！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String parkingSpaceCode = map.get("parkingSpaceCode");

            ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findAllByParkingSpaceCodeAndOrganizationIdAndLoggedOffState(parkingSpaceCode,organizationId,0);

            if (null==parkingSpaceManagementDO){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：车位基础信息不存在！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            ExclusiveParkingSpaceHistoryDataDO exclusiveParkingSpaceHistoryDataDO = exclusiveParkingSpaceHistoryDataDAO.findByOrganizationIdAndParkingSpaceCode(organizationId,parkingSpaceCode);

            if (null==exclusiveParkingSpaceHistoryDataDO){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：未查询到以前导入的数据！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            String deliveryTime = map.get("deliveryTime");

            String startTime = map.get("startTime");

            Double vacancyCharge = 0d;
            if (StringUtils.isNotBlank(map.get("vacancyCharge"))){
                vacancyCharge = Double.parseDouble(map.get("vacancyCharge"));
            }
            vacancyCharge = vacancyCharge + exclusiveParkingSpaceHistoryDataDO.getVacancyCharge();

            Double amountReceivable = 0d;
            if (StringUtils.isNotBlank(map.get("amountReceivable"))){
                amountReceivable = Double.parseDouble(map.get("amountReceivable"));
            }
            amountReceivable = amountReceivable + exclusiveParkingSpaceHistoryDataDO.getAmountReceivable();

            double actualMoneyCollection = 0d;
            if (StringUtils.isNotBlank(map.get("actualMoneyCollection"))){
                actualMoneyCollection = Double.parseDouble(map.get("actualMoneyCollection"));
            }
            actualMoneyCollection = actualMoneyCollection + exclusiveParkingSpaceHistoryDataDO.getActualMoneyCollection();


            String remarks = "";
            if (StringUtils.isNotBlank(map.get("remarks"))){
                remarks = map.get("remarks");
            }

            //计算专车位到期时间存库
            ExclusiveParkingSpaceHistoryDataDueTimeDO exclusiveParkingSpaceHistoryDataDueTimeDO = exclusiveParkingSpaceHistoryDataDueTimeDAO.findByOrganizationIdAndParkingSpaceCode(organizationId,parkingSpaceCode);
            //计算到期月份推移时间
            String dueTime = null;
            ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCodeAndOrganizationId(parkingSpaceCode,organizationId);
            Double money = iChargeDAO.findMoneyBychargeTypeAndParkingSpacePlaceAndParkingSpaceTypeAndOrganizationId(6,parkingSpaceInfoDO.getParkingSpacePlace(),parkingSpaceInfoDO.getParkingSpaceType(),organizationId);
            if (null == money){
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：收费项目配置错误！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }
            int monthly = new Double(actualMoneyCollection/money).intValue();
            try {
                dueTime = DateUtil.getAfterMonth(startTime,monthly);
            } catch (ParseException e) {
                LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
                logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
                logOldBillsDO.setResult("导入失败");
                logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
                logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOldBillsDO.setRemarks("第" + (i + 2)+"行，"+",导入失败！原因：时间计算错误！");
                logOldBillsDO.setOrganizationId(organizationId);
                logOldBillsDOS.add(logOldBillsDO);
                continue;
            }

            exclusiveParkingSpaceHistoryDataDueTimeDO.setUdt(currentTime);
            exclusiveParkingSpaceHistoryDataDueTimeDO.setOrganizationId(organizationId);
            exclusiveParkingSpaceHistoryDataDueTimeDO.setParkingSpaceCode(parkingSpaceCode);

            exclusiveParkingSpaceHistoryDataDueTimeDO.setDueTime(dueTime);

            exclusiveParkingSpaceHistoryDataDO.setParkingSpaceCode(parkingSpaceCode);
            exclusiveParkingSpaceHistoryDataDO.setOrganizationId(organizationId);
            exclusiveParkingSpaceHistoryDataDO.setRemarks(remarks);
            exclusiveParkingSpaceHistoryDataDO.setIdt(currentTime);
            exclusiveParkingSpaceHistoryDataDO.setActualMoneyCollection(actualMoneyCollection);
            exclusiveParkingSpaceHistoryDataDO.setAmountReceivable(amountReceivable);
            exclusiveParkingSpaceHistoryDataDO.setVacancyCharge(vacancyCharge);
            exclusiveParkingSpaceHistoryDataDO.setStartTime(startTime);
            exclusiveParkingSpaceHistoryDataDO.setDeliveryTime(deliveryTime);

            exclusiveParkingSpaceHistoryDataDAO.saveAndFlush(exclusiveParkingSpaceHistoryDataDO);
            exclusiveParkingSpaceHistoryDataDueTimeDAO.saveAndFlush(exclusiveParkingSpaceHistoryDataDueTimeDO);


            LogOldBillsDO logOldBillsDO = new LogOldBillsDO();
            logOldBillsDO.setCode(importCode);
//                logOldBillsDO.setOrderId(orderId);
            logOldBillsDO.setResult("导入成功");
            logOldBillsDO.setRoomCode(map.get("parkingSpaceCode"));
            logOldBillsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            logOldBillsDO.setRemarks("");
            logOldBillsDO.setOrganizationId(organizationId);
            logOldBillsDOS.add(logOldBillsDO);
            count++;
        }
        logOldBillsDAO.saveAll(logOldBillsDOS);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",list.size()); //导入数据条数
        map.put("realNumber",count); //导入成功条数
        map.put("logOldBillsDOS",logOldBillsDOS);
        return map;
    }

    @Override
    public PageData findByPage(int pageNum, int pageSize, String userId, String roomCode) {

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<HistoryDataDO> page = historyDataDAO.findAll(new Specification<HistoryDataDO>() {
            @Override
            public Predicate toPredicate(Root<HistoryDataDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));

                if (StringUtils.isNotBlank(roomCode)){
                    predicateList.add(criteriaBuilder.like(root.get("roomCode").as(String.class),"%"+roomCode+"%"));
                }
                return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        },pageable);

        return new PageData(page);
    }

    @Override
    public PageData findExclusiveParkingSpaceHistoryDataByPage(int pageNum, int pageSize, String userId, String parkingSpaceCode) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<ExclusiveParkingSpaceHistoryDataDO> page = exclusiveParkingSpaceHistoryDataDAO.findAll(new Specification<ExclusiveParkingSpaceHistoryDataDO>() {
            @Override
            public Predicate toPredicate(Root<ExclusiveParkingSpaceHistoryDataDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));

                if (StringUtils.isNotBlank(parkingSpaceCode)){
                    predicateList.add(criteriaBuilder.like(root.get("parkingSpaceCode").as(String.class),"%"+parkingSpaceCode+"%"));
                }
                return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        },pageable);

        return new PageData(page);
    }
}
