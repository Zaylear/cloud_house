package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.CouponDO;
import com.rbi.interactive.entity.dto.BillsBackDTO;
import com.rbi.interactive.utils.PageData;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.List;

public interface CouponService {

    PageData findCouponPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname);

    PageData findWaitAuditPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname);

    PageData findWaitAgainAuditPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname);

    PageData findAlreadyAuditPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname);

    List<CouponDO> findByOrganizationId(String userId);

    CouponDO findByCouponCode(String couponCode,String userId);

    void add(JSONObject jsonObject, String userId) throws ParseException;

    void delete(List<Integer> ids);

    void auditNoPass(int id);

    void audit(int id);

    BillsBackDTO againAudit(int id, HttpServletRequest request) throws Exception;
}
