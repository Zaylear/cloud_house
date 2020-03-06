package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.CustomerException;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.ParkingSpaceManagementDTO;
import com.rbi.interactive.service.ParkingSpaceManagementService;
import com.rbi.interactive.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ParkingSpaceManagementServiceImpl implements ParkingSpaceManagementService {
    @Autowired
    UserInfoDAO userInfoDAO;
    @Autowired
    OrganizationDAO organizationDAO;
    @Autowired
    ParkingSpaceManagementDAO parkingSpaceManagementDAO;
    @Autowired
    ParkingSpaceInfoDAO parkingSpaceInfoDAO;
    @Autowired(required = false)
    IParkingSpaceInfoDAO iParkingSpaceInfoDAO;
    @Autowired(required = false)
    IParkingSpaceManagementDAO iParkingSpaceManagementDAO;
    @Autowired(required = false)
    CustomerServiceImpl customerService;
    @Autowired(required = false)
    CustomerInfoDAO customerInfoDAO;
    @Autowired
    LogParkingSpaceManagementDAO logParkingSpaceManagementDAO;
    @Autowired
    VillageDAO villageDAO;



    @Override
    public String add(JSONObject json, String userId) {
        ParkingSpaceManagementDO parkingSpaceManagementDO = JSONObject.toJavaObject(json, ParkingSpaceManagementDO.class);

//        String uId = null;
//        if (null != parkingSpaceManagementDO.getIdNumber()){
//            CustomerInfoDO customerInfoDO = customerInfoDAO.findByIdNumber(parkingSpaceManagementDO.getIdNumber());
//            if (null == customerInfoDO){
//                Integer sex = Integer.valueOf(RegexValidateCard.validateIdCard(parkingSpaceManagementDO.getIdNumber()));
//                uId = DateUtil.timeStamp() + Tools.random(10, 99);
//                System.out.println("添加用户");
//                iParkingSpaceManagementDAO.addCustomer(uId, parkingSpaceManagementDO.getSurname() , parkingSpaceManagementDO.getIdNumber(), sex, parkingSpaceManagementDO.getMobilePhone());
//            }else {
//                uId = customerInfoDO.getUserId();
//            }
//        }

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        String organizationName = organizationDO.getOrganizationName();


        int count = iParkingSpaceManagementDAO.countTodayNum(organizationId)+1;
        String ti = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);//查询当天租车数量
        String contractNumber = "E"+ti+ Tools.frontCompWithZore(count,4);
        ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
        parkingSpaceManagementDO.setVillageCode(parkingSpaceInfoDO.getVillageCode());
        parkingSpaceManagementDO.setVillageName(parkingSpaceInfoDO.getVillageName());
        parkingSpaceManagementDO.setRegionCode(parkingSpaceInfoDO.getRegionCode());
        parkingSpaceManagementDO.setRegionName(parkingSpaceInfoDO.getRegionName());
        parkingSpaceManagementDO.setBuildingCode(parkingSpaceInfoDO.getBuildingCode());
        parkingSpaceManagementDO.setBuildingName(parkingSpaceInfoDO.getBuildingName());
        parkingSpaceManagementDO.setOrganizationId(organizationId);
        parkingSpaceManagementDO.setOrganizationName(organizationName);
        parkingSpaceManagementDO.setContractNumber(contractNumber);
//        parkingSpaceManagementDO.setCustomerUserId(uId);
        parkingSpaceManagementDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        parkingSpaceManagementDO.setLoggedOffState(0);
        int num = parkingSpaceManagementDAO.countByParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
        if (num != 0) {
            return "该车位已卖出";
        }
        if (parkingSpaceInfoDO.getParkingSpaceNature().equals("1")){
            parkingSpaceManagementDAO.save(parkingSpaceManagementDO);
            return "10000";
        }else {
            parkingSpaceManagementDAO.save(parkingSpaceManagementDO);
            iParkingSpaceInfoDAO.updateNature(parkingSpaceManagementDO.getParkingSpaceCode());
            iParkingSpaceInfoDAO.rentNuberLess(parkingSpaceInfoDO.getRegionCode());
            iParkingSpaceInfoDAO.properNuberAdd(parkingSpaceInfoDO.getRegionCode());
            return "10000";
        }
    }



    @Override
    public JSONArray findParkingSpaceCode(String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<ParkingSpaceManagementDTO> parkingSpaceManagementDTOS = iParkingSpaceManagementDAO.findAllByOrganizationId(organizationId);
        List<Map<String, Object>> data = new ArrayList<>();
        String villageCode = "";
        String regionCode = "";
        String buildingCode = "";
        String parkingSpaceCode = "";
        for (ParkingSpaceManagementDTO parkingSpaceManagementDTO : parkingSpaceManagementDTOS) {
            if (!villageCode.equals(parkingSpaceManagementDTO.getVillageCode())) {
                villageCode = parkingSpaceManagementDTO.getVillageCode();
                Map<String, Object> map = new HashMap<>();
                map.put("code", parkingSpaceManagementDTO.getVillageCode());
                map.put("pid", "");
                map.put("name", parkingSpaceManagementDTO.getVillageName());
                map.put("level","1");
                data.add(map);
            }
        }
        for (ParkingSpaceManagementDTO parkingSpaceManagementDTO : parkingSpaceManagementDTOS) {
            if (!regionCode.equals(parkingSpaceManagementDTO.getRegionCode())) {
                regionCode = parkingSpaceManagementDTO.getRegionCode();
                Map<String, Object> map = new HashMap<>();
                map.put("code", parkingSpaceManagementDTO.getRegionCode());
                map.put("pid", parkingSpaceManagementDTO.getVillageCode());
                map.put("name", parkingSpaceManagementDTO.getRegionName());
                map.put("level","2");
                data.add(map);
            }
        }
        for (ParkingSpaceManagementDTO parkingSpaceManagementDTO : parkingSpaceManagementDTOS) {
            if (!buildingCode.equals(parkingSpaceManagementDTO.getBuildingCode()) && null != parkingSpaceManagementDTO.getBuildingCode() && !"".equals(parkingSpaceManagementDTO.getBuildingCode())) {
                buildingCode = parkingSpaceManagementDTO.getBuildingCode();
                Map<String, Object> map = new HashMap<>();
                map.put("code", parkingSpaceManagementDTO.getBuildingCode());
                map.put("pid", parkingSpaceManagementDTO.getRegionCode());
                map.put("name", parkingSpaceManagementDTO.getBuildingName());
                map.put("level","3");
                data.add(map);
            }
        }
        for (ParkingSpaceManagementDTO parkingSpaceManagementDTO : parkingSpaceManagementDTOS) {
            if (!parkingSpaceCode.equals(parkingSpaceManagementDTO.getParkingSpaceCode())) {
                parkingSpaceCode = parkingSpaceManagementDTO.getParkingSpaceCode();
                Map<String, Object> map = new HashMap<>();
                if (null == parkingSpaceManagementDTO.getBuildingCode() || "".equals(parkingSpaceManagementDTO.getBuildingCode())){
                    map.put("code", parkingSpaceManagementDTO.getParkingSpaceCode());
                    map.put("pid", parkingSpaceManagementDTO.getRegionCode());
                    map.put("name", parkingSpaceManagementDTO.getParkingSpaceCode());
                    map.put("level","4");
                    data.add(map);
                }else{
                    map.put("code", parkingSpaceManagementDTO.getParkingSpaceCode());
                    map.put("pid", parkingSpaceManagementDTO.getBuildingCode());
                    map.put("name", parkingSpaceManagementDTO.getParkingSpaceCode());
                    map.put("level","4");
                    data.add(map);
                }
            }
        }
        System.out.println("map集;"+data);
        JSONArray result = EncapsulationTreeUtil.listToTree(JSONArray.parseArray(JSON.toJSONString(data)), "code", "pid", "SpaceDTO");
        return result;
    }



    @Override
    public PageData findFirstByPage(int pageNum, int pageSize, String organizationId) {
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationId(organizationId);
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceManagementDAO.findVillageNum(villageDOS.get(0).getVillageCode());
        List<ParkingSpaceManagementDO> parkingSpaceInfoDOS = iParkingSpaceManagementDAO.findVillage(villageDOS.get(0).getVillageCode(),pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceInfoDOS);
    }

    @Override
    public PageData findVillage(String villageCode,int pageNum, int pageSize) {

        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceManagementDAO.findVillageNum(villageCode);
        List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = iParkingSpaceManagementDAO.findVillage(villageCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceManagementDOS);
    }

    @Override
    public PageData findRegion(String regionCode, int pageNum, int pageSize) {
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceManagementDAO.findRegionNum(regionCode);
        List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = iParkingSpaceManagementDAO.findRegion(regionCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceManagementDOS);
    }

    @Override
    public PageData findBuilding(String buildingCode, int pageNum, int pageSize) {
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceManagementDAO.findBuildingNum(buildingCode);
        List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = iParkingSpaceManagementDAO.findBuilding(buildingCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceManagementDOS);
    }

    @Override
    public PageData findParkingSpaceCode(String ParkinSpaceCode, int pageNum, int pageSize) {
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceManagementDAO.findParkingCodeNum(ParkinSpaceCode);
        List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = iParkingSpaceManagementDAO.findParkingSpaceCode(ParkinSpaceCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceManagementDOS);
    }

    @Override
    public void deleteById(JSONArray jsonArray) {
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            String id = jsonObject.getString("id");
            ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findAllByParkingSpaceManagementId(Integer.valueOf(jsonObject.getString("id")));
            iParkingSpaceInfoDAO.updateRentNature(parkingSpaceManagementDO.getParkingSpaceCode());
            iParkingSpaceInfoDAO.properNuberLess(parkingSpaceManagementDO.getRegionCode());
            iParkingSpaceInfoDAO.rentNuberAdd(parkingSpaceManagementDO.getRegionCode());
            iParkingSpaceManagementDAO.deleteById(Integer.valueOf(id));
        }
    }

    @Override
    public void update(ParkingSpaceManagementDO parkingSpaceManagementDO) {
//        String uId = null;
//        if (null != parkingSpaceManagementDO.getIdNumber()){
//            CustomerInfoDO customerInfoDO = customerInfoDAO.findByIdNumber(parkingSpaceManagementDO.getIdNumber());
//            if (null == customerInfoDO){
//                Integer sex = Integer.valueOf(RegexValidateCard.validateIdCard(parkingSpaceManagementDO.getIdNumber()));
//                uId = DateUtil.timeStamp() + Tools.random(10, 99);
//                System.out.println("添加用户");
//                iParkingSpaceManagementDAO.addCustomer(uId, parkingSpaceManagementDO.getSurname() , parkingSpaceManagementDO.getIdNumber(), sex, parkingSpaceManagementDO.getMobilePhone());
//            }else {
//                uId = customerInfoDO.getUserId();
//            }
//        }
        parkingSpaceManagementDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        parkingSpaceManagementDAO.saveAndFlush(parkingSpaceManagementDO);
    }


    @Override
    public  Map<String, Object> excelImport(MultipartFile file, String userId) throws IOException {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        String organizationName = organizationDO.getOrganizationName();

        List<String> list = POIUtil.readExcel(file, 14);

        List<LogParkingSpaceManagementInfoDO> logParkingSpaceManagementInfoDOS = new ArrayList<>();
        int importCount = 0;

        for (int i = 0; i < list.size(); i++) {

                List<String> list1 = Arrays.asList(StringUtils.split(list.get(i), ","));

                String parkingSpaceCode = Tools.changeBlank(list1.get(0));

                String surname = Tools.changeBlank(list1.get(1));
                String mobilePhone = Tools.changeBlank(list1.get(2));
                String idNumber = Tools.changeBlank(list1.get(3));
                String authorizedPersonName = Tools.changeBlank(list1.get(4));
                String authorizedPersonPhone = Tools.changeBlank(list1.get(5));
                String authorizedPersonIdNumber = Tools.changeBlank(list1.get(6));

                String startTime = Tools.changeBlank(list1.get(7));

                String licensePlateNumber = Tools.changeBlank(list1.get(8));
                String vehicleOriginalType = Tools.changeBlank(list1.get(9));
                String licensePlateColor = Tools.changeBlank(list1.get(10));
                String licensePlateType = Tools.changeBlank(list1.get(11));
                String remarks = Tools.changeBlank(list1.get(12));

                int count = iParkingSpaceManagementDAO.countTodayNum(organizationId)+1;
                String ti = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);//查询当天租车数量
                String contractNumber = "E"+ti+ Tools.frontCompWithZore(count,4);
                try {

                if (null == parkingSpaceCode){
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("失败");
                    log.setRemarks("车位编号不能为空");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
                    continue;
                }
                int num2 = parkingSpaceInfoDAO.countByParkingSpaceCode(parkingSpaceCode);
                int num3  = parkingSpaceManagementDAO.countByParkingSpaceCode(parkingSpaceCode);
                if (num2 ==0){
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("失败");
                    log.setRemarks("车位编号不存在");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
                    continue;
                }
                if (num3 != 0){
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("失败");
                    log.setRemarks("车位编号已绑定");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
                    continue;
                }
                if (null == startTime){
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("失败");
                    log.setRemarks("开始计费时间不能为空");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
                    continue;
                }

                ParkingSpaceManagementDO parkingSpaceManagementDO = new ParkingSpaceManagementDO();
                parkingSpaceManagementDO.setContractNumber(contractNumber);
                parkingSpaceManagementDO.setParkingSpaceCode(parkingSpaceCode);
                parkingSpaceManagementDO.setAuthorizedPersonName(authorizedPersonName);
                parkingSpaceManagementDO.setAuthorizedPersonPhone(authorizedPersonPhone);
                parkingSpaceManagementDO.setAuthorizedPersonIdNumber(authorizedPersonIdNumber);
                parkingSpaceManagementDO.setStartTime(startTime);
                parkingSpaceManagementDO.setLicensePlateNumber(licensePlateNumber);
                parkingSpaceManagementDO.setVehicleOriginalType(vehicleOriginalType);
                parkingSpaceManagementDO.setLicensePlateColor(licensePlateColor);
                parkingSpaceManagementDO.setLicensePlateType(licensePlateType);
                parkingSpaceManagementDO.setOrganizationId(organizationId);
                parkingSpaceManagementDO.setOrganizationName(organizationName);
                parkingSpaceManagementDO.setRemarks(remarks);
                parkingSpaceManagementDO.setSurname(surname);
                parkingSpaceManagementDO.setMobilePhone(mobilePhone);
                parkingSpaceManagementDO.setIdNumber(idNumber);
//                String uId = null;
//                if (null != idNumber){
//                    CustomerInfoDO customerInfoDO = customerInfoDAO.findByIdNumber(idNumber);
//                    if (null == customerInfoDO){
//                        Integer sex = Integer.valueOf(RegexValidateCard.validateIdCard(idNumber));
//                        uId = DateUtil.timeStamp() + Tools.random(10, 99);
//                        System.out.println("添加用户");
//                        iParkingSpaceManagementDAO.addCustomer(uId, surname , idNumber, sex, mobilePhone);
//                    }else {
//                        uId = customerInfoDO.getUserId();
//                    }
//                }
                    ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findAllByParkingSpaceCode(parkingSpaceManagementDO.getParkingSpaceCode());
                    parkingSpaceManagementDO.setVillageCode(parkingSpaceInfoDO.getVillageCode());
                    parkingSpaceManagementDO.setVillageName(parkingSpaceInfoDO.getVillageName());
                    parkingSpaceManagementDO.setRegionCode(parkingSpaceInfoDO.getRegionCode());
                    parkingSpaceManagementDO.setRegionName(parkingSpaceInfoDO.getRegionName());
                    parkingSpaceManagementDO.setBuildingCode(parkingSpaceInfoDO.getBuildingCode());
                    parkingSpaceManagementDO.setBuildingName(parkingSpaceInfoDO.getBuildingName());
//                    parkingSpaceManagementDO.setCustomerUserId(uId);
                    parkingSpaceManagementDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    parkingSpaceManagementDO.setLoggedOffState(0);

                parkingSpaceManagementDAO.save(parkingSpaceManagementDO);
                if (parkingSpaceInfoDO.getParkingSpaceNature().equals("2")){
                    iParkingSpaceInfoDAO.rentNuberLess(parkingSpaceInfoDO.getRegionCode());
                    iParkingSpaceInfoDAO.properNuberAdd(parkingSpaceInfoDO.getRegionCode());
                }
                LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                log.setCode(i+2);
                log.setResult("成功");
                log.setParkingSpaceCode(parkingSpaceCode);
                log.setOrganizationId(organizationId);
                log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logParkingSpaceManagementInfoDOS.add(log);
                importCount = importCount + 1;

            } catch (NumberFormatException e) {
                LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                log.setCode(i+2);
                log.setParkingSpaceCode(parkingSpaceCode);
                log.setResult("数据格式错误，请填入对应数字或输入格式正确的数据");
                log.setOrganizationId(organizationId);
                log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logParkingSpaceManagementInfoDOS.add(log);
            } catch (NullPointerException e) {
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult(" ");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
            } catch (IndexOutOfBoundsException e) {
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("此房间无业主");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
            } catch (DataIntegrityViolationException e) {
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i + 2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("时间格式错误");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
            }catch (CustomerException e){
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i + 2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("身份证号错误");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
            }catch (Exception e){
                    e.printStackTrace();
                    LogParkingSpaceManagementInfoDO log = new LogParkingSpaceManagementInfoDO();
                    log.setCode(i+2);
                    log.setParkingSpaceCode(parkingSpaceCode);
                    log.setResult("处理异常");
                    log.setOrganizationId(organizationId);
                    log.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceManagementInfoDOS.add(log);
            }

        }
        logParkingSpaceManagementDAO.saveAll(logParkingSpaceManagementInfoDOS);
        Map<String, Object> map = new HashMap<>();
        map.put("totalNumber", list.size()); //导入数据条数
        map.put("realNumber", importCount); //导入成功条数
        map.put("logParkingSpaceManagementInfoDOS", logParkingSpaceManagementInfoDOS);//日志列表
        System.out.println("ok");
        return map;
    }

}




