package com.rbi.admin.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.CouponDAO;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.SystemSettingDAO;
import com.rbi.admin.dao.other.Coupon2DAO;
import com.rbi.admin.dao.structure.StructureDAO;
import com.rbi.admin.entity.edo.CouponDO;
import com.rbi.admin.entity.dto.CouponDO2;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    CouponDAO  couponDAO;
    @Autowired(required = false)
    StructureDAO structureDAO;
    @Autowired(required = false)
    SystemSettingDAO systemSettingDAO;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    @Autowired(required = false)
    Coupon2DAO coupon2DAO;

    public PageData findByPage(int pageNum, int pageSize,String organizationId){
        int pageNo = pageSize * (pageNum - 1);
        List<CouponDO2> couponDOS  = coupon2DAO.findByPage(organizationId,pageNo,pageSize);
        int totalPage = 0;
        int count = coupon2DAO.findNum(organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,couponDOS);
    }

    public String addCoupon(CouponDO couponDO,String userId){
        if (null == couponDO.getCouponName() || null == couponDO.getChargeCode() || null == couponDO.getCouponType() || null == couponDO.getMoney()){
            return "必填项不能为空";
        }
        if ("" == couponDO.getCouponName() || "" == couponDO.getChargeCode() || "" == couponDO.getCouponType()){
            return "必填项不能为空";
        }
        String organizationId = structureDAO.findOrganizationId(userId);
        String distributor = structureDAO.findNameByUserId(userId);
        String couponCode = DateUtil.timeStamp()+ Tools.random(100,999);
        couponDO.setOrganizationId(organizationId);
        couponDO.setDistributor(distributor);
        couponDO.setCouponCode(couponCode);
        couponDO.setEnable(1);
        couponDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        couponDAO.save(couponDO);
        return "10000";
    }

    public String updateCoupon(CouponDO couponDO,String userId){
        if (null == couponDO.getCouponName() || null == couponDO.getChargeCode() || null == couponDO.getCouponType() || null == couponDO.getEffectiveTime() || null == couponDO.getMoney()){
            return "必填项不能为空";
        }
        if ("" == couponDO.getCouponName() || "" == couponDO.getChargeCode() || "" == couponDO.getCouponType()){
            return "必填项不能为空";
        }
        String organizationId = structureDAO.findOrganizationId(userId);
        String distributor = structureDAO.findNameByUserId(userId);
        couponDO.setOrganizationId(organizationId);
        couponDO.setDistributor(distributor);
        couponDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        couponDAO.saveAndFlush(couponDO);
        return "10000";
    }


    public void deleteByIds(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("id");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "("+str+")";
        idsDeleteDAO.deleteCouponByIds(ids);
    }

}
