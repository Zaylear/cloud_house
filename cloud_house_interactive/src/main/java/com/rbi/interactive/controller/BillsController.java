package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDO;
import com.rbi.interactive.service.BillsService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bills")
public class BillsController {

    private final static Logger logger = LoggerFactory.getLogger(BillsController.class);

    @Autowired
    BillsService billsService;

    /**
     * 分页查询缴费单信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findBillPage")
    public ResponseVo<PageData> findBillPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
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

            PageData<BillDO> billDOPageData = billsService.findBillPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",billDOPageData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002","服务器处理失败");
        }

    }



    /**
     * 分页查询缴费单信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/find_bill_detail")
    public ResponseVo<Map<String,Object>> findBillDetail(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            String orderId = jsonObject.getString("orderId");

            Map<String,Object> map = billsService.findBillDetail(userId,orderId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     * 批量删除单据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public ResponseVo<String> deleteBill(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String userId = JwtToken.getClaim(request.getHeader("appkey"),"userId");
            billsService.deleteBill(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【单据请求类】批量删除单据异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 更新单据
     * @param jsonObject
     * @return
     */
    @PostMapping("/update_bill")
    public ResponseVo<String> updateBill(@RequestBody JSONObject jsonObject){
        try {
            billsService.updateBill(jsonObject);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【单据请求类】更新单据异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 更新单据详情
     * @param jsonObject
     * @return
     */
    @PostMapping("/update_bill_detail")
    public ResponseVo<String> updateBillDetail(@RequestBody JSONObject jsonObject){
        try {
            billsService.updateBillDetail(jsonObject);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【单据请求类】更新单据详情异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 更新车位单据
     * @param jsonObject
     * @return
     */
    @PostMapping("/update_parking_space_bill_detail")
    public ResponseVo<String> updateParkingSpaceBillDetail(@RequestBody JSONObject jsonObject){
        try {

            System.out.println("进来啊");

            billsService.updateParkingSpaceBillDetail(jsonObject);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【单据请求类】更新车位单据异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
