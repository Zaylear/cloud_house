package com.rbi.wx.wechatpay.controller;

import com.rbi.wx.wechatpay.controller.wechatpaycontroller.WxPayController;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.*;
import com.rbi.wx.wechatpay.dto.PayMentRecordsDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayMentRoomDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayMentUserDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayerInfoDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayerPayListDTO;
import com.rbi.wx.wechatpay.entity.payment.PayMentEntity;
import com.rbi.wx.wechatpay.service.impl.PaymentRecordsServiceImpl;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "PayAllAPI",value = "")
@Controller
public class PaymentRecordsController {
    @Autowired
    private PaymentRecordsServiceImpl paymentRecordsService;

    /**
     * 缴费记录
     * 我的缴费明细
     * @param getPayMentRecordsRequestEntity
     * @param request
     * @return
     */
    @ApiOperation(value="获取用户缴费信息", notes="根据userId获取缴费信息 并且开启了分页")
    @PostMapping("/wx/getpaymentrecords")
    @ResponseBody
    public JsonEntityUtil<List<PayMentRecordsDTO>> getPaymentRecords(@RequestBody @ApiParam(value = "分页查询的参数和userId",required = true) GetPayMentRecordsRequestEntity getPayMentRecordsRequestEntity, HttpServletRequest request){
        return this.paymentRecordsService.getPaymentRecords(request.getHeader("APPKEY"),
               getPayMentRecordsRequestEntity.getPageSize(),
                getPayMentRecordsRequestEntity.getPageNum());
    }

    /**
     * 缴费 获取缴费项目菜单
     *
     * 费用明细
     * 查看有哪些项目欠费或者不欠费
     * @param getPayMentRequestEntity
     * @return
     */

    @ApiOperation(value="查看有哪些项目欠费或者不欠费", notes="根据roomCode获取欠费信息")
    @PostMapping("/wx/getpayment")
    @ResponseBody
    public JsonEntityUtil<List<PayMentEntity>> getPayment(@RequestBody @ApiParam(value = "房间编号",required = true) GetPayMentRequestEntity getPayMentRequestEntity){
        return this.paymentRecordsService.getPayment(getPayMentRequestEntity.getRoomCode(),getPayMentRequestEntity.getOrganizationId());
    }

    /**
     * 缴费页面
     * @param payMentRequestEntity
     * @return
     */
    @ApiOperation(value="缴费页面", notes="根据roomCode和chargeCode来进行缴费")
    @PostMapping("/wx/payment")
    @ResponseBody
    public JsonEntityUtil<Double> payMent(@RequestBody @ApiParam(value = "参数实体",required = true) PayMentRequestEntity payMentRequestEntity){
        return this.paymentRecordsService.payMent(payMentRequestEntity.getRoomCode(),payMentRequestEntity.getChargeCode());
    }

    /**
     * 房屋详情里面的缴费明细
     * 获取房屋信息的接口
     * @param payMentRoomRequestEntity
     * @return
     */
    @ApiOperation(value="房屋详情里面的缴费明细里面的房屋信息", notes="根据roomCode来获取缴费明细")
    @PostMapping("/wx/payment/room")
    @ResponseBody
    public JsonEntityUtil<PayMentRoomDTO> payMentRoom(@RequestBody  @ApiParam(value = "参数实体",required = true) PayMentRoomRequestEntity payMentRoomRequestEntity){
        return this.paymentRecordsService.getPayRoom(payMentRoomRequestEntity.getRoomCode());
    }

    /**
     * 房屋详情里面的缴费明细
     * 获取房屋缴费信息的接口
     * roomCode 房屋ID
     * pageSize 单页大小
     * pageNum  查询第几页
     * @param payMentUserRquestEntity
     * @return
     */
    @ApiOperation(value="/pay/detail 房屋详情里面的缴费明细里面的下部分的账单", notes="根据roomCode获取历史账单 并且开启了分页")
    @PostMapping("/wx/payment/user")
    @ResponseBody
    public JsonEntityUtil<List<PayMentUserDTO>> payMentUser(@RequestBody  @ApiParam(value = "参数实体",required = true) PayMentUserRquestEntity payMentUserRquestEntity){
        return this.paymentRecordsService.getPayMentUser(payMentUserRquestEntity.getPageSize(),
               payMentUserRquestEntity.getPageNum(),
               payMentUserRquestEntity.getRoomCode());
    }
    /**
     * 获取缴费人明细
     * 缴费人信息
     * @param request
     * @return
     */
    @ApiOperation(value="/mine/payinfo  缴费明细", notes="根据userId获取历史账单 并且开启了分页")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/roomindex/payerinfo")
    @ResponseBody
    public JsonEntityUtil<PayerInfoDTO> getPayerInfo(HttpServletRequest request){
        return this.paymentRecordsService.getPayerInfo(request.getHeader("APPKEY"));

    }

    /**
     * 获取缴费人明细
     * 该缴费人的历史账单
     * pageSize 单页大小
     * pageNum  查询第几页
     * roomCode 查询房间
     * userId   查询谁的记录
     * @param roomIndexPayListRquestEntity
     * @return
     */
    @ApiOperation(value="/chargepay/userinfo  房屋详情里面的缴费明细里面的历史账单", notes="根据roomCode获取历史账单 并且开启了分页")
    @PostMapping("/roomindex/paylist")
    @ResponseBody
    public JsonEntityUtil<List<PayerPayListDTO>> getPayerPayList(@RequestBody  @ApiParam(value = "参数实体",required = true) RoomIndexPayListRquestEntity roomIndexPayListRquestEntity, HttpServletRequest request){
        return this.paymentRecordsService.getPayerPayList(roomIndexPayListRquestEntity.getPageSize(),
        roomIndexPayListRquestEntity.getPageNum(),
                roomIndexPayListRquestEntity.getRoomCode(),
                roomIndexPayListRquestEntity.getUserId());
    }

    @ApiOperation(value="/chargepay/userinfo  房屋详情里面的缴费明细里面的历史账单", notes="根据roomCode获取历史账单 并且开启了分页")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/payment/getcoupon")
    @ResponseBody
    public  JsonEntityUtil getPayMentCoupon(@RequestBody @ApiParam(value = "参数实体",required = true) GetPayMentCouponRequestEntity getPayMentCouponRequestEntity,HttpServletRequest request){
        return this.paymentRecordsService.getPayMentCoupon(request.getHeader("APPKEY"),
                getPayMentCouponRequestEntity.getRoomCode(),
                getPayMentCouponRequestEntity.getChargeCode(),
                getPayMentCouponRequestEntity.getPayMonth());
    }

}
