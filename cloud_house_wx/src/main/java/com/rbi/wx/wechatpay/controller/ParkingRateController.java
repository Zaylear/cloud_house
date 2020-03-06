package com.rbi.wx.wechatpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.service.ParkingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 停车位缴费
 */

@Controller
public class ParkingRateController {

    @Autowired
    private ParkingRateService parkingRateService;

    /**
     * 车位租赁费 费用计算
     * @param jsonObject
     *
     *{
     *     parkingSpaceCostDetailDO:
     *     {
     *         roomCode:""
     *         datedif:""
     *         licensePlateNumber:""
     *         rentalRenewalStatus:""
     *     }
     *     organizationId:""
     *}
     *
     * @return
     */
    @PostMapping("/parkingRate/getInfoByLicensePlate")
    @ResponseBody
    public JsonEntityUtil getInfoByLicensePlate(@RequestBody JSONObject jsonObject){
        return this.parkingRateService.getInfoByLicensePlate(jsonObject);
    }


    /**
     * 微信支付
     * @param jsonObject
     * @return
     */
    @PostMapping("/parkingRate/WxPay")
    @ResponseBody
    public JsonEntityUtil wxPay(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        jsonObject.put("APPKEY",request.getHeader("APPKEY"));
        return this.parkingRateService.wxPay(jsonObject);
    }

    /**
     * 微信支付  车位管理
     * @param jsonObject
     * @return
     */
    @PostMapping("/parkingRate/parkingRateWxPay")
    @ResponseBody
    public JsonEntityUtil parkingRateWxPay(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        jsonObject.put("APPKEY",request.getHeader("APPKEY"));
        return this.parkingRateService.parkingRatewxPay(jsonObject);
    }
    /**
     * 车位租赁费 获取房间列表
     * @param jsonObject
     * @return
     */
    @PostMapping("/parkingRate/getRoomCode")
    @ResponseBody
    public JsonEntityUtil getRoomCode(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        jsonObject.put("APPKEY",request.getHeader("APPKEY"));
        return this.parkingRateService.getRoomCode(jsonObject);
    }

    /**
     * 车位管理费  获取合同编号
     * @param jsonObject
     * @return
     */
    @PostMapping("/parkingRate/getParkingSpaceCode")
    @ResponseBody
    public JsonEntityUtil getParkingSpaceCode(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        jsonObject.put("APPKEY",request.getHeader("APPKEY"));
        return this.parkingRateService.getParkingSpaceCode(jsonObject);
    }

    /**
     * 车位管理费 费用计算
     * @param jsonObject
     *
     * {
     *     organizationId:""
     *     parkingSpaceCode:""
     *     datedif:""
     * }
     *
     * @return
     */
    @PostMapping("/parkingRate/getInfoByParkingSpaceCode")
    @ResponseBody
    public JsonEntityUtil getInfoByParkingSpaceCode(@RequestBody JSONObject jsonObject){
        return this.parkingRateService.getInfoByParkingSpaceCode(jsonObject);
    }


}
