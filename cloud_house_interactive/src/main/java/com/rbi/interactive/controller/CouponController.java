package com.rbi.interactive.controller;


import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.DueTimeException;
import com.rbi.interactive.entity.CouponDO;
import com.rbi.interactive.entity.RoomAndCouponDO;
import com.rbi.interactive.service.CouponService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    public final static Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    CouponService couponService;

    /**
     * 分页查询优惠券信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findCouponPage")
    public ResponseVo<PageData> findCouponPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = couponService.findCouponPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【优惠券请求类】分页查询异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询待审核优惠券信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findWaitAuditPage")
    public ResponseVo<PageData> findWaitAuditPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            PageData pageData = couponService.findWaitAuditPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【优惠券请求类】分页查询待审核数据异常ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询待复核优惠券信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findWaitAgainAuditPage")
    public ResponseVo<PageData> findWaitAgainAuditPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            PageData pageData = couponService.findWaitAgainAuditPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【优惠券请求类】分页查询待复审数据异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询已审核优惠券信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findAlreadyAuditPage")
    public ResponseVo<PageData> findAlreadyAuditPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            PageData pageData = couponService.findAlreadyAuditPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【优惠券请求类】分页查询已审核数据异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据机构ID查询优惠券信息
     * @param request
     * @return
     */
    @PostMapping("/findByOrganizationId")
    public ResponseVo<List<CouponDO>> findByOrganizationId(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            return ResponseVo.build("1000","请求成功",couponService.findByOrganizationId(userId));
        } catch (Exception e) {
            logger.error("【优惠券请求类】根据机构Id查询优惠券信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 通过优惠券编号查询优惠券信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/findByCouponCode")
    public ResponseVo<CouponDO> findByCouponCode(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String couponCode = jsonObject.getString("couponCode");
            CouponDO couponDO = couponService.findByCouponCode(couponCode,userId);
            return ResponseVo.build("1000","请求成功",couponDO);
        } catch (Exception e) {
            logger.error("【优惠券请求类】根据优惠券编号查询优惠券信息，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 客户获取优惠券新增
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ResponseVo<String> add(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            couponService.add(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (ConnectException e) {
            logger.error("【优惠券复审处理类】处理异常！ERROR:{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        } catch (ParseException e) {
            logger.error("【优惠券复审处理类】处理异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        } catch (Exception e) {
            logger.error("【优惠券请求类】客户获取优惠券新增失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/update")
    public ResponseVo<String> update(){
        return null;
    }

    /**
     * 删除优惠券获取记录
     * @param jsonObject
     * @return
     */
    @PostMapping("/delete")
    public ResponseVo<String> delete(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            List<Integer> idList = new ArrayList<>();
            for (String id:ids){
                idList.add(Integer.parseInt(id));
            }
            couponService.delete(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (NumberFormatException e) {
            logger.error("【优惠券请求类】客户获取优惠券删除失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 审核未通过处理接口
     * @param jsonObject
     * @return
     */
    @PostMapping("/auditNoPass")
    public ResponseVo<String> auditNoPass(@RequestBody JSONObject jsonObject){
        try {
            couponService.auditNoPass(jsonObject.getInteger("id"));
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【优惠券请求类】审核不通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 审核通过处理接口
     * @param jsonObject
     * @return
     */
    @PostMapping("/audit")
    public ResponseVo<String> audit(@RequestBody JSONObject jsonObject){
        try {
            couponService.audit(jsonObject.getInteger("id"));
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【优惠券请求类】审核通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 复审通过处理接口
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/againAudit")
    public ResponseVo<String> againAudit(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            couponService.againAudit(jsonObject.getInteger("id"),request);
            return ResponseVo.build("1000","请求成功");
        } catch (DueTimeException e){
            logger.error("【优惠券请求类】物业费到期时间异常，ERROR：{}",e);
            return ResponseVo.build("1002","该房号物业费到期时间异常，不允许抵扣！");
        } catch (Exception e) {
            logger.error("【优惠券请求类】复审通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
