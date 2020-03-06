package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.RefundApplicationDO;
import com.rbi.interactive.entity.RefundHistoryDO;
import com.rbi.interactive.utils.PageData;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface RefundService {

    Map findCustomerByPhone(String mobilePhone);

    List<ChargeItemDO> findChargeItem(String userId);

    void add(JSONObject jsonObject, String userId);

    PageData findByPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname);

    PageData findNotByPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode, String buildingCode, String unitCode, String roomCode, String mobilePhone,String idNumber,String surname);

    PageData findAlreadyByPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode, String buildingCode, String unitCode, String roomCode, String mobilePhone,String idNumber,String surname);

    void update(RefundHistoryDO refundHistoryDO) throws ParseException;

    void delete(List<String> ids);

    void application(RefundApplicationDO refundApplicationDO);

    void preliminaryExaminationPass(int id);

    void reviewPass(int id,String userId) throws Exception;

    void examineNoPass(int id);

    PageData findApplicationByPage(int pageNum,int pageSzie,String userId);

    PageData findApplicationByWaitAuditPage(int pageNum,int pageSzie,String userId);

    PageData findApplicationByWaitReviewPage(int pageNum,int pageSzie,String userId);

    PageData findApplicationByAlreadyAuditPage(int pageNum,int pageSzie,String userId);

    void updateApplication(JSONObject jsonObject,String userId);

}
