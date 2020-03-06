package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.ParkingSpaceDueTimeException;
import com.rbi.interactive.abnormal.RentalParkingSpaceException;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import com.rbi.interactive.entity.dto.BillsBackDTO;
import com.rbi.interactive.entity.dto.ChargeItemBackDTO;
import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import com.rbi.interactive.utils.PageData;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface FrontOfficeCashierService {

//    PageData<T> findHousePage(int pageNo, int pageSize, String token) throws Exception;

    PageData<T> findHousePageS(int pageNum, int pageSize, String token, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) throws Exception;

    List<HouseAndProprietorDTO> findHouseByRoomCode(JSONObject jsonObject, String token) throws ConnectException;

    List<ChargeItemBackDTO> findChargeItem(String roomCode,String token) throws Exception;

    List<ParkingSpaceCostDetailDO> parkingSpaceRentalFeeCost(JSONObject jsonObject,String organizationId) throws ParseException, ConnectException, RentalParkingSpaceException;

    ParkingSpaceCostDetailDO parkingManagementFeeCost(JSONObject jsonObject, String organizationId) throws ParkingSpaceDueTimeException, ParseException, ConnectException;

    Map<String,Object> findCost(JSONObject jsonObject, String userId) throws ParseException, ConnectException, ParkingSpaceDueTimeException;

    List<BillsBackDTO> addBill(JSONObject jsonObject, String userId) throws Exception;

    BillDetailedDO findChargeItemCostDTO(String chargeCode, int datedif);

    String printBilles(JSONObject jsonObject) throws ParseException;

    Map<String,Object> importOldBills(MultipartFile multipartFile, String userId);

    Map<String,Object> expenseDeductionAndReSum(JSONObject jsonObject);

}
