package com.rbi.admin.service.connect;


import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.ExcelDAO;
import com.rbi.admin.dao.ParkingSpaceManagementDAO;
import com.rbi.admin.dao.RegionDAO;
import com.rbi.admin.dao.connect.OwnerDAO;
import com.rbi.admin.dao.connect.RoomInfoDAO;
import com.rbi.admin.dao.structure.StructureDAO;
import com.rbi.admin.entity.edo.FloorDO;
import com.rbi.admin.entity.edo.ParkingSpaceManagementDO;
import com.rbi.admin.entity.edo.RegionDO;

import com.rbi.admin.entity.dto.Owner2DTO;
import com.rbi.admin.entity.dto.OwnerDTO;
import com.rbi.admin.entity.dto.RoomInfoDTO;
import com.rbi.admin.entity.dto.structure.*;
import com.rbi.admin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OwnerService {
    @Autowired(required = false)
    OwnerDAO ownerDAO;
    @Autowired(required = false)
    StructureDAO structureDAO;
    @Autowired(required = false)
    RegionDAO regionDAO;
    @Autowired(required = false)
    ExcelDAO excelDAO;
    @Autowired(required = false)
    RoomInfoDAO roomInfoDAO;
    @Autowired(required = false)
    BindTool bindTool;
    @Autowired
    ParkingSpaceManagementDAO parkingSpaceManagementDAO;


    public PageData findVillageByPage(String villageCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = ownerDAO.findVillageByPage(villageCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findVillageNum(villageCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findRegionByPage(String regionCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = ownerDAO.findRegionByPage(regionCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findRegionNum(regionCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findBuildingByPage(String buildingCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = ownerDAO.findBuildingByPage(buildingCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findBuildingNum(buildingCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findUnitByPage(String unitCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = ownerDAO.findUnitByPage(unitCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findUnitNum(unitCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findRoomByPage(String roomCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = ownerDAO.findRoomByPage(roomCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findRoomNum(roomCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }


    public PageData findOwnerByRoomCode(String roomCode,String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String roomCode2 = "'%" + roomCode + "%'";
        List<OwnerDTO> ownerDTOS = ownerDAO.findOwnerByRoomCode(roomCode2,organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findOwnerByRoomCodeNum(roomCode2,organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }



    public PageData findOwnerByPhone(String mobilePhone, String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String phone = "'%" + mobilePhone + "%'";
        List<OwnerDTO> ownerDTOS = ownerDAO.findOwnerByPhone(phone, organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findOwnerByPhoneNum(phone, organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }

    public PageData findOwnerByIdNumber(String idNumber,String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String idNumber2 = "'%" + idNumber + "%'";
        List<OwnerDTO> ownerDTOS = ownerDAO.findOwnerByIdNumber(idNumber2,organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findOwnerByIdNumberNum(idNumber2,organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }

    public PageData findOwnerBySurname(String idNumber,String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String idNumber2 = "'%" + idNumber + "%'";
        List<OwnerDTO> ownerDTOS = ownerDAO.findOwnerBySurname(idNumber2,organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = ownerDAO.findOwnerBySurnameNum(idNumber2,organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }



    public List<OwnerDTO> findAllCustomerByRoomCode(String roomCode) {
        List<OwnerDTO> ownerDTOS = ownerDAO.findAllCustomerByRoomCode(roomCode);
        return ownerDTOS;
    }


    public Map<String, Object> findOwnerRoomDetail(String organizationId,String roomCode, String customerUserId) {
        Map<String, Object> map = new HashMap<>();
        OwnerDTO ownerDTO  = ownerDAO.findOwnerByCodeAndUserId(roomCode, customerUserId);
        List<OwnerDTO> ownerDTO1 = ownerDAO.findRoomByCustomerUserId(customerUserId);
        List<ParkingSpaceManagementDO> parkingSpaceManagementDOList = parkingSpaceManagementDAO.findByOrganizationIdAndCustomerUserId(organizationId,customerUserId);

        for (int i = 0;i<ownerDTO1.size();i++){
            int num = ownerDAO.findCustomerNum(ownerDTO1.get(i).getRoomCode());
            if (num==0){
                ownerDTO1.get(i).setRentStatus("未出租");
            }else {
                ownerDTO1.get(i).setRentStatus("已出租");
            }
        }
        map.put("owner", ownerDTO);
        map.put("roomInfo", ownerDTO1);
        map.put("parkingSpaceManagementDOS",parkingSpaceManagementDOList);
        return map;
    }

    public Map<String, Object> findToUpdate(String roomCode) {
        Map<String, Object> map = new HashMap<>();
        RoomInfoDTO roomInfoDTO = ownerDAO.findRoomInfoByRoomCode(roomCode);

        Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTO.getFloorCode());
        roomInfoDTO.setFloor(floor);

        List<Owner2DTO> owner2DTO = ownerDAO.findOwnerInfoByRoomCode(roomCode);

        map.put("roomInfo", roomInfoDTO);
        map.put("owner", owner2DTO);
        return map;
    }

    /**
    * 其他页面接口
    * */
    public List<CustomerNameDTO> findCustomerByRoomCode(String roomCode,String organizationId) {
        List<CustomerNameDTO> customerNameDTOS = ownerDAO.findCustomerByRoomCode(roomCode,organizationId);
        List<CustomerNameDTO> customerNameDTO = ownerDAO.findIdNumberByOrganizationId(organizationId);
        for (int i = 0;i<customerNameDTO.size();i++){
            customerNameDTOS.add(customerNameDTO.get(i));
        }
        return customerNameDTOS;
    }

    /**业主信息添加修改
     * */
    public String addOwner(JSONObject json, String userId) throws Exception {
        RoomInfoDTO roomInfoDTO = JSON.toJavaObject(json.getJSONObject("roomInfo"), RoomInfoDTO.class);
        JSONArray jsonArray = json.getJSONArray("owner");
        if (jsonArray.size()>0){
            roomInfoDTO.setRoomStatus(2);
        }else {
            roomInfoDTO.setRoomStatus(1);
        }
        String organizationId = structureDAO.findOrganizationId(userId);
        String organizationName = structureDAO.findOrganizationName(userId);
        String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        String villageName = roomInfoDTO.getVillageName();
        String regionName = roomInfoDTO.getRegionName();
        String buildingName = roomInfoDTO.getBuildingName();
        String unitName = roomInfoDTO.getUnitName();
        Integer floor1 = roomInfoDTO.getFloor();
        String roomCode2 = roomInfoDTO.getRoomCode();
        Integer roomType = roomInfoDTO.getRoomType();
        Double roomSize = roomInfoDTO.getRoomSize();
        Integer roomStatus = roomInfoDTO.getRoomStatus();
        Integer renovationStatus = roomInfoDTO.getRenovationStatus();
        String renovationStartTime = roomInfoDTO.getRenovationStartTime();
        String renovationDeadline = roomInfoDTO.getRenovationDeadline();

        String startBillingTime = roomInfoDTO.getStartBillingTime();
        String realRecyclingHomeTime = roomInfoDTO.getRealRecyclingHomeTime();

        if (null == roomSize || null == roomType || null == roomStatus){
            return "房间大小、房间类型、房间状态不能为空";
        }
        if (roomType == 1){
            if ("" == villageName || "" ==regionName ||"" == buildingName || "" == unitName || "" == roomCode2){
                return "添加住宅时 小区名、地块名、楼栋名、单元、楼层名、房间编号不能为空";
            }
            if (null == villageName || null == regionName|| null == buildingName|| null == unitName|| null == floor1 || null == roomCode2){
                return "添加住宅时 小区名、地块名、楼栋名、单元、楼层名、楼层、房间编号不能为空";
            }
        }
        if (roomType == 3){
            if ("" == villageName || "" ==regionName || "" == roomCode2){
                return "添加小区时 小区名、地块名、房间编号不能为空";
            }
            if (null == villageName || null == regionName|| null == roomCode2){
                return "添加小区时 小区名、地块名、房间编号不能为空";
            }
        }
        if(jsonArray.size() != 0 &&  null==startBillingTime && null == realRecyclingHomeTime){
            return "添加业主时 交房时间、开始计费时间不能为空";
        }
        if(jsonArray.size() != 0 &&  startBillingTime=="" && realRecyclingHomeTime == ""){
            return "添加业主时 交房时间、开始计费时间不能为空";
        }
        if ("".equals(renovationStartTime)) {
            renovationStartTime = null;
        }
        if ("".equals(renovationDeadline)) {
            renovationDeadline = null;
        }

        if (null == buildingName || "".equals(buildingName)){
            buildingName = "X";
        }
        if (null == unitName || "".equals(unitName)){
            unitName = "X";
        }

        String floor = "";
        if (null == roomInfoDTO.getFloor()){
            floor = "X";
        }else {
            floor = roomInfoDTO.getFloor().toString();
        }


        String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();
        String regionCode = null;
        RegionDO regionDO1 = regionDAO.findByVillageCodeAndRegionName(villageCode, regionName);
        if (null != regionDO1) {
            regionCode = regionDO1.getRegionCode();
        } else {
            if ("".equals(Tools.filteringChinese(regionName))) {
                for (int k = 0; ; k++) {
                    regionCode = villageCode + "-" + PinYin.getPinYinHeadChar(regionName).toUpperCase() + "0" + new Random().nextInt(10);
                    if (null == regionDAO.findAllByRegionCodeAndVillageCode(regionCode, villageCode)) {
                        break;
                    }
                    if (100 == k) {
                        return "1002";
                    }
                }
            } else {
                regionCode = villageCode + "-" + Tools.filteringChinese(regionName);
                for (int k = 0; ; k++) {
                    if (null == regionDAO.findAllByRegionCodeAndVillageCode(regionCode, villageCode)) {
                        break;
                    }
                    regionCode = villageCode + "-" + Tools.filteringChinese(regionName) + new Random().nextInt(10);
                    if (k == 100) {
                        return "1002";
                    }
                }
            }
        }
        String buildingCode = regionCode + "-" + Tools.filteringChinese(buildingName);
        if (roomType == 3){
            unitName = "("+floor+")层";
        }
        System.out.println("单元名称："+unitName);
        String unitCode = buildingCode + "-" + Tools.filteringChinese(unitName);
        String floorCode = unitCode +"-"+ Tools.filteringChinese(String.valueOf(floor));
        if (roomType == 3){
            floorCode = unitCode +"-"+ Tools.filteringChinese(unitName);
        }
        String roomCode = unitCode + "-" + roomCode2;

        System.out.println("小区进行");
        List<RegionStructureDTO> regionStructureDTOS = structureDAO.findRegion2(regionCode);
        if (regionStructureDTOS.size() == 0) {
            System.out.println("添加新地块*" + regionCode);
            excelDAO.addRegion(villageCode, regionCode, regionName);
        }
        List<BuildingStructureDTO> buildingStructureDTOS = structureDAO.findBuiding2(buildingCode);
        if (buildingStructureDTOS.size() == 0) {
            System.out.println("添加新楼栋*:" + buildingCode);
            excelDAO.addBuilding(regionCode, buildingCode, buildingName);
        }
        List<UnitStructureDTO> unitStructureDTOS = structureDAO.findUnit2(unitCode);
        if (unitStructureDTOS.size() == 0) {
            System.out.println("添加新单元*:" + unitCode);
            excelDAO.addUnit(buildingCode, unitCode, unitName);
        }

        List<FloorDO> floorDOS = structureDAO.findFloor2(floorCode);
        if (floorDOS.size() == 0) {
            System.out.println("添加新楼层*:" + floorCode);
            if (floor.equals("X")){
                excelDAO.addFloor2(floorCode,unitCode);
            }else {
                excelDAO.addFloor(floorCode, Integer.valueOf(floor), unitCode);
            }
        }

        List<RoomStructureDTO> roomStructureDTOS = structureDAO.findRoom2(roomCode);
        if (roomStructureDTOS.size() == 0) {
            System.out.println(roomCode + "添加新房屋");
            excelDAO.addRoom(unitCode, roomCode, roomSize, roomType, roomStatus, renovationStatus, renovationStartTime, renovationDeadline, startBillingTime ,realRecyclingHomeTime,floorCode);
        }else {
            System.out.println("修改房屋");
            excelDAO.updateRoom(roomSize, roomType, roomStatus, renovationStatus, renovationStartTime, renovationDeadline,startBillingTime,realRecyclingHomeTime,floorCode, roomCode);
        }
        //绑定收费项目
        bindTool.bindChargeItem(organizationId, roomCode, roomType, villageCode, villageName, regionCode, regionName, buildingCode, buildingName, unitCode, unitName, idt, organizationName);
        if (jsonArray.size()==0){
            return "1000";
        }
        String status = bindTool.addOwner(jsonArray, roomCode, organizationId,startBillingTime,realRecyclingHomeTime);
        if (status.equals("10001")){
            return "业主信息除备注外不能为空";
        }else if (status.equals("10002")){
            return "身份证号格式错误";
        }else if(status.equals("10003")){
            return "电话格式错误";
        }else {
            return "1000";
        }
        //绑定收费项目结束
    }



    /**空置房添加修改
 * */
    public String addOwnerNew(JSONObject json, String userId) throws Exception {
        RoomInfoDTO roomInfoDTO = JSON.toJavaObject(json.getJSONObject("roomInfo"), RoomInfoDTO.class);
        JSONArray jsonArray = json.getJSONArray("owner");
        if (jsonArray.size()>0){
            roomInfoDTO.setRoomStatus(2);
        }else {
            roomInfoDTO.setRoomStatus(1);
        }
        String organizationId = structureDAO.findOrganizationId(userId);
        String organizationName = structureDAO.findOrganizationName(userId);
        String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        String villageName = roomInfoDTO.getVillageName();
        String regionName = roomInfoDTO.getRegionName();
        String buildingName = roomInfoDTO.getBuildingName();
        String unitName = roomInfoDTO.getUnitName();
        Integer floor1 = roomInfoDTO.getFloor();
        String roomCode2 = roomInfoDTO.getRoomCode();
        Integer roomType = roomInfoDTO.getRoomType();
        Double roomSize = roomInfoDTO.getRoomSize();
        Integer roomStatus = roomInfoDTO.getRoomStatus();
        Integer renovationStatus = roomInfoDTO.getRenovationStatus();
        String renovationStartTime = roomInfoDTO.getRenovationStartTime();
        String renovationDeadline = roomInfoDTO.getRenovationDeadline();

        String startBillingTime = roomInfoDTO.getStartBillingTime();
        String realRecyclingHomeTime = roomInfoDTO.getRealRecyclingHomeTime();

        if (null == roomSize || null == roomType || null == roomStatus){
            return "房间大小、房间类型、房间状态不能为空";
        }
        if (roomType == 1){
            if ("" == villageName || "" ==regionName ||"" == buildingName || "" == unitName || "" == roomCode2){
                return "添加住宅时 小区名、地块名、楼栋名、单元、楼层名、房间编号不能为空";
            }
            if (null == villageName || null == regionName|| null == buildingName|| null == unitName|| null == floor1 || null == roomCode2){
                return "添加住宅时 小区名、地块名、楼栋名、单元、楼层名、楼层、房间编号不能为空";
            }
        }
        if (roomType == 3){
            if ("" == villageName || "" ==regionName || "" == roomCode2){
                return "添加小区时 小区名、地块名、房间编号不能为空";
            }
            if (null == villageName || null == regionName|| null == roomCode2){
                return "添加小区时 小区名、地块名、房间编号不能为空";
            }
        }
        if(jsonArray.size() != 0 &&  null==startBillingTime && null == realRecyclingHomeTime){
            return "添加业主时 交房时间、开始计费时间不能为空";
        }
        if(jsonArray.size() != 0 &&  startBillingTime=="" && realRecyclingHomeTime == ""){
            return "添加业主时 交房时间、开始计费时间不能为空";
        }
        if ("".equals(renovationStartTime)) {
            renovationStartTime = null;
        }
        if ("".equals(renovationDeadline)) {
            renovationDeadline = null;
        }

        if (null == buildingName || "".equals(buildingName)){
            buildingName = "X";
        }
        if (null == unitName || "".equals(unitName)){
            unitName = "X";
        }

        String floor = "";
        if (null == roomInfoDTO.getFloor()){
            floor = "X";
        }else {
            floor = roomInfoDTO.getFloor().toString();
        }


        String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();
        String regionCode = null;
        RegionDO regionDO1 = regionDAO.findByVillageCodeAndRegionName(villageCode, regionName);
        if (null != regionDO1) {
            regionCode = regionDO1.getRegionCode();
        } else {
            if ("".equals(Tools.filteringChinese(regionName))) {
                for (int k = 0; ; k++) {
                    regionCode = villageCode + "-" + PinYin.getPinYinHeadChar(regionName).toUpperCase() + "0" + new Random().nextInt(10);
                    if (null == regionDAO.findAllByRegionCodeAndVillageCode(regionCode, villageCode)) {
                        break;
                    }
                    if (100 == k) {
                        return "1002";
                    }
                }
            } else {
                regionCode = villageCode + "-" + Tools.filteringChinese(regionName);
                for (int k = 0; ; k++) {
                    if (null == regionDAO.findAllByRegionCodeAndVillageCode(regionCode, villageCode)) {
                        break;
                    }
                    regionCode = villageCode + "-" + Tools.filteringChinese(regionName) + new Random().nextInt(10);
                    if (k == 100) {
                        return "1002";
                    }
                }
            }
        }
        String buildingCode = regionCode + "-" + Tools.filteringChinese(buildingName);
        if (roomType == 3){
            unitName = "("+floor+")层";
        }
        System.out.println("单元名称："+unitName);
        String unitCode = buildingCode + "-" + Tools.filteringChinese(unitName);
        String floorCode = unitCode +"-"+ Tools.filteringChinese(String.valueOf(floor));
        if (roomType == 3){
            floorCode = unitCode +"-"+ Tools.filteringChinese(unitName);
        }
        String roomCode = unitCode + "-" + roomCode2;

        System.out.println("小区进行");
        List<RegionStructureDTO> regionStructureDTOS = structureDAO.findRegion2(regionCode);
        if (regionStructureDTOS.size() == 0) {
            System.out.println("添加新地块*" + regionCode);
            excelDAO.addRegion(villageCode, regionCode, regionName);
        }
        List<BuildingStructureDTO> buildingStructureDTOS = structureDAO.findBuiding2(buildingCode);
        if (buildingStructureDTOS.size() == 0) {
            System.out.println("添加新楼栋*:" + buildingCode);
            excelDAO.addBuilding(regionCode, buildingCode, buildingName);
        }
        List<UnitStructureDTO> unitStructureDTOS = structureDAO.findUnit2(unitCode);
        if (unitStructureDTOS.size() == 0) {
            System.out.println("添加新单元*:" + unitCode);
            excelDAO.addUnit(buildingCode, unitCode, unitName);
        }

        List<FloorDO> floorDOS = structureDAO.findFloor2(floorCode);
        if (floorDOS.size() == 0) {
            System.out.println("添加新楼层*:" + floorCode);
            if (floor.equals("X")){
                excelDAO.addFloor2(floorCode,unitCode);
            }else {
                excelDAO.addFloor(floorCode, Integer.valueOf(floor), unitCode);
            }
        }

        List<RoomStructureDTO> roomStructureDTOS = structureDAO.findRoom2(roomCode);
        if (roomStructureDTOS.size() == 0) {
            System.out.println(roomCode + "添加新房屋");
            excelDAO.addRoom(unitCode, roomCode, roomSize, roomType, roomStatus, renovationStatus, renovationStartTime, renovationDeadline, startBillingTime ,realRecyclingHomeTime,floorCode);
        }else {
            System.out.println("修改房屋");
            excelDAO.updateRoom(roomSize, roomType, roomStatus, renovationStatus, renovationStartTime, renovationDeadline,startBillingTime,realRecyclingHomeTime,floorCode, roomCode);
        }
        //绑定收费项目
        bindTool.bindChargeItem(organizationId, roomCode, roomType, villageCode, villageName, regionCode, regionName, buildingCode, buildingName, unitCode, unitName, idt, organizationName);
        if (jsonArray.size()==0){
            return "1000";
        }
        String status = bindTool.addOwner(jsonArray, roomCode, organizationId,startBillingTime,realRecyclingHomeTime);
        if (status.equals("10001")){
            return "业主信息除备注外不能为空";
        }else if (status.equals("10002")){
            return "身份证号格式错误";
        }else if(status.equals("10003")){
            return "电话格式错误";
        }else {
            return "1000";
        }
        //绑定收费项目结束
    }

}
