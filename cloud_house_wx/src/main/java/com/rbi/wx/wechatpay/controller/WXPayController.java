package com.rbi.wx.wechatpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.dto.paymentlist.PayCouponDTO;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.PayMentListRequestEntity;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.RoomCodeAndCouponRequestEntity;
import com.rbi.wx.wechatpay.service.impl.PayServiceImpl;
import com.rbi.wx.wechatpay.util.paymentlistutil.PayMentListUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WXPayController {

    @Autowired
    private PayServiceImpl payService;
    @PostMapping("/wx/property")
    @ResponseBody
    public JsonEntityUtil propertyPay(@RequestBody JSONObject jsonObject){
          return this.payService.getProperty(jsonObject.getDouble("roomSize"));
    }
    /**
     * 获取九宫格
     */
    @ApiOperation(value="缴费九宫格", notes="根据chargeCode和roomCode查询")
    @PostMapping("/wx/paymentlist")
    @ResponseBody
    public JsonEntityUtil<List<PayMentListUtil>> payMentLit(@RequestBody PayMentListRequestEntity payMentListRequestEntity){
        return this.payService.getPayMentList(payMentListRequestEntity.getRoomCode(),payMentListRequestEntity.getChargeCode(),payMentListRequestEntity.getOrganizationId());
    }
    @ApiOperation(value="缴费支付页面", notes="")
    @PostMapping("/wx/paycharge")
    @ResponseBody
    public JsonEntityUtil chargePay(@ApiParam(value = "实体")@RequestBody ChargePayRequestEntity chargePayRequestEntity){
        return this.payService.chargePay(chargePayRequestEntity);
    }

    @ApiOperation(value="/mine/tenantinfo&&/mine/deputyinfo&&/chargepay/tenant&&/chargepay/tenant&&  查询业主还是副业主", notes="根据userId和roomCode查询")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/wx/coupon")
    @ResponseBody
    public JsonEntityUtil<List<PayCouponDTO>> getCoupon(@ApiParam(value = "参数实体",required = true)@RequestBody RoomCodeAndCouponRequestEntity roomCodeAndCouponRequestEntity){
    return this.payService.getPayCoupon(roomCodeAndCouponRequestEntity.getRoomCode(),roomCodeAndCouponRequestEntity.getChargeCode());
    }
}
