package com.rbi.interactive.controller;


import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.DueTimeException;
import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.RefundApplicationDO;
import com.rbi.interactive.entity.RefundHistoryDO;
import com.rbi.interactive.service.RefundService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/refund")
public class RefundController {

    private final static Logger logger = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    RefundService refundService;

    /**
     * 根据手机号查询客户信息
     */
    @PostMapping("/findCustomerByPhone")
    public ResponseVo findCustomerByPhone(@RequestBody JSONObject jsonObject){

        try {
            String phone = jsonObject.getString("mobilePhone");
            Map<String ,Object> map = new HashMap<>();

            map = refundService.findCustomerByPhone(phone);
            if ("1000".equals(map.get("status"))) {
                return ResponseVo.build("1000","请求成功",map.get("value"));
            }else {
                return ResponseVo.build("1005","参数不存在");
            }

        } catch (Exception e) {
            logger.error("【退款信息请求类】根据手机号查询客户信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     * 查询收费项目信息
     */
    @PostMapping("/findChargeItem")
    public ResponseVo findChargeItem(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            List<ChargeItemDO> chargeItemDOS = refundService.findChargeItem(userId);
            return ResponseVo.build("1000","请求成功",chargeItemDOS);
        } catch (Exception e) {
            logger.error("【退款信息请求类】查询收费项目信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 新增退款缴费单
     */
    @PostMapping("/add")
    public ResponseVo add(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            refundService.add(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【退款信息请求类】新增退款缴费单失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 查询退款信息
     */
    @PostMapping("/findByPage")
    public ResponseVo findByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

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

            PageData pageData = refundService.findByPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】查询退款信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 查询未退款信息
     */
    @PostMapping("/findNotByPage")
    public ResponseVo findNotByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

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

            PageData pageData = refundService.findNotByPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】查询未退款信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 查询已退款信息
     */
    @PostMapping("/findAlreadyByPage")
    public ResponseVo findAlreadyByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

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

            PageData pageData = refundService.findAlreadyByPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】查询已退款信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 修改退款信息
     */
    @PostMapping("/update")
    public ResponseVo update(@RequestBody JSONObject jsonObject){
        try {
            RefundHistoryDO refundHistoryDO = JSONObject.parseObject(jsonObject.toJSONString(), RefundHistoryDO.class);
            refundService.update(refundHistoryDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【退款信息请求类】修改退款信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 删除退款信息
     */
    @PostMapping("/delete")
    public ResponseVo delete(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            refundService.delete(ids);
            return ResponseVo.build("1000","请求成功");
        } catch (NumberFormatException e) {
            logger.error("【退款信息请求类】删除退款信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 申请退款
     */
    @PostMapping("/application")
    public ResponseVo application(@RequestBody JSONObject jsonObject){
        try {
            RefundApplicationDO refundApplicationDO = JSONObject.parseObject(jsonObject.toJSONString(), RefundApplicationDO.class);
            refundService.application(refundApplicationDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【退款信息请求类】申请退款处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     * 退款初审通过
     */
    @PostMapping("/preliminaryExaminationPass")
    public ResponseVo preliminaryExaminationPass(@RequestBody JSONObject jsonObject){
        try {
            refundService.preliminaryExaminationPass(jsonObject.getInteger("id"));
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【退款信息请求类】退款初审通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 复审通过
     */
    @PostMapping("/reviewPass")
    public ResponseVo reviewPass(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            refundService.reviewPass(jsonObject.getInteger("id"),userId);
            return ResponseVo.build("1000","请求成功");
        } catch (DueTimeException e){
            logger.error("【优惠券请求类】物业费到期时间异常，ERROR：{}",e);
            return ResponseVo.build("1002","该房号物业费到期时间异常，不允许抵扣！");
        } catch (Exception e) {
            logger.error("【退款信息请求类】复审通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 审核不通过
     */
    @PostMapping("/examineNoPass")
    public ResponseVo examineNoPass(@RequestBody JSONObject jsonObject){
        try {
            refundService.examineNoPass(jsonObject.getInteger("id"));
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【退款信息请求类】审核不通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询退款申请信息
     */
    @PostMapping("/findApplicationByPage")
    public ResponseVo findApplicationByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            PageData pageData = refundService.findApplicationByPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】分页查询退款申请信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询退款申请待审核信息
     */
    @PostMapping("findApplicationByWaitAuditPage")
    public ResponseVo findApplicationByWaitAuditPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            PageData pageData = refundService.findApplicationByWaitAuditPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】分页查询退款申请待审核信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询退款申请待审核信息
     */
    @PostMapping("findApplicationByWaitReviewPage")
    public ResponseVo findApplicationByWaitReviewPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            PageData pageData = refundService.findApplicationByWaitReviewPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】分页查询退款申请待审核信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询已审核信息
     */
    @PostMapping("findApplicationByAlreadyAuditPage")
    public ResponseVo findApplicationByAlreadyAuditPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");

            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            PageData pageData = refundService.findApplicationByAlreadyAuditPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【退款信息请求类】分页查询已审核信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/update_refund_application")
    public ResponseVo updateApplication(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            refundService.updateApplication(jsonObject,userId);
            return ResponseVo.build("1000","更新成功");
        } catch (Exception e) {
            logger.error("【退款申请信息请求类】修改退款申请信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
