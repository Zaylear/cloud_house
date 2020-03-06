package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.LiquidatedDamageLogDO;
import com.rbi.interactive.entity.dto.RoomCodeAndMobilePhoneDTO;
import com.rbi.interactive.utils.PageData;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface BusinessLiquidatedDamagesService {

    Map<String,Object> batchProcessingLiquidatedDamages(MultipartFile multipartFile, String userId) throws ParseException;

    PageData findByPage(int pageNo,int pageSize,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname,String userId);

    void update(JSONObject jsonObject, String userId) throws ParseException;

    PageData findByNotByPage(int pageNum,int pageSize,String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname);

    PageData findByWaitAuditPage(int pageNum,int pageSize,String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname);

    PageData findByWaitReviewPage(int pageNum,int pageSize,String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname);

    PageData findByAlreadyAuditPage(int pageNum,int pageSize,String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname);

    void auditPass(int id,String userId);

    void reviewPass(int id,String userId);

    void noPass(int id,String remarks,String userId);

    void delete(List<Integer> idList);

    Map<String,Object> onekeyCalculationLiquidatedDamages(List<RoomCodeAndMobilePhoneDTO> roomCodeAndMobilePhoneDTOS, String startTime, String endTime, String liquidatedDamageDueTime, String userId);
}
