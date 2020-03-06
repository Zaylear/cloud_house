package com.rbi.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.CouponDO;
import com.rbi.admin.entity.dto.ChargeCodesDTO;
import com.rbi.admin.entity.dto.SystemSettingDTO;
import com.rbi.admin.service.CouponService;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.ChargeService;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = "/coupon")
public class CouponController {
    @Autowired
    CouponService couponService;

    @Autowired
    StructureService structureService;

    @Autowired
    SystemSettingService systemSettingService;

    @Autowired
    ChargeService chargeService;

    @RequestMapping(value = "/findByPage",method = RequestMethod.POST)
    public ResponseVo<PageData> findByPage(@RequestBody JSONObject json,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = structureService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            PageData pageData = couponService.findByPage(pageNo,pageSize,organizationId);
            return ResponseVo.build("1000","查询成功",pageData);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @PostMapping("/add")
    public ResponseVo addCoupon(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");

            CouponDO couponDO = JSON.toJavaObject(json,CouponDO.class);
            String status = couponService.addCoupon(couponDO,userId);
            if (status == "10000"){
                return ResponseVo.build("1000", "添加成功");
            }else {
                return ResponseVo.build("1003", status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "金额输入错误");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "金额输入错误");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseVo<Object> updateSetting(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            CouponDO couponDO = JSON.toJavaObject(json,CouponDO.class);
            String status = couponService.updateCoupon(couponDO,userId);
            if (status == "10000"){
                return ResponseVo.build("1000", "修改成功");
            }else {
                return ResponseVo.build("1003", status);
            }
        } catch (JSONException e) {
            return ResponseVo.build("1003", "金额输入错误");
        } catch (NumberFormatException e) {
            return ResponseVo.build("1003", "金额输入错误");
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
    public ResponseVo deleteByIds(@RequestBody JSONObject request){
        try {
            JSONArray result = request.getJSONArray("data");
            couponService.deleteByIds(result);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/findEffectiveTime")
    public ResponseVo<List<SystemSettingDTO>> findEffectiveTime(HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = structureService.findOrganizationId(userId);
            List<SystemSettingDTO> systemSettingDTOS = systemSettingService.findChoose("COUPON_EFFECTIVE_TIME",organizationId);
            return ResponseVo.build("1000", "查询有效时间成功",systemSettingDTOS);
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/findCouponType")
    public ResponseVo<List<SystemSettingDTO>> findCouponType(HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = structureService.findOrganizationId(userId);

            List<SystemSettingDTO> systemSettingDTOS = systemSettingService.findChoose("COUPON_TYPE",organizationId);
            return ResponseVo.build("1000", "查询收费优惠卷类型成功",systemSettingDTOS);
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @PostMapping("/findChargeCode")
    public ResponseVo<List<ChargeCodesDTO>> findChargeCode(HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = structureService.findOrganizationId(userId);
            List<ChargeCodesDTO> chargeCodesDTOS =chargeService.chooseChargeCode(organizationId);
            return ResponseVo.build("1000", "查询收费项目编号成功",chargeCodesDTOS);
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

}
