package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.RegionCodeDTO;
import com.rbi.interactive.service.ParkingSpaceService;
import com.rbi.interactive.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    @Autowired
    ParkingSpaceInfoDAO parkingSpaceInfoDAO;
    @Autowired(required = false)
    IParkingSpaceInfoDAO iParkingSpaceInfoDAO;
    @Autowired
    UserInfoDAO userInfoDAO;
    @Autowired
    OrganizationDAO organizationDAO;
    @Autowired
    VillageDAO villageDAO;
    @Autowired
    RegionDAO regionDAO;
    @Autowired
    BuildingDAO buildingDAO;
    @Autowired
    LogParkingSpaceInfoDAO logParkingSpaceInfoDAO;
    @Autowired
    ParkingSpaceManagementDAO parkingSpaceManagementDAO;


    @Override
    public void leadingIn(JSONObject jsonObject, String userId) {

    }

    @Override
    public String add(JSONObject json, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        ParkingSpaceInfoDO parkingSpaceInfoDO = JSONObject.parseObject(json.toJSONString(),ParkingSpaceInfoDO.class);

        VillageDO villageDO = villageDAO.findByOrganizationIdAndVillageCodeAndEnable(userInfoDO.getOrganizationId(),parkingSpaceInfoDO.getVillageCode(),1);
        RegionDO regionDO = regionDAO.findAllByRegionCodeAndVillageCode(parkingSpaceInfoDO.getRegionCode(),parkingSpaceInfoDO.getVillageCode());
        parkingSpaceInfoDO.setVillageName(villageDO.getVillageName());
        parkingSpaceInfoDO.setRegionName(regionDO.getRegionName());

        if (null == parkingSpaceInfoDO.getVillageCode() ||
                null == parkingSpaceInfoDO.getRegionCode() ||
                "".equals(parkingSpaceInfoDO.getVillageCode()) ||
                "".equals(parkingSpaceInfoDO.getRegionCode())){
            return "请先选择地块或楼栋";
        }
        if (null == parkingSpaceInfoDO.getParkingSpacePlace() ||
                null == parkingSpaceInfoDO.getParkingSpaceCode() ||
                null == parkingSpaceInfoDO.getParkingSpaceNature() ||
                null == parkingSpaceInfoDO.getParkingSpaceType() ||
                null == parkingSpaceInfoDO.getVehicleCapacity()||
                "".equals(parkingSpaceInfoDO.getParkingSpacePlace()) ||
                "".equals(parkingSpaceInfoDO.getParkingSpaceCode()) ||
                "".equals(parkingSpaceInfoDO.getParkingSpaceNature()) ||
                "".equals(parkingSpaceInfoDO.getParkingSpaceType()) ||
                "".equals(parkingSpaceInfoDO.getVehicleCapacity())){
            return "必填字段不能为空";
        }

        String buildingName = null;
        if ("".equals(parkingSpaceInfoDO.getBuildingCode()) || null==parkingSpaceInfoDO.getBuildingCode()){
            buildingName="X";
        }else {
            BuildingDO buildingDO = buildingDAO.findByBuildingCodeAndRegionCode(parkingSpaceInfoDO.getBuildingCode(),parkingSpaceInfoDO.getRegionCode());
            buildingName = buildingDO.getBuildingName();
            parkingSpaceInfoDO.setBuildingName(buildingName);
        }
        String floor = null;
        if ("".equals(parkingSpaceInfoDO.getFloor()) || null==parkingSpaceInfoDO.getFloor()) {
            floor = "X";
        }else {
            floor = parkingSpaceInfoDO.getFloor();
        }
        String parkingSpaceCode=null;
        if ("".equals(parkingSpaceInfoDO.getFloor()) || null==parkingSpaceInfoDO.getFloor()){
            parkingSpaceCode = parkingSpaceInfoDO.getRegionCode()+"-"+ Tools.filteringChinese(buildingName)+"-"+
                    floor+"-"+parkingSpaceInfoDO.getParkingSpaceCode();
        }else {
            parkingSpaceCode = parkingSpaceInfoDO.getRegionCode()+"-"+ Tools.filteringChinese(buildingName)+"-("+
                    floor+")-"+parkingSpaceInfoDO.getParkingSpaceCode();
        }
        if(parkingSpaceInfoDAO.countAllByParkingSpaceCode(parkingSpaceCode)!=0){
            return "车位编号重复！";
        }

        if (null == parkingSpaceInfoDO.getCurrentCapacity() || "".equals(parkingSpaceInfoDO.getCurrentCapacity())){
            parkingSpaceInfoDO.setCurrentCapacity(parkingSpaceInfoDO.getVehicleCapacity());
        }
        parkingSpaceInfoDO.setParkingSpaceCode(parkingSpaceCode);
        parkingSpaceInfoDO.setOrganizationId(organizationId);
        parkingSpaceInfoDO.setOrganizationName(organizationDO.getOrganizationName());
        parkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        parkingSpaceInfoDAO.save(parkingSpaceInfoDO);

        int num = iParkingSpaceInfoDAO.findNumByRegionCode(parkingSpaceInfoDO.getRegionCode());
        if (num == 0){
            iParkingSpaceInfoDAO.addRegionCode(organizationId,parkingSpaceInfoDO.getRegionCode());
        }
        if (parkingSpaceInfoDO.getParkingSpaceNature().equals("1")){
            iParkingSpaceInfoDAO.regionNuberAdd(parkingSpaceInfoDO.getRegionCode());
            iParkingSpaceInfoDAO.properNuberAdd(parkingSpaceInfoDO.getRegionCode());
        }
        if (parkingSpaceInfoDO.getParkingSpaceNature().equals("2")){
            iParkingSpaceInfoDAO.regionNuberAdd(parkingSpaceInfoDO.getRegionCode());
            iParkingSpaceInfoDAO.rentNuberAdd(parkingSpaceInfoDO.getRegionCode());
        }
        return "10000";
    }

    @Override
    public void update(JSONObject jsonObject,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        ParkingSpaceInfoDO parkingSpaceInfoDO = JSONObject.parseObject(jsonObject.toJSONString(),ParkingSpaceInfoDO.class);
        parkingSpaceInfoDO.setOrganizationId(organizationId);
        parkingSpaceInfoDO.setOrganizationName(organizationDO.getOrganizationName());
        parkingSpaceInfoDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));

        String nature = iParkingSpaceInfoDAO.findNatureByCode(parkingSpaceInfoDO.getParkingSpaceCode());
        if (nature.equals("1") && parkingSpaceInfoDO.getParkingSpaceNature().equals("2")){
            iParkingSpaceInfoDAO.properNuberLess(parkingSpaceInfoDO.getRegionCode());
            iParkingSpaceInfoDAO.rentNuberAdd(parkingSpaceInfoDO.getRegionCode());

            ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findByParkingSpaceCode(parkingSpaceInfoDO.getParkingSpaceCode());
            parkingSpaceManagementDAO.deleteById(parkingSpaceManagementDO.getParkingSpaceManagementId());
        }
        if (nature.equals("2") && parkingSpaceInfoDO.getParkingSpaceNature().equals("1")){
            iParkingSpaceInfoDAO.rentNuberLess(parkingSpaceInfoDO.getRegionCode());
            iParkingSpaceInfoDAO.properNuberAdd(parkingSpaceInfoDO.getRegionCode());
        }
        parkingSpaceInfoDAO.saveAndFlush(parkingSpaceInfoDO);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        for (int i = 0; i<ids.size(); i++){
            ParkingSpaceInfoDO parkingSpaceInfoDO = parkingSpaceInfoDAO.findByParkingSpaceInfoId(ids.get(i));
            iParkingSpaceInfoDAO.regionNuberLess(parkingSpaceInfoDO.getRegionCode());
            if (parkingSpaceInfoDO.getParkingSpaceNature().equals("1")){
                iParkingSpaceInfoDAO.properNuberLess(parkingSpaceInfoDO.getRegionCode());
            }
            if (parkingSpaceInfoDO.getParkingSpaceNature().equals("2")){
                iParkingSpaceInfoDAO.rentNuberLess(parkingSpaceInfoDO.getRegionCode());
            }
            parkingSpaceInfoDAO.deleteById(ids.get(i));
            ParkingSpaceManagementDO parkingSpaceManagementDO = parkingSpaceManagementDAO.findByParkingSpaceCode(parkingSpaceInfoDO.getParkingSpaceCode());
            parkingSpaceManagementDAO.deleteById(parkingSpaceManagementDO.getParkingSpaceManagementId());
        }

    }


    @Override
    public PageData findFirstByPage(int pageNum, int pageSize, String organizationId) {
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationId(organizationId);
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceInfoDAO.findVillageNum(villageDOS.get(0).getVillageCode());
        List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = iParkingSpaceInfoDAO.findVillage(villageDOS.get(0).getVillageCode(),pageNo,pageSize);
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
        int count = iParkingSpaceInfoDAO.findVillageNum(villageCode);
        List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = iParkingSpaceInfoDAO.findVillage(villageCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceInfoDOS);
    }

    @Override
    public PageData findRegion(String regionCode, int pageNum, int pageSize) {
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceInfoDAO.findRegionNum(regionCode);
        List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = iParkingSpaceInfoDAO.findRegion(regionCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceInfoDOS);
    }

    @Override
    public PageData findBuilding(String buildingCode, int pageNum, int pageSize) {
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceInfoDAO.findBuildingNum(buildingCode);
        List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = iParkingSpaceInfoDAO.findBuilding(buildingCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceInfoDOS);
    }

    @Override
    public PageData findParkingSpaceCode(String ParkinSpaceCode, int pageNum, int pageSize) {
        int pageNo = (pageNum-1)*pageSize;
        int count = iParkingSpaceInfoDAO.findParkingCodeNum(ParkinSpaceCode);
        List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = iParkingSpaceInfoDAO.findParkingSpaceCode(ParkinSpaceCode,pageNo,pageSize);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,parkingSpaceInfoDOS);
    }


    @Override
    public String findOrganizationId(String userId) {
            String organizationId = userInfoDAO.findOrganizationId(userId);
            return organizationId;
        }


    @Override
    public Map<String,Object> excelImport(MultipartFile file, String userId) throws IOException {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        String organizationName = organizationDO.getOrganizationName();
        List<String> list = POIUtil.readExcel(file, 13);
        List<LogParkingSpaceInfoDO> logParkingSpaceInfoDOS = new ArrayList<>();
        int importCount = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                List<String> list1 = Arrays.asList(StringUtils.split(list.get(i), ","));
                String villageName = list1.get(0);
                String regionName = list1.get(1);
                String buildingName = list1.get(2);
                String floor = list1.get(3);
                String parkingSpaceCode = list1.get(4);
                Integer parkingSpacePlace = Integer.parseInt(list1.get(5));
                String parkingSpaceType = list1.get(6);
                String parkingSpaceNature = list1.get(7);
                String parkingSpaceArea1 = list1.get(8);
                String vehicleCapacity1 = list1.get(9);
                String currentCapacity1 = list1.get(10);
                String remarks2 = list1.get(11);
                int num = iParkingSpaceInfoDAO.findVillageNumByVillageName(villageName);
                if (num == 0) {
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("不存在的小区名:" + villageName);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    System.out.println("不存在的小区名:" + villageName);
                    continue;
                }
                String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();
                int num2 = iParkingSpaceInfoDAO.findRegionNumByRegionName(regionName,villageCode);
                if (num2 == 0) {
                    System.out.println("不存在的地块名:" + regionName);
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("不存在的地块名:" + regionName);
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    continue;
                }
                RegionDO regionDO = regionDAO.findByRegionNameAndVillageCode(regionName,villageCode);
                String regionCode = regionDO.getRegionCode();
                String buildingCode = null;
                if (buildingName.equals("blank")) {
                    buildingName = null;
                    buildingCode = null;
                } else {
                    BuildingDO buildingDO = buildingDAO.findByBuildingNameAndRegionCode(buildingName,regionCode);
                    if ( null != buildingDO) {
                        buildingCode = buildingDO.getBuildingCode();
                    } else {
                        System.out.println("不存在的楼栋名:" + buildingName);
                        LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                        logParkingSpaceInfoDO.setCode(i + 2);
                        logParkingSpaceInfoDO.setResult("失败");
                        logParkingSpaceInfoDO.setRemarks("不存在的楼栋名:" + buildingName);
                        logParkingSpaceInfoDO.setOrganizationId(organizationId);
                        logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                        continue;
                    }
                }
                if (floor.equals("blank")) {
                    floor = null;
                }
                if (parkingSpaceCode.equals("blank")) {
                    System.out.println("车位编号不能为空");
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("车位编号不能为空");
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    continue;
                }
                if (parkingSpacePlace.equals("blank")) {
                    System.out.println("车位地点不能为空");
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("车位地点不能为空");
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    continue;
                }
                if (parkingSpaceType.equals("blank")) {
                    System.out.println("车位类型不能为空");
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("车位类型不能为空");
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    continue;
                }
                if (parkingSpaceNature.equals("blank")) {
                    System.out.println("车位性质不能为空");
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("车位性质不能为空");
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    continue;
                }
                Double parkingSpaceArea = null;
                if (!parkingSpaceArea1.equals("blank")) {
                    parkingSpaceArea = Double.valueOf(parkingSpaceArea1);
                }
                if (vehicleCapacity1.equals("blank")) {
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("融车数量不能为空");
                    logParkingSpaceInfoDO.setOrganizationId(organizationId);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                }
                Integer vehicleCapacity = Integer.valueOf(vehicleCapacity1);
                Integer currentCapacity = null;
                if (!currentCapacity1.equals("blank")) {
                    currentCapacity = Integer.valueOf(currentCapacity1);
                }else {
                    currentCapacity = vehicleCapacity;
                }
                String remarks = null;
                if (!remarks2.equals("blank")){
                    remarks = remarks2;
                }

                ParkingSpaceInfoDO parkingSpaceInfoDO = new ParkingSpaceInfoDO();
                parkingSpaceInfoDO.setOrganizationId(organizationId);
                parkingSpaceInfoDO.setOrganizationName(organizationName);

                parkingSpaceInfoDO.setVillageName(villageName);
                parkingSpaceInfoDO.setVillageCode(villageCode);
                parkingSpaceInfoDO.setRegionName(regionName);
                parkingSpaceInfoDO.setRegionCode(regionCode);
                parkingSpaceInfoDO.setBuildingName(buildingName);
                parkingSpaceInfoDO.setBuildingCode(buildingCode);
                parkingSpaceInfoDO.setFloor(floor);
                parkingSpaceInfoDO.setParkingSpacePlace(parkingSpacePlace);
                parkingSpaceInfoDO.setParkingSpaceType(parkingSpaceType);
                parkingSpaceInfoDO.setParkingSpaceNature(parkingSpaceNature);
                parkingSpaceInfoDO.setParkingSpaceArea(parkingSpaceArea);
                parkingSpaceInfoDO.setVehicleCapacity(vehicleCapacity);
                parkingSpaceInfoDO.setCurrentCapacity(currentCapacity);
                parkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                parkingSpaceInfoDO.setRemarks(remarks);

                if (null == buildingName) {
                    buildingName = "X";
                }
                if (null == parkingSpaceInfoDO.getFloor()) {
                    floor = "X";
                }
                if (floor.equals("X")) {
                    parkingSpaceCode = parkingSpaceInfoDO.getRegionCode() + "-" + Tools.filteringChinese(buildingName) + "-" +
                            floor + "-" + parkingSpaceCode;
                } else {
                    parkingSpaceCode = parkingSpaceInfoDO.getRegionCode() + "-" + Tools.filteringChinese(buildingName) + "-(" +
                            floor + ")-" + parkingSpaceCode;
                }
                if (parkingSpaceInfoDAO.countAllByParkingSpaceCode(parkingSpaceCode) != 0) {
                    System.out.println("重复车位编号");
                    LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                    logParkingSpaceInfoDO.setCode(i + 2);
                    logParkingSpaceInfoDO.setResult("失败");
                    logParkingSpaceInfoDO.setRemarks("重复车位编号：" + parkingSpaceCode);
                    logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                    logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                    continue;
                }
                parkingSpaceInfoDO.setParkingSpaceCode(parkingSpaceCode);
                parkingSpaceInfoDAO.save(parkingSpaceInfoDO);
                LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                logParkingSpaceInfoDO.setCode(i + 2);
                logParkingSpaceInfoDO.setResult("成功");
                logParkingSpaceInfoDO.setParkingSpaceCodes(parkingSpaceCode);
                logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
                importCount = importCount + 1;

                /**
                 * 统计
                 * */
                int regionCodeNum = iParkingSpaceInfoDAO.findNumByRegionCode(parkingSpaceInfoDO.getRegionCode());
                if (regionCodeNum == 0){
                    iParkingSpaceInfoDAO.addRegionCode(organizationId,parkingSpaceInfoDO.getRegionCode());
                }
                if (parkingSpaceInfoDO.getParkingSpaceNature().equals("1")){
                    iParkingSpaceInfoDAO.regionNuberAdd(parkingSpaceInfoDO.getRegionCode());
                    iParkingSpaceInfoDAO.properNuberAdd(parkingSpaceInfoDO.getRegionCode());
                }
                if (parkingSpaceInfoDO.getParkingSpaceNature().equals("2")){
                    iParkingSpaceInfoDAO.regionNuberAdd(parkingSpaceInfoDO.getRegionCode());
                    iParkingSpaceInfoDAO.rentNuberAdd(parkingSpaceInfoDO.getRegionCode());
                }



            } catch (NumberFormatException e) {
                LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                logParkingSpaceInfoDO.setCode(i + 2);
                logParkingSpaceInfoDO.setResult("数据格式错误，请填入对应数字或输入格式正确的数据");
                logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
            } catch (NullPointerException e) {
                LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                logParkingSpaceInfoDO.setCode(i + 2);
                logParkingSpaceInfoDO.setResult("必填字段不能为空");
                logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
            }catch (Exception e){
                LogParkingSpaceInfoDO logParkingSpaceInfoDO = new LogParkingSpaceInfoDO();
                logParkingSpaceInfoDO.setCode(i + 2);
                logParkingSpaceInfoDO.setResult("处理异常");
                logParkingSpaceInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                logParkingSpaceInfoDOS.add(logParkingSpaceInfoDO);
            }
        }
            logParkingSpaceInfoDAO.saveAll(logParkingSpaceInfoDOS);
            Map<String, Object> map = new HashMap<>();
            map.put("totalNumber", list.size()); //导入数据条数
            map.put("realNumber", importCount); //导入成功条数
            map.put("logParkingSpaceInfoDOS", logParkingSpaceInfoDOS);//日志列表
            System.out.println("ok");
            return map;
        }






}
