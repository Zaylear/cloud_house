package com.rbi.interactive.service.impl;

import com.rbi.interactive.abnormal.CustomerException;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.service.OwnerInformationService;
import com.rbi.interactive.utils.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;

@Service
public class OwnerInformationServiceImpl implements OwnerInformationService {

    private final static Logger logger = LoggerFactory.getLogger(OwnerInformationServiceImpl.class);

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    RegionDAO regionDAO;

    @Autowired
    BuildingDAO buildingDAO;

    @Autowired
    UnitDAO unitDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired
    RoomAndChargeItemsDAO roomAndChargeItemsDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    LogOwnerInformationDAO logOwnerInformationDAO;

//    @Autowired(required = false)
//    VillageChooseService villageChooseService;

    @Override
    public Map<String, Object> ownerInformation(MultipartFile multipartFile, String userId) throws ParseException {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String filePath = "D:\\newExcel.xlsx";
        String columns[] = {"villageName","regionName","buildingName","unitName","roomCode","roomSize",
                "roomType","roomStatus","renovationStatus","renovationStartTime","renovationDeadline","surname","idNumber",
                "mobilePhone","identity","normalPaymentStatus","startBillingTime","realRecyclingHomeTime","startTime","endTime","remarks"};
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
        logger.info("读取业主信息数据成功！数据量：{}",list.size());

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        String organizationName = organizationDO.getOrganizationName();
        List<LogOwnerInformationDO> logOwnerInformationDOS = new ArrayList<>();

        int importCount = 0;

        flag:for (int i = 0; i < list.size(); i++) {

            try {
                Map<String,String> map = new LinkedHashMap<String,String>();
                map = list.get(i);
                String villageName = map.get("villageName");
                String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();
                //判断小区
                List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnableAndVillageCode(organizationId,1,villageCode);
                if (0==villageDOS.size()){
                    //小区不存在，不能导入
                    LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                    logOwnerInformationDO.setOrganizationId(organizationId);
                    logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logOwnerInformationDO.setCode(i+2);
                    logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：小区不存在！");
                    logOwnerInformationDOS.add(logOwnerInformationDO);
                    continue;
                }
                String regionName = map.get("regionName");
                String regionCode = null;
                //判断地块编号是否存在
                RegionDO regionDO1 = regionDAO.findByVillageCodeAndRegionName(villageCode,regionName);
                if (null != regionDO1){
                    regionCode = regionDO1.getRegionCode();
                }else {
                    if ("".equals(Tools.filteringChinese(regionName))){
                        for (int k = 0;;k++){
                            regionCode = villageCode+"-"+PinYin.getPinYinHeadChar(regionName).toUpperCase()+"0"+new Random().nextInt(10);
                            if (null==regionDAO.findAllByRegionCodeAndVillageCode(regionCode,villageCode)){
                                break;
                            }
                            if (100==k){
                                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                                logOwnerInformationDO.setOrganizationId(organizationId);
                                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                                logOwnerInformationDO.setCode(i+2);
                                logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：生成地块编号异常！");
                                logOwnerInformationDOS.add(logOwnerInformationDO);
                                break flag;
                            }
                        }
                    }else {
                        regionCode = villageCode+"-"+ Tools.filteringChinese(regionName);
                        for (int k = 0;;k++){
                            if (null==regionDAO.findAllByRegionCodeAndVillageCode(regionCode,villageCode)){
                                break;
                            }
                            regionCode = villageCode+"-"+ Tools.filteringChinese(regionName)+new Random().nextInt(10);
                            if (k==100){
                                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                                logOwnerInformationDO.setOrganizationId(organizationId);
                                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                                logOwnerInformationDO.setCode(i+2);
                                logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：生成地块编号异常！");
                                logOwnerInformationDOS.add(logOwnerInformationDO);
                                break flag;
                            }
                        }
                    }
//                    regionCode = villageCode+"-"+regionCode;
                }

                String buildingName = map.get("buildingName");
                String buildingCode = regionCode+"-"+Tools.filteringChinese(buildingName);
                String unitName = map.get("unitName");
                String unitCode = buildingCode+"-"+Tools.filteringChinese(unitName);
                String roomCode = unitCode+"-"+map.get("roomCode");
                Double roomSize = Double.parseDouble(map.get("roomSize"));
                Integer roomType = Integer.parseInt(map.get("roomType"));
                Integer roomStatus = Integer.parseInt(map.get("roomStatus"));
                Integer renovationStatus = 0;
                if (null != map.get("renovationStatus")&&!"".equals(map.get("renovationStatus"))){
                    renovationStatus = Integer.parseInt(map.get("renovationStatus"));
                }
                String renovationStartTime = null;
                if (null != map.get("renovationStartTime")&&!"".equals(map.get("renovationStartTime"))){
                    renovationStartTime = map.get("renovationStartTime");
                }
                String renovationDeadline = null;
                if (null != map.get("renovationDeadline")&&!"".equals(map.get("renovationDeadline"))){
                    renovationDeadline = map.get("renovationDeadline");
                }
                String surname = map.get("surname");
                String idNumber = map.get("idNumber");
                String identity = map.get("identity");
                String mobilePhone = map.get("mobilePhone");
                String startBillingTime = map.get("startBillingTime");
                String realRecyclingHomeTime = map.get("realRecyclingHomeTime");
                String startTime = null;
                String endTime = null;
                if ("3".equals(identity)){
                    if (null==map.get("startTime")||"".equals(map.get("startTime"))||null==map.get("endTime")||"".equals(map.get("endTime"))){
                        LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                        logOwnerInformationDO.setRoomCode(roomCode);
                        logOwnerInformationDO.setOrganizationId(organizationId);
                        logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        logOwnerInformationDO.setCode(i+2);
                        logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：租户租房时间段不能为空！");
                        logOwnerInformationDOS.add(logOwnerInformationDO);
                        continue ;
                    }else {
                        startTime = map.get("startTime");
                        endTime = map.get("endTime");
                    }
                }

                String remarks = map.get("remarks");

                //导入地块
                RegionDO regionDO = regionDAO.findAllByRegionCodeAndVillageCode(regionCode,villageCode);
                if (null==regionDO){
                    regionDO = new RegionDO();
                    regionDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                }
                regionDO.setRegionCode(regionCode);
                regionDO.setRegionName(regionName);
                regionDO.setVillageCode(villageCode);
                regionDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                regionDAO.saveAndFlush(regionDO);

                //导入楼栋
                BuildingDO buildingDO = buildingDAO.findByBuildingCodeAndRegionCode(buildingCode,regionCode);
                if (null == buildingDO){
                    buildingDO = new BuildingDO();
                    buildingDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                }
                buildingDO.setBuildingCode(buildingCode);
                buildingDO.setBuildingName(buildingName);
                buildingDO.setRegionCode(regionCode);
                buildingDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                buildingDAO.saveAndFlush(buildingDO);

                //导入单元
                UnitDO unitDO = unitDAO.findByUnitCodeAndBuildingCode(unitCode,buildingCode);
                if (null == unitDO){
                    unitDO = new UnitDO();
                    unitDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                }
                unitDO.setBuildingCode(buildingCode);
                unitDO.setUnitCode(unitCode);
                unitDO.setUnitName(unitName);
                unitDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                unitDAO.saveAndFlush(unitDO);

                RoomDO roomDO = roomDAO.findByRoomCodeAndUnitCode(roomCode,unitCode);
                if (null == roomDO){
                    roomDO = new RoomDO();
                    roomDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                }
                roomDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                roomDO.setRoomCode(roomCode);
//                  此处判断装修时间，为空不set值
                if (renovationStatus==1){
                    roomDO.setRenovationDeadline(renovationDeadline);
                    roomDO.setRenovationStartTime(renovationStartTime);
                    roomDO.setRenovationStatus(renovationStatus);
                } else {
                    roomDO.setRenovationStatus(0);
                }

                roomDO.setRoomSize(roomSize);
                roomDO.setRoomStatus(roomStatus);
                roomDO.setRoomType(roomType);
                roomDO.setUnitCode(unitCode);
                roomDO.setRemarks(remarks);
                roomDAO.saveAndFlush(roomDO);

                //判断空置房，无业主导入信息
                if (1==roomStatus){
                    if (null==map.get("identity")||"".equals(map.get("identity"))){
                        LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                        logOwnerInformationDO.setRoomCode(roomCode);
                        logOwnerInformationDO.setOrganizationId(organizationId);
                        logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        logOwnerInformationDO.setCode(i+2);
                        logOwnerInformationDO.setResult("导入成功");
                        logOwnerInformationDOS.add(logOwnerInformationDO);
                        continue flag;
                    }
                }

                Integer normalPaymentStatus = Integer.parseInt(map.get("normalPaymentStatus"));

                List<String> customerUserIds = new ArrayList<>();

                for (int j = 0; j < Tools.containCharactersNumber(surname,";")+1; j++) {
                    //导入业主信息
                    CustomerInfoDO customerInfoDO = customerDAO.findByIdNumber(Tools.analysisStr(idNumber,";",j+1));

                    if (null == customerInfoDO){
                        customerInfoDO = new CustomerInfoDO();
                        customerInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        String customerId = DateUtil.timeStamp()+Tools.random(10, 99);
                        Integer count = 0;
                        while (1==count){
                            CustomerInfoDO customerInfoDO1 = customerDAO.findByUserId(customerId);
                            if (null == customerInfoDO1){
                                count = 1;
                            }else {
                                customerId = DateUtil.timeStamp()+Tools.random(10, 99);
                            }
                        }
                        customerInfoDO.setUserId(customerId);
                    }
                    customerInfoDO.setEnabled(1);
                    customerInfoDO.setIdNumber(Tools.analysisStr(idNumber,";",j+1));
                    customerInfoDO.setLoginStatus(0);
                    customerInfoDO.setMobilePhone(Tools.analysisStr(mobilePhone,";",j+1));
                    String  sex = "3";

                    //验证身份证号格式，包含港、澳、台
//                    String patternMainLand = "(^\\d{18}$)|(^\\d{15}$)";
//                    Pattern IDNumber = Pattern.compile(regexEmail);
//                    Matcher emailM = emailP.matcher(inputEmail);
//                    if (!emailM.find()) {
//                        throw new CustomerException(inputEmail + “邮箱填写不规范”);
//                    }

                    //根据身份证号判断性别
//                    if (0==Integer.parseInt(String.valueOf(Tools.analysisStr(idNumber,";",j+1).charAt(16)))/2){
//                        sex = "2";
//                    }else {
//                        sex = "1";
//                    }

                    try {
                        sex = RegexValidateCard.validateIdCard(Tools.analysisStr(idNumber,";",j+1));
                    } catch (CustomerException e) {
                        LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                        logOwnerInformationDO.setOrganizationId(organizationId);
                        logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        logOwnerInformationDO.setCode(i+2);
                        logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：身份证号填写错误！");
                        logOwnerInformationDOS.add(logOwnerInformationDO);
                        continue flag;
                    }

                    customerInfoDO.setSex(sex);
                    customerInfoDO.setSurname(Tools.analysisStr(surname,";",j+1));
                    customerInfoDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    customerDAO.saveAndFlush(customerInfoDO);

                    customerUserIds.add(customerInfoDO.getUserId());
                }

                /**
                 * 绑定业主房间信息
                 * 绑定房间与业主信息之前，先解除该房间与业主的所有关系重新绑定
                 */
//                roomAndCustomerDAO.deleteAllByRoomCode(roomCode);

                for (String customerUserId : customerUserIds) {
                    RoomAndCustomerDO roomAndCustomerDO = roomAndCustomerDAO.findByCustomerUserIdAndRoomCodeAndLoggedOffState(customerUserId,roomCode,0);
                    if (null == roomAndCustomerDO){
                        roomAndCustomerDO.setSurplus(0d);
                        roomAndCustomerDO = new RoomAndCustomerDO();
                        roomAndCustomerDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    }
                    roomAndCustomerDO.setRoomCode(roomCode);
                    roomAndCustomerDO.setRemarks(remarks);
                    roomAndCustomerDO.setCustomerUserId(customerUserId);
                    roomAndCustomerDO.setIdentity(Integer.parseInt(identity));
                    roomAndCustomerDO.setOrganizationId(organizationId);
                    roomAndCustomerDO.setOrganizationName(organizationName);
                    roomAndCustomerDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    roomAndCustomerDO.setPastDue(0);
                    roomAndCustomerDO.setLoggedOffState(0);
                    roomAndCustomerDO.setNormalPaymentStatus(normalPaymentStatus);
                    if ("3".equals(identity)){
                        roomAndCustomerDO.setStartTime(startTime);
                        roomAndCustomerDO.setEndTime(endTime);
                    }else {
                        roomAndCustomerDO.setStartBillingTime(startBillingTime);
                        roomAndCustomerDO.setRealRecyclingHomeTime(realRecyclingHomeTime);
                    }
                    roomAndCustomerDAO.save(roomAndCustomerDO);
                }

                //绑定房间收费项目信息
                List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findAllByOrganizationIdAndEnableAndMustPay(organizationId,1,1);
                for (ChargeItemDO chargeItemDO:chargeItemDOS){
                    if (1 == roomType){
                        if (2==chargeItemDO.getChargeType()||3 == chargeItemDO.getChargeType()){
                            continue;
                        }
                        RoomAndChargeItemsDO roomAndChargeItemsDO = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomCode,unitCode,buildingCode,regionCode,villageCode,organizationId,chargeItemDO.getChargeCode());
                        if (null == roomAndChargeItemsDO){
                            roomAndChargeItemsDO = new RoomAndChargeItemsDO();
                            roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        }
                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDO.setOrganizationId(organizationId);
                        roomAndChargeItemsDO.setOrganizationName(organizationName);
                        roomAndChargeItemsDO.setSurplus(0d);
                        roomAndChargeItemsDO.setChargeCode(chargeItemDO.getChargeCode());
                        roomAndChargeItemsDO.setBuildingCode(buildingCode);
                        roomAndChargeItemsDO.setBuildingName(buildingName);
                        roomAndChargeItemsDO.setRegionCode(regionCode);
                        roomAndChargeItemsDO.setRegionName(regionName);
                        roomAndChargeItemsDO.setRoomCode(roomCode);
                        roomAndChargeItemsDO.setUnitCode(unitCode);
                        roomAndChargeItemsDO.setUnitName(unitName);
                        roomAndChargeItemsDO.setVillageCode(villageCode);
                        roomAndChargeItemsDO.setVillageName(villageName);
                        roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
                    }else if (2 == roomType){
                        if (1==chargeItemDO.getChargeType()||2 == chargeItemDO.getChargeType()){
                            continue;
                        }
                        RoomAndChargeItemsDO roomAndChargeItemsDO = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomCode,unitCode,buildingCode,regionCode,villageCode,organizationId,chargeItemDO.getChargeCode());
                        if (null == roomAndChargeItemsDO){
                            roomAndChargeItemsDO = new RoomAndChargeItemsDO();
                            roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        }
                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDO.setOrganizationId(organizationId);
                        roomAndChargeItemsDO.setOrganizationName(organizationName);
                        roomAndChargeItemsDO.setSurplus(0d);
                        roomAndChargeItemsDO.setChargeCode(chargeItemDO.getChargeCode());
                        roomAndChargeItemsDO.setBuildingCode(buildingCode);
                        roomAndChargeItemsDO.setBuildingName(buildingName);
                        roomAndChargeItemsDO.setRegionCode(regionCode);
                        roomAndChargeItemsDO.setRegionName(regionName);
                        roomAndChargeItemsDO.setRoomCode(roomCode);
                        roomAndChargeItemsDO.setUnitCode(unitCode);
                        roomAndChargeItemsDO.setUnitName(unitName);
                        roomAndChargeItemsDO.setVillageCode(villageCode);
                        roomAndChargeItemsDO.setVillageName(villageName);
                        roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);

                    }else if (3 == roomType){
                        if (1==chargeItemDO.getChargeType()||3 == chargeItemDO.getChargeType()){
                            continue;
                        }
                        RoomAndChargeItemsDO roomAndChargeItemsDO = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomCode,unitCode,buildingCode,regionCode,villageCode,organizationId,chargeItemDO.getChargeCode());
                        if (null == roomAndChargeItemsDO){
                            roomAndChargeItemsDO = new RoomAndChargeItemsDO();
                            roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        }
                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDO.setOrganizationId(organizationId);
                        roomAndChargeItemsDO.setOrganizationName(organizationName);
                        roomAndChargeItemsDO.setSurplus(0d);
                        roomAndChargeItemsDO.setChargeCode(chargeItemDO.getChargeCode());
                        roomAndChargeItemsDO.setBuildingCode(buildingCode);
                        roomAndChargeItemsDO.setBuildingName(buildingName);
                        roomAndChargeItemsDO.setRegionCode(regionCode);
                        roomAndChargeItemsDO.setRegionName(regionName);
                        roomAndChargeItemsDO.setRoomCode(roomCode);
                        roomAndChargeItemsDO.setUnitCode(unitCode);
                        roomAndChargeItemsDO.setUnitName(unitName);
                        roomAndChargeItemsDO.setVillageCode(villageCode);
                        roomAndChargeItemsDO.setVillageName(villageName);
                        roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
                    }

                }
                importCount ++;
                //导入成功并封装日志
                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                logOwnerInformationDO.setRoomCode(roomCode);
                logOwnerInformationDO.setOrganizationId(organizationId);
                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOwnerInformationDO.setCode(i+2);
                logOwnerInformationDO.setResult("导入成功");
                logOwnerInformationDOS.add(logOwnerInformationDO);
            } catch (NumberFormatException e) {
                //数字转换错误
                logger.error("【业主信息导入服务类】数字转换错误，ERROR：{}",e);
                //封装日志
                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                logOwnerInformationDO.setOrganizationId(organizationId);
                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOwnerInformationDO.setCode(i+2);
                logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：状态码填写错误！");
                logOwnerInformationDOS.add(logOwnerInformationDO);
            } catch (NullPointerException e){
                //必填字段不能为空
                logger.error("【业主信息导入服务类】必填字段不能为空，ERROR：{}",e);
                //封装日志
                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                logOwnerInformationDO.setOrganizationId(organizationId);
                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOwnerInformationDO.setCode(i+2);
                logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：必填字段不能为空！");
                logOwnerInformationDOS.add(logOwnerInformationDO);
            } catch (StringIndexOutOfBoundsException e){
                //身份证号填写错误
                logger.error("【业主信息导入服务类】身份证号填写错误，ERROR：{}",e);
                //封装日志
                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                logOwnerInformationDO.setOrganizationId(organizationId);
                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOwnerInformationDO.setCode(i+2);
                logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：身份证号填写错误！");
                logOwnerInformationDOS.add(logOwnerInformationDO);
            }catch (DataIntegrityViolationException e){
                //时间格式填写错误
                logger.error("【业主信息导入服务类】时间格式填写错误，ERROR：{}",e);
                //封装日志
                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
                logOwnerInformationDO.setOrganizationId(organizationId);
                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logOwnerInformationDO.setCode(i+2);
                logOwnerInformationDO.setResult("第"+(i+2)+"行，导入失败！原因：时间格式填写错误！");
                logOwnerInformationDOS.add(logOwnerInformationDO);
            }
        }
        logOwnerInformationDAO.saveAll(logOwnerInformationDOS);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",list.size()); //导入数据条数
        map.put("realNumber",importCount); //导入成功条数
        map.put("logOwnerInformationDOS",logOwnerInformationDOS);//日志列表
//        villageChooseService.saveAllVillageTree();
        return map;
    }



//    public Map<String, Object> ownerInformation(MultipartFile multipartFile, String userId) throws ParseException {
//        Workbook wb =null;
//        Sheet sheet = null;
//        Row row = null;
//        List<Map<String,String>> list = null;
//        String cellData = null;
////        String filePath = "D:\\newExcel.xlsx";
//        String columns[] = {"villageName","regionName","buildingName","unitName","roomCode","roomSize",
//                "roomType","roomStatus","renovationStatus","renovationStartTime","renovationDeadline","surname","idNumber",
//                "mobilePhone","identity","remarks","normalPaymentStatus","startBillingTime"};
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
//        logger.info("读取业主信息数据成功！数据量：{}",list.size());
//
//        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
//        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
//        String organizationName = organizationDO.getOrganizationName();
//        List<LogOwnerInformationDO> logOwnerInformationDOS = new ArrayList<>();
//
//        int importCount = 0;
//
//        for (int i = 0; i < list.size(); i++) {
//
//            try {
//                Map<String,String> map = new LinkedHashMap<String,String>();
//                map = list.get(i);
//                String villageName = map.get("villageName");
//                String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();
//                String regionName = map.get("regionName");
//                String regionCode = villageCode+"-"+ Tools.filteringChinese(regionName);
//                String buildingName = map.get("buildingName");
//                String buildingCode = regionCode+"-"+Tools.filteringChinese(buildingName);
//                String unitName = map.get("unitName");
//                String unitCode = buildingCode+"-"+Tools.filteringChinese(unitName);
//                String roomCode = unitCode+"-"+map.get("roomCode");
//                Double roomSize = Double.parseDouble(map.get("roomSize"));
//                Integer roomType = Integer.parseInt(map.get("roomType"));
//                Integer roomStatus = Integer.parseInt(map.get("roomStatus"));
////                Integer renovationStatus = 0;
////                if (StringUtils.isNotBlank(map.get("renovationStatus"))){
////                    renovationStatus = Integer.parseInt(map.get("renovationStatus"));
////                }
////                String renovationStartTime = "";
////                if (StringUtils.isNotBlank(map.get("renovationStartTime"))){
////                    renovationStartTime = map.get("renovationStartTime");
////                }
////                String renovationDeadline = "";
////                if (StringUtils.isNotBlank(map.get("renovationDeadline"))){
////                    renovationDeadline = map.get("renovationDeadline");
////                }
//                String surname = map.get("surname");
//                String idNumber = map.get("idNumber");
//                String identity = map.get("identity");
//                String mobilePhone = map.get("mobilePhone");
//                Integer normalPaymentStatus = Integer.parseInt(map.get("normalPaymentStatus"));
//                String startBillingTime = map.get("startBillingTime").replace(".","-");
//                String remarks = map.get("remarks");
//
//                //判断小区
//                List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnableAndVillageCode(organizationId,1,villageCode);
//                if (0==villageDOS.size()){
//                    //小区不存在，不能导入
//                    LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
//                    logOwnerInformationDO.setOrganizationId(organizationId);
//                    logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    logOwnerInformationDO.setCode(i+2);
//                    logOwnerInformationDO.setResult("导入失败");
//                    logOwnerInformationDO.setRemarks("第"+(i+2)+"行，导入失败！原因：小区不存在！");
//                    logOwnerInformationDOS.add(logOwnerInformationDO);
//                    continue;
//                }
//                System.out.println(regionCode);
//                //导入地块
//                RegionDO regionDO = regionDAO.findAllByRegionCodeAndVillageCode(regionCode,villageCode);
//                if (null==regionDO){
//                    regionDO = new RegionDO();
//                    regionDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                }
//                regionDO.setRegionCode(regionCode);
//                regionDO.setRegionName(regionName);
//                regionDO.setVillageCode(villageCode);
//                regionDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                regionDAO.saveAndFlush(regionDO);
//
//                //导入楼栋
//                BuildingDO buildingDO = buildingDAO.findByBuildingCodeAndRegionCode(buildingCode,regionCode);
//                if (null == buildingDO){
//                    buildingDO = new BuildingDO();
//                    buildingDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                }
//                buildingDO.setBuildingCode(buildingCode);
//                buildingDO.setBuildingName(buildingName);
//                buildingDO.setRegionCode(regionCode);
//                buildingDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                buildingDAO.saveAndFlush(buildingDO);
//
//                //导入单元
//                UnitDO unitDO = unitDAO.findByUnitCodeAndBuildingCode(unitCode,buildingCode);
//                if (null == unitDO){
//                    unitDO = new UnitDO();
//                    unitDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                }
//                unitDO.setBuildingCode(buildingCode);
//                unitDO.setUnitCode(unitCode);
//                unitDO.setUnitName(unitName);
//                unitDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                unitDAO.saveAndFlush(unitDO);
//
//                RoomDO roomDO = roomDAO.findByRoomCodeAndUnitCode(roomCode,unitCode);
//                if (null == roomDO){
//                    roomDO = new RoomDO();
//                    roomDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                }
//                roomDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                roomDO.setRoomCode(roomCode);
////                roomDO.setRenovationDeadline(renovationDeadline);
////                roomDO.setRenovationStartTime(renovationStartTime);
////                roomDO.setRenovationStatus(renovationStatus);
//                roomDO.setRoomSize(roomSize);
//                roomDO.setRoomStatus(roomStatus);
//                roomDO.setRoomType(roomType);
//                roomDO.setUnitCode(unitCode);
//                roomDO.setRemarks(remarks);
//                roomDAO.saveAndFlush(roomDO);
//
//                for (int j = 0; j < Tools.containCharactersNumber(surname,";")+1; j++) {
//                    CustomerInfoDO customerInfoDO = customerDAO.findByMobilePhoneAndEnabled(Tools.analysisStr(mobilePhone,";",j+1),1);
//
//                    if (null == customerInfoDO){
//                        customerInfoDO = new CustomerInfoDO();
//                        customerInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        String customerId = DateUtil.timeStamp()+Tools.random(10, 99);
//                        Integer count = 0;
//                        while (1==count){
//                            CustomerInfoDO customerInfoDO1 = customerDAO.findByUserId(customerId);
//                            if (null == customerInfoDO1){
//                                count = 1;
//                            }else {
//                                customerId = DateUtil.timeStamp()+Tools.random(10, 99);
//                            }
//                        }
//                        customerInfoDO.setUserId(customerId);
//                    }
//                    customerInfoDO.setEnabled(1);
//                    customerInfoDO.setSex(Tools.analysisStr(idNumber,";",j+1));
//                    customerInfoDO.setLoginStatus(0);
//                    customerInfoDO.setMobilePhone(Tools.analysisStr(mobilePhone,";",j+1));
//                    String  sex = null;
////                    customerInfoDO.setSex(sex);
//                    customerInfoDO.setSurname(Tools.analysisStr(surname,";",j+1));
//                    customerInfoDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    customerDAO.saveAndFlush(customerInfoDO);
//
//                    RoomAndCustomerDO roomAndCustomerDO = roomAndCustomerDAO.findByUserIdAndRoomCodeAndLoggedOffState(customerInfoDO.getUserId(),roomCode,0);
//                    if (null == roomAndCustomerDO){
//                        roomAndCustomerDO = new RoomAndCustomerDO();
//                        roomAndCustomerDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    }
//                    roomAndCustomerDO.setRoomCode(roomCode);
//                    roomAndCustomerDO.setRemarks(remarks);
//                    roomAndCustomerDO.setUserId(customerInfoDO.getUserId());
//                    roomAndCustomerDO.setIdentity(1);
//                    roomAndCustomerDO.setOrganizationId(organizationId);
//                    roomAndCustomerDO.setOrganizationName(organizationName);
//                    roomAndCustomerDO.setSurplus(0d);
//                    roomAndCustomerDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    roomAndCustomerDO.setStartBillingTime(startBillingTime);
//                    roomAndCustomerDO.setPastDue(0);
//                    roomAndCustomerDO.setLoggedOffState(0);
//                    roomAndCustomerDO.setNormalPaymentStatus(normalPaymentStatus);
//                    roomAndCustomerDAO.saveAndFlush(roomAndCustomerDO);
//                }
//
//                List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findAllByOrganizationIdAndEnableAndMustPay(organizationId,1,1);
//                System.out.println(chargeItemDOS.size());
//                for (ChargeItemDO chargeItemDO:chargeItemDOS){
//                    if (1 == roomType){
//                        if (2==chargeItemDO.getChargeType()||3 == chargeItemDO.getChargeType()){
//                            continue;
//                        }
//                        RoomAndChargeItemsDO roomAndChargeItemsDO = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomCode,unitCode,buildingCode,regionCode,villageCode,organizationId,chargeItemDO.getChargeCode());
//                        if (null == roomAndChargeItemsDO){
//                            roomAndChargeItemsDO = new RoomAndChargeItemsDO();
//                            roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        }
//                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        roomAndChargeItemsDO.setOrganizationId(organizationId);
//                        roomAndChargeItemsDO.setOrganizationName(organizationName);
//                        roomAndChargeItemsDO.setSurplus(0d);
//                        roomAndChargeItemsDO.setChargeCode(chargeItemDO.getChargeCode());
//                        roomAndChargeItemsDO.setBuildingCode(buildingCode);
//                        roomAndChargeItemsDO.setBuildingName(buildingName);
//                        roomAndChargeItemsDO.setRegionCode(regionCode);
//                        roomAndChargeItemsDO.setRegionName(regionName);
//                        roomAndChargeItemsDO.setRoomCode(roomCode);
//                        roomAndChargeItemsDO.setUnitCode(unitCode);
//                        roomAndChargeItemsDO.setUnitName(unitName);
//                        roomAndChargeItemsDO.setVillageCode(villageCode);
//                        roomAndChargeItemsDO.setVillageName(villageName);
//                        roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
//                    }else if (2 == roomType){
//                        if (1==chargeItemDO.getChargeType()||2 == chargeItemDO.getChargeType()){
//                            continue;
//                        }
//                        RoomAndChargeItemsDO roomAndChargeItemsDO = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomCode,unitCode,buildingCode,regionCode,villageCode,organizationId,chargeItemDO.getChargeCode());
//                        if (null == roomAndChargeItemsDO){
//                            roomAndChargeItemsDO = new RoomAndChargeItemsDO();
//                            roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        }
//                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        roomAndChargeItemsDO.setOrganizationId(organizationId);
//                        roomAndChargeItemsDO.setOrganizationName(organizationName);
//                        roomAndChargeItemsDO.setSurplus(0d);
//                        roomAndChargeItemsDO.setChargeCode(chargeItemDO.getChargeCode());
//                        roomAndChargeItemsDO.setBuildingCode(buildingCode);
//                        roomAndChargeItemsDO.setBuildingName(buildingName);
//                        roomAndChargeItemsDO.setRegionCode(regionCode);
//                        roomAndChargeItemsDO.setRegionName(regionName);
//                        roomAndChargeItemsDO.setRoomCode(roomCode);
//                        roomAndChargeItemsDO.setUnitCode(unitCode);
//                        roomAndChargeItemsDO.setUnitName(unitName);
//                        roomAndChargeItemsDO.setVillageCode(villageCode);
//                        roomAndChargeItemsDO.setVillageName(villageName);
//                        roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
//
//                    }else if (3 == roomType){
//                        if (1==chargeItemDO.getChargeType()||3 == chargeItemDO.getChargeType()){
//                            continue;
//                        }
//                        RoomAndChargeItemsDO roomAndChargeItemsDO = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomCode,unitCode,buildingCode,regionCode,villageCode,organizationId,chargeItemDO.getChargeCode());
//                        if (null == roomAndChargeItemsDO){
//                            roomAndChargeItemsDO = new RoomAndChargeItemsDO();
//                            roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        }
//                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                        roomAndChargeItemsDO.setOrganizationId(organizationId);
//                        roomAndChargeItemsDO.setOrganizationName(organizationName);
//                        roomAndChargeItemsDO.setSurplus(0d);
//                        roomAndChargeItemsDO.setChargeCode(chargeItemDO.getChargeCode());
//                        roomAndChargeItemsDO.setBuildingCode(buildingCode);
//                        roomAndChargeItemsDO.setBuildingName(buildingName);
//                        roomAndChargeItemsDO.setRegionCode(regionCode);
//                        roomAndChargeItemsDO.setRegionName(regionName);
//                        roomAndChargeItemsDO.setRoomCode(roomCode);
//                        roomAndChargeItemsDO.setUnitCode(unitCode);
//                        roomAndChargeItemsDO.setUnitName(unitName);
//                        roomAndChargeItemsDO.setVillageCode(villageCode);
//                        roomAndChargeItemsDO.setVillageName(villageName);
//                        roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
//                    }
//
//                }
//                importCount ++;
//                //导入成功并封装日志
//                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
//                logOwnerInformationDO.setRoomCode(roomCode);
//                logOwnerInformationDO.setOrganizationId(organizationId);
//                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOwnerInformationDO.setCode(i+2);
//                logOwnerInformationDO.setResult("导入成功");
//                logOwnerInformationDOS.add(logOwnerInformationDO);
//            } catch (NumberFormatException e) {
//                //数字转换错误
//                logger.error("【业主信息导入服务类】数字转换错误，ERROR：{}",e);
//                //封装日志
//                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
//                logOwnerInformationDO.setOrganizationId(organizationId);
//                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOwnerInformationDO.setCode(i+2);
//                logOwnerInformationDO.setResult("导入失败");
//                logOwnerInformationDO.setRemarks("第"+(i+2)+"行，导入失败！原因：状态码填写错误！");
//                logOwnerInformationDOS.add(logOwnerInformationDO);
//            } catch (NullPointerException e){
//                //必填字段不能为空
//                logger.error("【业主信息导入服务类】必填字段不能为空，ERROR：{}",e);
//                //封装日志
//                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
//                logOwnerInformationDO.setOrganizationId(organizationId);
//                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOwnerInformationDO.setCode(i+2);
//                logOwnerInformationDO.setResult("导入失败");
//                logOwnerInformationDO.setRemarks("第"+(i+2)+"行，导入失败！原因：必填字段不能为空！");
//                logOwnerInformationDOS.add(logOwnerInformationDO);
//            } catch (StringIndexOutOfBoundsException e){
//                //身份证号填写错误
//                logger.error("【业主信息导入服务类】身份证号填写错误，ERROR：{}",e);
//                //封装日志
//                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
//                logOwnerInformationDO.setOrganizationId(organizationId);
//                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOwnerInformationDO.setCode(i+2);
//                logOwnerInformationDO.setResult("导入失败");
//                logOwnerInformationDO.setRemarks("第"+(i+2)+"行，导入失败！原因：身份证号填写错误！");
//                logOwnerInformationDOS.add(logOwnerInformationDO);
//            }catch (DataIntegrityViolationException e){
//                //时间格式填写错误
//                logger.error("【业主信息导入服务类】时间格式填写错误，ERROR：{}",e);
//                //封装日志
//                LogOwnerInformationDO logOwnerInformationDO = new LogOwnerInformationDO();
//                logOwnerInformationDO.setOrganizationId(organizationId);
//                logOwnerInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                logOwnerInformationDO.setCode(i+2);
//                logOwnerInformationDO.setResult("导入失败");
//                logOwnerInformationDO.setRemarks("第"+(i+2)+"行，导入失败！原因：时间格式填写错误！");
//                logOwnerInformationDOS.add(logOwnerInformationDO);
//            }
//        }
//        logger.info("导入业主信息完成！");
//        logOwnerInformationDAO.saveAll(logOwnerInformationDOS);
//        Map<String,Object> map = new HashMap<>();
//        map.put("totalNumber",list.size()); //导入数据条数
//        map.put("realNumber",importCount); //导入成功条数
//        map.put("logOwnerInformationDOS",logOwnerInformationDOS);//日志列表
//        return map;
//    }
}
