package com.rbi.admin.service;

import com.rbi.admin.dao.ExcelDAO;
import com.rbi.admin.dao.VacantRoomDAO;
import com.rbi.admin.dao.connect.SystemSetDAO;
import com.rbi.admin.entity.dto.Owner2DTO;
import com.rbi.admin.entity.dto.RoomInfoDTO;
import com.rbi.admin.util.ExcelUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ConfigurationProperties(prefix="path")
@Data
@Service
public class ExcelService {

    private String excelPath;

    private String findExcelPath;

    @Autowired(required = false)
    ExcelDAO excelDAO;

    @Autowired(required = false)
    SystemSetDAO systemSetDAO;

    @Autowired(required = false)
    VacantRoomDAO vacantRoomDAO;

    public String OwnerExport(String level, String code, int identity1, String organizationId){
        try {
            String filepath = "";
            String name = "";
            String findPath = "";
            String sheetName = "sheet1";
            List<RoomInfoDTO> roomInfoDTOS = null;
            String identity = systemSetDAO.findNameBySettingCodeAndName(String.valueOf(identity1), "IDENTITY");
            if (level.equals("1")) {
                roomInfoDTOS = vacantRoomDAO.findVillageByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() + identity + "导出.xlsx";
            }
            if (level.equals("2")) {
                roomInfoDTOS = vacantRoomDAO.findRegionByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() + identity + "导出.xlsx";
            }
            if (level.equals("3")) {
                roomInfoDTOS = vacantRoomDAO.findBuildingByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() + identity + "导出.xlsx";
            }
            if (level.equals("4")) {
                roomInfoDTOS = vacantRoomDAO.findUnitByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() +
                        roomInfoDTOS.get(0).getUnitName() + identity + "导出.xlsx";
            }
            if (level.equals("5")) {
                roomInfoDTOS = vacantRoomDAO.findRoomByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() +
                        roomInfoDTOS.get(0).getUnitName() +
                        roomInfoDTOS.get(0).getRoomCode() + identity + "导出.xlsx";
            }
            filepath = excelPath+name;
            findPath = findExcelPath + name;


            List<String> titles = new ArrayList<>();
            titles.add("小区");
            titles.add("地块");
            titles.add("楼栋");
            titles.add("单元");
            titles.add("楼层");
            titles.add("房间号");
            titles.add("使用面积");
            titles.add("房间类型");
            titles.add("房间类型#");

            titles.add("房间状态");
            titles.add("房间状态#");

            titles.add("装修情况");
            titles.add("装修情况#");

            titles.add("装修开始时间");
            titles.add("装修结束时间");
            titles.add("姓名");
            titles.add("身份证号");
            titles.add("手机号");

            titles.add("身份");
            titles.add("身份#");

            titles.add("缴费状态");
            titles.add("缴费状态#");

            titles.add("物业费开始计费时间");
            titles.add("实际收房时间");
            titles.add("租房开始时间");
            titles.add("租房结束时间");


            titles.add("备注");
            List<Map<String, Object>> values = new ArrayList<>();

            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("小区", roomInfoDTOS.get(i).getVillageName());
                map.put("地块", roomInfoDTOS.get(i).getRegionName());
                map.put("楼栋", roomInfoDTOS.get(i).getBuildingName());
                map.put("单元", roomInfoDTOS.get(i).getUnitName());

                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                map.put("楼层",floor);

                String roomc = roomInfoDTOS.get(i).getRoomCode();
                String roomcc = roomc.substring(roomc.lastIndexOf("-")+1);
                map.put("房间号",roomcc);

                map.put("使用面积", roomInfoDTOS.get(i).getRoomSize());
                map.put("房间类型", roomInfoDTOS.get(i).getRoomType());
                map.put("房间类型#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRoomType()), "ROOM_TYPE"));

                map.put("房间状态", roomInfoDTOS.get(i).getRoomStatus());
                map.put("房间状态#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRoomStatus()), "ROOM_STATUS"));

                map.put("装修情况", roomInfoDTOS.get(i).getRenovationStatus());
                map.put("装修情况#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRenovationStatus()), "RENOVATION_STATUS"));

                String startTime = roomInfoDTOS.get(i).getRenovationStartTime();
                String endTime = roomInfoDTOS.get(i).getRenovationDeadline();
                if ("1999-09-09".equals(startTime)) {
                    startTime = null;
                }
                if ("1999-09-09".equals(endTime)) {
                    endTime = null;
                }
                map.put("装修开始时间", startTime);
                map.put("装修结束时间", endTime);

                List<Owner2DTO> owner2DTOS = excelDAO.findByRoomCode(roomInfoDTOS.get(i).getRoomCode(), identity1);
                if (owner2DTOS.size() != 0) {
                    String surname1 = "";
                    String idNumber1 = "";
                    String mobilePhone1 = "";
                    for (int j = 0; j < owner2DTOS.size(); j++) {
                        String temp1 = owner2DTOS.get(j).getSurname();
                        surname1 = surname1 + ";" + temp1;

                        String temp2 = owner2DTOS.get(j).getIdNumber();
                        idNumber1 = idNumber1 + ";" + temp2;

                        String temp4 = owner2DTOS.get(j).getMobilePhone();
                        mobilePhone1 = mobilePhone1 + ";" + temp4;
                    }
                    String surname = surname1.substring(1);
                    String idNumber = idNumber1.substring(1);
                    String mobilePhone = mobilePhone1.substring(1);
                    String normalPaymentStatus = String.valueOf(owner2DTOS.get(0).getNormalPaymentStatus());
                    String startBillingTime = owner2DTOS.get(0).getStartBillingTime();
                    String realRecyclingHomeTime = owner2DTOS.get(0).getRealRecyclingHomeTime();
                    String remarks = owner2DTOS.get(0).getRemarks();
                    map.put("姓名", surname);
                    map.put("身份证号", idNumber);
                    map.put("手机号", mobilePhone);
                    map.put("身份", identity1);
                    map.put("身份#", identity);
                    map.put("缴费状态", normalPaymentStatus);
                    map.put("缴费状态#",systemSetDAO.findNameBySettingCodeAndName(normalPaymentStatus, "NORMAL_PAYMENT_STATUS"));
                    map.put("物业费开始计费时间", startBillingTime);
                    map.put("实际收房时间", realRecyclingHomeTime);
                    map.put("租房开始时间", "");
                    map.put("租房结束时间", "");
                    map.put("备注", remarks);
                }
                values.add(map);
            }
            ExcelUtils.writeExcel(filepath, sheetName, titles, values);
            return "10000;"+"导出成功！;"+findPath;
        }catch (FileNotFoundException e){
            return "表格在占用，请关闭打开的excel表格或找不到路径";
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return "异常错误";
        }
    }


    /**
     * 租客导出
     *
     * */
    public String CustomerExport(String level, String code, int identity1, String organizationId){
        try {
            String filepath = "";
            String name = "";
            String findPath = "";
            String sheetName = "sheet1";
            List<RoomInfoDTO> roomInfoDTOS = null;
            String identity = systemSetDAO.findNameBySettingCodeAndName(String.valueOf(identity1), "IDENTITY");
            if (level.equals("1")) {
                roomInfoDTOS = vacantRoomDAO.findVillageByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() + identity + "导出.xlsx";
            }
            if (level.equals("2")) {
                roomInfoDTOS = vacantRoomDAO.findRegionByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() + identity + "导出.xlsx";
            }
            if (level.equals("3")) {
                roomInfoDTOS = vacantRoomDAO.findBuildingByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() + identity + "导出.xlsx";
            }
            if (level.equals("4")) {
                roomInfoDTOS = vacantRoomDAO.findUnitByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() +
                        roomInfoDTOS.get(0).getUnitName() + identity + "导出.xlsx";
            }
            if (level.equals("5")) {
                roomInfoDTOS = vacantRoomDAO.findRoomByIdentity(code,identity1);
                if (roomInfoDTOS.size() == 0){
                    return "无此种类型的客户";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() +
                        roomInfoDTOS.get(0).getUnitName() +
                        roomInfoDTOS.get(0).getRoomCode() + identity + "导出.xlsx";
            }
            filepath = excelPath+name;
            findPath = findExcelPath + name;

            List<String> titles = new ArrayList<>();
            titles.add("小区");
            titles.add("地块");
            titles.add("楼栋");
            titles.add("单元");
            titles.add("楼层");
            titles.add("房间号");
            titles.add("使用面积");
            titles.add("房间类型");
            titles.add("房间类型#");

            titles.add("房间状态");
            titles.add("房间状态#");

            titles.add("装修情况");
            titles.add("装修情况#");

            titles.add("装修开始时间");
            titles.add("装修结束时间");
            titles.add("姓名");
            titles.add("身份证号");
            titles.add("手机号");

            titles.add("身份");
            titles.add("身份#");

            titles.add("缴费状态");
            titles.add("缴费状态#");

            titles.add("物业费开始计费时间");
            titles.add("实际收房时间");
            titles.add("租房开始时间");
            titles.add("租房结束时间");
            titles.add("备注");
            List<Map<String, Object>> values = new ArrayList<>();

            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("小区", roomInfoDTOS.get(i).getVillageName());
                map.put("地块", roomInfoDTOS.get(i).getRegionName());
                map.put("楼栋", roomInfoDTOS.get(i).getBuildingName());
                map.put("单元", roomInfoDTOS.get(i).getUnitName());

                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                map.put("楼层",floor);

                String roomc = roomInfoDTOS.get(i).getRoomCode();
                String roomcc = roomc.substring(roomc.lastIndexOf("-")+1);
                map.put("房间号",roomcc);

                map.put("使用面积", roomInfoDTOS.get(i).getRoomSize());
                map.put("房间类型", roomInfoDTOS.get(i).getRoomType());
                map.put("房间类型#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRoomType()), "ROOM_TYPE"));

                map.put("房间状态", roomInfoDTOS.get(i).getRoomStatus());
                map.put("房间状态#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRoomStatus()), "ROOM_STATUS"));

                map.put("装修情况", roomInfoDTOS.get(i).getRenovationStatus());
                map.put("装修情况#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRenovationStatus()), "RENOVATION_STATUS"));

                String startTime = roomInfoDTOS.get(i).getRenovationStartTime();
                String endTime = roomInfoDTOS.get(i).getRenovationDeadline();
                if ("1999-09-09".equals(startTime)) {
                    startTime = null;
                }
                if ("1999-09-09".equals(endTime)) {
                    endTime = null;
                }
                map.put("装修开始时间", startTime);
                map.put("装修结束时间", endTime);

                List<Owner2DTO> owner2DTOS = excelDAO.findByRoomCode(roomInfoDTOS.get(i).getRoomCode(), identity1);
                if (owner2DTOS.size() != 0) {
                    String surname1 = "";
                    String idNumber1 = "";
                    String mobilePhone1 = "";
                    for (int j = 0; j < owner2DTOS.size(); j++) {
                        String temp1 = owner2DTOS.get(j).getSurname();
                        surname1 = surname1 + ";" + temp1;

                        String temp2 = owner2DTOS.get(j).getIdNumber();
                        idNumber1 = idNumber1 + ";" + temp2;

                        String temp4 = owner2DTOS.get(j).getMobilePhone();
                        mobilePhone1 = mobilePhone1 + ";" + temp4;
                    }
                    String surname = surname1.substring(1);
                    String idNumber = idNumber1.substring(1);
                    String mobilePhone = mobilePhone1.substring(1);
                    String normalPaymentStatus = String.valueOf(owner2DTOS.get(0).getNormalPaymentStatus());

                    String CustomerStartTime = owner2DTOS.get(0).getStartTime();
                    String CustomerEndTime = owner2DTOS.get(0).getEndTime();

                    String remarks = owner2DTOS.get(0).getRemarks();
                    map.put("姓名", surname);
                    map.put("身份证号", idNumber);
                    map.put("手机号", mobilePhone);
                    map.put("身份", identity1);
                    map.put("身份#", identity);
                    map.put("缴费状态", normalPaymentStatus);
                    map.put("缴费状态#",systemSetDAO.findNameBySettingCodeAndName(normalPaymentStatus, "NORMAL_PAYMENT_STATUS"));

                    map.put("物业费开始计费时间", "");
                    map.put("实际收房时间", "");

                    map.put("租房开始时间", CustomerStartTime);
                    map.put("租房结束时间", CustomerEndTime);
                    map.put("备注", remarks);
                }
                values.add(map);
            }
            ExcelUtils.writeExcel(filepath, sheetName, titles, values);
            return "10000;"+"导出成功！;"+findPath;
        }catch (FileNotFoundException e){
            return "表格在占用（或找不到路径），请关闭打开的excel表格";
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return "异常错误";
        }
    }


    public String vacantRoomExport(String level, String code, String organizationId)throws IOException {
        try {
            String filepath = "";
            String name = "";
            String findPath = "";
            String sheetName = "sheet1";
            List<RoomInfoDTO> roomInfoDTOS = null;

            if (level.equals("1")) {
                roomInfoDTOS = vacantRoomDAO.findVillage(code);
                if (roomInfoDTOS.size() == 0){
                    return "无空置房";
                }
                name = roomInfoDTOS.get(0).getVillageName() + "空置房导出.xlsx";
            }
            if (level.equals("2")) {
                roomInfoDTOS = vacantRoomDAO.findRegion(code);
                if (roomInfoDTOS.size() == 0){
                    return "无空置房";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() + "空置房导出.xlsx";
            }
            if (level.equals("3")) {
                roomInfoDTOS = vacantRoomDAO.findBuilding(code);
                if (roomInfoDTOS.size() == 0){
                    return "无空置房";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() + "空置房导出.xlsx";
            }
            if (level.equals("4")) {
                roomInfoDTOS = vacantRoomDAO.findUnit(code);
                if (roomInfoDTOS.size() == 0){
                    return "无空置房";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() +
                        roomInfoDTOS.get(0).getUnitName() + "空置房导出.xlsx";
            }
            if (level.equals("5")) {
                roomInfoDTOS = vacantRoomDAO.findRoom(code);
                if (roomInfoDTOS.size() == 0){
                    return "无空置房";
                }
                name = roomInfoDTOS.get(0).getVillageName() +
                        roomInfoDTOS.get(0).getRegionName() +
                        roomInfoDTOS.get(0).getBuildingName() +
                        roomInfoDTOS.get(0).getUnitName() +
                        roomInfoDTOS.get(0).getRegionName() + "空置房导出.xlsx";
            }
            filepath = excelPath+name;
            findPath = findExcelPath + name;

            List<String> titles = new ArrayList<>();
            titles.add("小区");
            titles.add("地块");
            titles.add("楼栋");
            titles.add("单元");
            titles.add("楼层");
            titles.add("房间号");
            titles.add("使用面积");
            titles.add("房间类型");
            titles.add("房间类型#");
            titles.add("房间状态");
            titles.add("房间状态#");
            titles.add("装修情况");
            titles.add("装修情况#");
            titles.add("装修开始时间");
            titles.add("装修结束时间");
            titles.add("姓名");
            titles.add("身份证号");
            titles.add("手机号");
            titles.add("身份");
            titles.add("身份#");
            titles.add("缴费状态");
            titles.add("缴费状态#");
            titles.add("物业费开始计费时间");
            titles.add("实际收房时间");
            titles.add("租房开始时间");
            titles.add("租房结束时间");
            titles.add("备注");
            List<Map<String, Object>> values = new ArrayList<>();

            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("小区", roomInfoDTOS.get(i).getVillageName());
                map.put("地块", roomInfoDTOS.get(i).getRegionName());
                map.put("楼栋", roomInfoDTOS.get(i).getBuildingName());
                map.put("单元", roomInfoDTOS.get(i).getUnitName());

                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                map.put("楼层",floor);
                map.put("房间号", roomInfoDTOS.get(i).getRoomCode());

                map.put("使用面积", roomInfoDTOS.get(i).getRoomSize());

                map.put("房间类型", roomInfoDTOS.get(i).getRoomType());
                map.put("房间类型#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRoomType()), "ROOM_TYPE"));

                map.put("房间状态", roomInfoDTOS.get(i).getRoomStatus());
                map.put("房间状态#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRoomStatus()), "ROOM_STATUS"));

                map.put("装修情况", roomInfoDTOS.get(i).getRenovationStatus());
                map.put("装修情况#", systemSetDAO.findNameBySettingCodeAndName(String.valueOf(roomInfoDTOS.get(i).getRenovationStatus()), "RENOVATION_STATUS"));
                map.put("装修开始时间", roomInfoDTOS.get(i).getRenovationStartTime());
                map.put("装修结束时间", roomInfoDTOS.get(i).getRenovationDeadline());

                map.put("姓名", "");
                map.put("身份证号", "");
                map.put("手机号", "");
                map.put("身份", "");
                map.put("身份#","" );
                map.put("缴费状态","" );
                map.put("缴费状态#","");
                map.put("物业费开始计费时间", "");
                map.put("实际收房时间", "");
                map.put("租房开始时间", "");
                map.put("租房结束时间", "");
                map.put("备注", "");

                values.add(map);
            }
            ExcelUtils.writeExcel(filepath, sheetName, titles, values);
            return "10000;"+"导出成功！;"+findPath;
        }catch (FileNotFoundException e){
            return "表格在占用（或找不到路径），请关闭打开的excel表格";
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return "异常错误";
        }
    }



}
