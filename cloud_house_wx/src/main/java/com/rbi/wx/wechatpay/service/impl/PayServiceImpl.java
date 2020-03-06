package com.rbi.wx.wechatpay.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.common.interactive.PropertyFeeDueTime;
import com.rbi.wx.wechatpay.dto.paymentlist.PayCouponDTO;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.entity.MoneyMonth;
import com.rbi.wx.wechatpay.dto.paymentlist.PayMentListPropertyDTO;
import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.service.PayService;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import com.rbi.wx.wechatpay.util.chargepayfactory.ChargePayFactoryInterface;
import com.rbi.wx.wechatpay.util.chargepayfactory.ChargeTypeFactory;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.RoomPerproty;
import com.rbi.wx.wechatpay.util.paylistfactory.GetPayList;
import com.rbi.wx.wechatpay.util.paylistfactory.PayListFactory;
import com.rbi.wx.wechatpay.util.paymentlistutil.GetPayMentListJson;
import com.rbi.wx.wechatpay.util.paymentlistutil.PayMentListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private HTTPRequest httpRequest;
    @Autowired
    private PayMentMapper payMentMapper;
    @Reference
    private PropertyFeeDueTime propertyFeeDueTime;
    /**
     * 物业费 月数选择九宫格数据
     * @param roomSize
     * @return
     */
    @Override
    public JsonEntityUtil getProperty(Double roomSize) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String data = httpRequest.JsonRequest(Constants.DATE_URL + "", "POST", "qwer123");
            String discountData = httpRequest.JsonRequest(Constants.DATE_URL + "", "POST", "qwer123");
            JSONObject jsonObject = JSON.parseObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject discountJson = JSON.parseObject(discountData).getJSONObject("data");
            Map discountMap = discountJson.getInnerMap();
            Double standard = jsonObject1.getDouble("chargeStandard");
            Double sum = roomSize * standard;
            HashMap<String, MoneyMonth> map = new HashMap<>();
            for (double i = 1.0; i <= 12.0; i++) {
                double monthSum = sum * i;
                double dicount=0.1*Double.valueOf(discountMap.get(i + "").toString());
                map.put(i + "", new MoneyMonth(monthSum
                        , monthSum * i *dicount
                        , dicount));
            }
            jsonEntityUtil = new JsonEntityUtil("1000", "成功");
            jsonEntityUtil.setEntity(map);
        }catch (Exception e){
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;

    }

    @Override
    public JsonEntityUtil chargePay(ChargePayRequestEntity chargePayRequestEntity) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String dueTime=this.propertyFeeDueTime.propertyFeeDueTime(chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getOrganizationId());
            chargePayRequestEntity.setStartTime(dueTime);
            ChargePayFactoryInterface chargType=ChargeTypeFactory.getChargePay(this.payMentMapper.findType(chargePayRequestEntity.getChargeCode()));
            jsonEntityUtil = new JsonEntityUtil("1000", "成功");
            jsonEntityUtil.setEntity(chargType.getChargePayDTO(chargePayRequestEntity,this.payMentMapper));
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil<List<PayMentListUtil>> getPayMentList(String roomCode, String chargeCode,String organizationId) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            GetPayList getPayList= PayListFactory.getPayList(this.payMentMapper.findChargeTypeByChargeCode(chargeCode));

            return getPayList.getPayList(roomCode,organizationId,chargeCode,payMentMapper);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil<List<PayCouponDTO>> getPayCoupon(String roomCode, String chargeCode) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            List list=this.payMentMapper.findCoupon(roomCode,chargeCode);
            jsonEntityUtil=new JsonEntityUtil("1000","查询成功");
            jsonEntityUtil.setEntity(list);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }
}

