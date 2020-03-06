package com.rbi.wx.wechatpay.controller.wechatpaycontroller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.dto.paymentrecordentity.WxPayDTO;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.service.wechatservice.WxPayService;
import com.rbi.wx.wechatpay.util.IpUtil;
import com.rbi.wx.wechatpay.util.ResponseVo;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class WxPayController {

    private final static Logger logger = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private RSAGetStringUtil rsaGetStringUtil;
    /**
     * 进行有月数的缴费
     * JsonObject
     * openId:openid
     * body:缴费信息
     * userId;用户Id
     * MCHOpenId;商户公众号OpenId 还未启用
     * roomCode:房间编号
     * month:缴费月数
     * chargeCode;缴费项目ID
     */
    @PostMapping("/wx/wxchargepay")
    @ResponseBody
    public JsonEntityUtil<WxPayDTO> chargePay(@RequestBody ChargePayRequestEntity chargePayRequestEntity, HttpServletRequest request){
        //传 房间ID 缴费月数 项目ID  返回   缴纳的金额
       // String totalFee = jsonObject.getString("totalFee");
        String totalFee="1";
        String IP = IpUtil.getIpAddr(request);
        return wxPayService.wxPay(chargePayRequestEntity,IP,request.getHeader("APPKEY"));


       // return null;
    }



}
