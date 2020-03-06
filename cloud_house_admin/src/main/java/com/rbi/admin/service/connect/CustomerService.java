package com.rbi.admin.service.connect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.ExcelDAO;
import com.rbi.admin.dao.connect.CustomerDAO;
import com.rbi.admin.dao.structure.StructureDAO;
import com.rbi.admin.entity.dto.CustomerInfoDTO;
import com.rbi.admin.entity.dto.OwnerDTO;
import com.rbi.admin.entity.edo.CustomerInfoDO;
import com.rbi.admin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CustomerService {
    @Autowired(required = false)
    CustomerDAO customerDAO;
    @Autowired(required = false)
    StructureDAO structureDAO;
    @Autowired(required = false)
    ExcelDAO excelDAO;


    public PageData findVillageByPage(String villageCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = customerDAO.findVillageByPage(villageCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findVillageNum(villageCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findRegionByPage(String regionCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = customerDAO.findRegionByPage(regionCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findRegionNum(regionCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findBuildingByPage(String buildingCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = customerDAO.findBuildingByPage(buildingCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findBuildingNum(buildingCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findUnitByPage(String unitCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = customerDAO.findUnitByPage(unitCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findUnitNum(unitCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findRoomByPage(String roomCode,int roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<OwnerDTO> roomInfoDTOS = customerDAO.findRoomByPage(roomCode,roomType, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findRoomNum(roomCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }











    public PageData findCustomerByRoomCode(String roomCode, String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String roomCode2 = "'%" + roomCode + "%'";
        List<OwnerDTO> ownerDTOS = customerDAO.findCustomerByRoomCode(roomCode2, organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findCustomerByRoomCodeNum(roomCode2, organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }

    public PageData findCustomerByPhone(String mobilePhone, String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String phone = "'%" + mobilePhone + "%'";
        List<OwnerDTO> ownerDTOS = customerDAO.findCustomerByPhone(phone, organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findCustomerByPhoneNum(phone, organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }

    public PageData findCustomerByIdNumber(String idNumber, String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String idNumber2 = "'%" + idNumber + "%'";
        List<OwnerDTO> ownerDTOS = customerDAO.findCustomerByIdNumber(idNumber2, organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findCustomerByIdNumberNum(idNumber2, organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }

    public PageData findCustomerBySurname(String idNumber, String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String idNumber2 = "'%" + idNumber + "%'";
        List<OwnerDTO> ownerDTOS = customerDAO.findCustomerBySurname(idNumber2, organizationId, pageNo, pageSize);
        int totalPage = 0;
        int count = customerDAO.findCustomerBySurnameNum(idNumber2, organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, ownerDTOS);
    }


    public Map<String, Object> findCustomerDetail(String roomCode, String customerUserId) {
        Map<String, Object> map = new HashMap<>();
        OwnerDTO ownerDTO = customerDAO.findOwnerByCodeAndUserId(roomCode, customerUserId);
        List<OwnerDTO> ownerDTO1 = customerDAO.findRoomByCustomerUserId(customerUserId);
        for (int i = 0; i < ownerDTO1.size(); i++) {
            int num = customerDAO.findCustomerNum(ownerDTO1.get(i).getRoomCode());
            if (num == 0) {
                ownerDTO1.get(i).setRentStatus("未出租");
            } else {
                ownerDTO1.get(i).setRentStatus("已出租");
            }
        }
        map.put("customer", ownerDTO);
        map.put("roomInfo", ownerDTO1);
        return map;
    }


    public String addCustomer(JSONObject json, String userId) throws Exception {
        String organizationId = structureDAO.findOrganizationId(userId);
        String roomCode = json.getString("roomCode");

//        Integer roomStatus = customerDAO.findStatusByRoomCode(roomCode);
//        if (1 == roomStatus){
//            return "空置房不能添加租客，请先添加业主";
//        }

        CustomerInfoDTO customerInfoDTO = JSON.toJavaObject(json, CustomerInfoDTO.class);
        String customerUserId = customerInfoDTO.getCustomerUserId();
        String surname = customerInfoDTO.getSurname();
        String idNumber = customerInfoDTO.getIdNumber();
        Integer sex = Integer.valueOf(RegexValidateCard.validateIdCard(idNumber));
        String mobilePhone = customerInfoDTO.getMobilePhone();
        Integer identity = customerInfoDTO.getIdentity();
        String remarks = customerInfoDTO.getRemarks();
        String startTime = customerInfoDTO.getStartTime();
        String endTime = customerInfoDTO.getEndTime();
        Integer normalPaymentStatus = customerInfoDTO.getNormalPaymentStatus();
        if (roomCode == "") {
            return "房间编号为空，请选择房间";
        }
        if (null == roomCode) {
            return "房间编号为空，请选择房间";
        }
        if (surname == "" || idNumber == "" || mobilePhone == "" || startTime == "" || endTime == "") {
            return "租客信息除备注外不能为空";
        }
        if (null == surname || null == identity || null == normalPaymentStatus || null == idNumber || null == startTime || null == endTime || null == mobilePhone) {
            return "租客信息除备注外不能为空";
        }
        try {
            RegexValidateCard.validateIdCard(idNumber);
        } catch (CustomerException e) {
            return "身份证号格式错误";
        }
        if (false == RegexUtils.checkMobile(mobilePhone)) {
            return "手机号格式错误";
        }
        if (null == customerUserId || customerUserId=="") {
            CustomerInfoDO customerInfoDO = customerDAO.findByIdNumber(idNumber);
            if (null == customerInfoDO){
                String uId = DateUtil.timeStamp() + Tools.random(10, 99);
                System.out.println("添加租客" + surname);
                excelDAO.addCustomer(uId, surname, idNumber, sex, mobilePhone, remarks);
                System.out.println("绑定租客" + surname);
                excelDAO.addRoomCustomerC(uId, roomCode, startTime, organizationId, normalPaymentStatus, identity, 0f, endTime);
            }else {
                System.out.println("绑定租客" + surname);
                excelDAO.addRoomCustomerC(customerInfoDO.getUserId(), roomCode, startTime, organizationId, normalPaymentStatus, identity, 0f, endTime);
            }
        } else {
            System.out.println("修改租客" + surname);
            excelDAO.updateCustomer(mobilePhone, surname, idNumber, sex, remarks, customerUserId);
            excelDAO.updateRCC(identity, normalPaymentStatus, startTime, endTime, roomCode, customerUserId);
        }
        customerDAO.updateRoomStatus(roomCode);
        return "10000";
    }

}
