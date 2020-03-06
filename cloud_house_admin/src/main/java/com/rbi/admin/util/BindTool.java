package com.rbi.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.ExcelDAO;
import com.rbi.admin.dao.RegionDAO;
import com.rbi.admin.dao.connect.OwnerDAO;
import com.rbi.admin.dao.structure.StructureDAO;
import com.rbi.admin.entity.dto.CustomerInfoDTO;
import com.rbi.admin.entity.dto.structure.ChargeCodeDTO;
import com.rbi.admin.entity.edo.CustomerInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BindTool {
    @Autowired(required = false)
    OwnerDAO ownerDAO;
    @Autowired(required = false)
    StructureDAO structureDAO;
    @Autowired(required = false)
    RegionDAO regionDAO;
    @Autowired(required = false)
    ExcelDAO excelDAO;

    public void bindChargeItem(String organizationId, String roomCode, int roomType,
                               String villageCode, String villageName, String regionCode,
                               String regionName, String buildingCode, String buildingName,
                               String unitCode, String unitName, String idt,
                               String organizationName) {

        System.out.println("绑定收费项目");
        List<ChargeCodeDTO> chargeCodeDTOS2 = excelDAO.findChargeItem(organizationId);//找出所有收费项目
        for (int m = 0; m < chargeCodeDTOS2.size(); m++) {
            if (roomType == 1 && chargeCodeDTOS2.get(m).getChargeType() == 1) {
                int num = excelDAO.findRoomItemNum(roomCode, chargeCodeDTOS2.get(m).getChargeCode());
                if (num == 0) {
                    System.out.println("绑定住宅物业费");
                    excelDAO.addRoomItem2(roomCode, chargeCodeDTOS2.get(m).getChargeCode(), 0.0, villageCode, villageName, regionCode, regionName, buildingCode, buildingName, unitCode, unitName, idt, organizationId, organizationName);
                } else {
                    System.out.println("已绑定住宅物业费");
                }
            } else if (roomType == 2 && chargeCodeDTOS2.get(m).getChargeType() == 2) {
                int num = excelDAO.findRoomItemNum(roomCode, chargeCodeDTOS2.get(m).getChargeCode());
                if (num == 0) {
                    System.out.println("绑定商业物业费");
                    excelDAO.addRoomItem2(roomCode, chargeCodeDTOS2.get(m).getChargeCode(), 0.0, villageCode, villageName, regionCode, regionName, buildingCode, buildingName, unitCode, unitName, idt, organizationId, organizationName);
                } else {
                    System.out.println("已绑定商业物业费");
                }
            } else if (roomType == 3 && chargeCodeDTOS2.get(m).getChargeType() == 3) {
                int num = excelDAO.findRoomItemNum(roomCode, chargeCodeDTOS2.get(m).getChargeCode());
                if (num == 0) {
                    System.out.println("绑定办公楼物业费");
                    excelDAO.addRoomItem2(roomCode, chargeCodeDTOS2.get(m).getChargeCode(), 0.0, villageCode, villageName, regionCode, regionName, buildingCode, buildingName, unitCode, unitName, idt, organizationId, organizationName);
                } else {
                    System.out.println("已绑定办公楼物业费");
                }
            } else if (chargeCodeDTOS2.get(m).getChargeType() != 1 && chargeCodeDTOS2.get(m).getChargeType() != 2 && chargeCodeDTOS2.get(m).getChargeType() != 3){
                int num = excelDAO.findRoomItemNum(roomCode, chargeCodeDTOS2.get(m).getChargeCode());
                if (num == 0) {
                    System.out.println("绑定其他收费项目");
                    excelDAO.addRoomItem2(roomCode, chargeCodeDTOS2.get(m).getChargeCode(), 0.0, villageCode, villageName, regionCode, regionName, buildingCode, buildingName, unitCode, unitName, idt, organizationId, organizationName);
                } else {
                    System.out.println("已绑定其他收费项目");
                }
            }
        }
    }

    public String addOwner(JSONArray jsonArray, String roomCode, String organizationId,String startBillingTime2,String realRecyclingHomeTime2){
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = JSONArray.parseObject(jsonArray.get(i).toString());
                CustomerInfoDTO customerInfoDTO = JSON.toJavaObject(jsonObject, CustomerInfoDTO.class);
                String customerUserId = customerInfoDTO.getCustomerUserId();
                String surname = customerInfoDTO.getSurname();
                String idNumber = customerInfoDTO.getIdNumber();
                Integer sex = Integer.valueOf(RegexValidateCard.validateIdCard(idNumber));
                String mobilePhone = customerInfoDTO.getMobilePhone();
                Integer identity = customerInfoDTO.getIdentity();
                String remarks = customerInfoDTO.getRemarks();
                Integer normalPaymentStatus = customerInfoDTO.getNormalPaymentStatus();

                String startBillingTime = startBillingTime2;
                String realRecyclingHomeTime = realRecyclingHomeTime2;
                if (surname == "" || idNumber =="" || mobilePhone=="" || realRecyclingHomeTime=="" || startBillingTime == "") {
                    return "10001";
                }
                if (null == surname|| null == identity|| null == normalPaymentStatus || null == startBillingTime || null == idNumber|| null == mobilePhone|| null == realRecyclingHomeTime) {
                    return "10001";
                }
                try {
                    RegexValidateCard.validateIdCard(idNumber);
                } catch (CustomerException e) {
                    return "10002";
                }
                if (false == RegexUtils.checkMobile(mobilePhone)) {
                    return "10003";
                }
                if (customerUserId == null || "".equals(customerUserId)) {
                    CustomerInfoDO customerInfoDO = ownerDAO.findByIdNumber(idNumber);
                    if (null == customerInfoDO){
                        String uId = DateUtil.timeStamp() + Tools.random(10, 99);
                        System.out.println("添加用户" + surname);
                        excelDAO.addCustomer(uId, surname, idNumber, sex, mobilePhone, remarks);
                        System.out.println("绑定用户" + surname);
                        excelDAO.addRoomCustomer(uId, roomCode, startBillingTime, organizationId, normalPaymentStatus, identity, 0f, realRecyclingHomeTime);
                    }else {
                        System.out.println("绑定用户" + surname);
                        excelDAO.addRoomCustomer(customerInfoDO.getUserId(), roomCode, startBillingTime, organizationId, normalPaymentStatus, identity, 0f, realRecyclingHomeTime);
                    }
                } else {
                    System.out.println("修改用户" + surname);
                    excelDAO.updateCustomer(mobilePhone, surname, idNumber, sex, remarks, customerUserId);
                    excelDAO.updateRC(identity, normalPaymentStatus, startBillingTime, realRecyclingHomeTime, roomCode, customerUserId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1000";
    }

}

