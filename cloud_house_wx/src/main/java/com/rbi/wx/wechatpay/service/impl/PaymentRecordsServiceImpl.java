package com.rbi.wx.wechatpay.service.impl;



import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rbi.common.interactive.PropertyFeeDueTime;
import com.rbi.wx.wechatpay.dto.AllChargeDTO;
import com.rbi.wx.wechatpay.dto.PayMentRecordsDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayerPayListDTO;
import com.rbi.wx.wechatpay.entity.payment.PayMentEntity;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.indexroom.PayMentRoomDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayMentUserDTO;
import com.rbi.wx.wechatpay.dto.indexroom.PayerInfoDTO;
import com.rbi.wx.wechatpay.mapper.PaymentRecordsMapper;
import com.rbi.wx.wechatpay.service.PaymentRecordsService;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentRecordsServiceImpl implements PaymentRecordsService{
    @Autowired
    private PaymentRecordsMapper paymentRecordsMapper;
    @Autowired
    private RSAGetStringUtil rsaGetStringUtil;
    @Autowired
    private RedisUtil redisUtil;
    private final static Logger logger = LoggerFactory.getLogger(PaymentRecordsServiceImpl.class);
    /**
     * 获取房间余额
     * @param roomCode
     * @param chargeCode
     * @return
     */
    @Override
    public JsonEntityUtil payMent(String roomCode, String chargeCode) {
        JsonEntityUtil jsonEntityUtil=null;
        try{
        double surplus=this.paymentRecordsMapper.getSurplus(roomCode,chargeCode);
        jsonEntityUtil=new JsonEntityUtil("1000","成功");
        jsonEntityUtil.setEntity(surplus);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

//    /**
//     * 获取缴费菜单
//     * @param roomCode
//     * @return
//     */
//    @Override
//    public JsonEntityUtil getPayment(String roomCode) {
//        JsonEntityUtil jsonEntityUtil=null;
//        try {
//            List<PayMentEntity> payMent=this.paymentRecordsMapper.chargeList(roomCode);
//            payMent.add(this.paymentRecordsMapper.getRecords(roomCode));
//            jsonEntityUtil=new JsonEntityUtil("1000","成功");
//            for (PayMentEntity s:payMent){
//                if (s!=null)
//                s.setStatus(this.paymentRecordsMapper.getCharType(s.getChargeCode()));
//            }
//            jsonEntityUtil.setEntity(payMent);
//
//        }catch (Exception e){
//            e.printStackTrace();
//            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
//        }
//        return jsonEntityUtil;
//    }



    @Reference()
    private PropertyFeeDueTime propertyFeeDueTime;

    /**
     * 获取缴费菜单重构方法
     * @param roomCode
     * @return
     */
    @Override
    public JsonEntityUtil getPayment(String roomCode,String organizationId) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            List<PayMentEntity> payMent=new ArrayList<>();
            payMent.add(this.paymentRecordsMapper.getPayMent(roomCode,organizationId));
            //String organizationId=this.paymentRecordsMapper.getOrganizationId(roomCode);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            Integer monthlyArrears=0;
            Double amountOfArrears=0.0;
            for (PayMentEntity s:payMent){
                    String dueTime=this.propertyFeeDueTime.propertyFeeDueTime(roomCode,organizationId);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String todayDate= DateUtil.date("yyyy-MM-dd");
                    Date date1 = format.parse(todayDate);
                    Date date2 = format.parse(dueTime);
                    int compareTo = date1.compareTo(date2);
                    if (compareTo==1){
                        //已欠费
                        Integer day=Integer.valueOf(todayDate.substring(8));
                        Integer dueDay=Integer.valueOf(dueTime.substring(8));
                        monthlyArrears =DateUtil.getMonth(date2,date1);
                        if (day>=dueDay){
                            monthlyArrears+=1;
                        }
                        s.setMonthlyArrears(monthlyArrears+"");
                       Double roomSize=this.paymentRecordsMapper.getRoomSize(roomCode);
                       Double stande=this.paymentRecordsMapper.getChargeStandard(s.getChargeCode());
                       amountOfArrears=roomSize*stande*monthlyArrears;
                       s.setDueTime(dueTime);
                       s.setAmountOfArrears(amountOfArrears+"");
                        s.setStateOfArrears("欠费");
                        s.setColor("Red");
                        s.setStatus("true");
                    }else {
                        s.setDueTime(dueTime);
                        s.setAmountOfArrears(amountOfArrears+"");
                        s.setMonthlyArrears(monthlyArrears+"");
                        s.setStateOfArrears("正常");
                        s.setColor("green");
                        s.setStatus("true");
                    }
                    this.redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+organizationId+"-"+roomCode,dueTime);
            }
            jsonEntityUtil.setEntity(payMent);

        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getPaymentRecords(String userIdAppKey,Integer pageSize,Integer pageNum) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List<PayMentRecordsDTO> list=this.paymentRecordsMapper.getPaymentRecords(userId,(pageNum-1)*10,10);
            jsonEntityUtil=new JsonEntityUtil("1000","查询成功");
            jsonEntityUtil.setEntity(list);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1003","服务器异常");
        }
        return jsonEntityUtil;
    }
    @Override
    public JsonEntityUtil getPayMentUser(Integer pageSize,Integer pageNum,String roomCode){
        JsonEntityUtil jsonEntityUtil=null;
        try {
            List<PayMentUserDTO> list=this.paymentRecordsMapper.getPayMentListByRoomCode(roomCode,10,(pageNum-1)*10);
//            for (PayMentUserDTO payMentUserDTO:list){
//                JSONArray json= JSON.parseArray(payMentUserDTO.getChargeName());
//                AllChargeDTO allChargeDTO=json.getObject(0, AllChargeDTO.class);
//                payMentUserDTO.setChargeName(allChargeDTO.getChargeName());
//            }
         jsonEntityUtil=new JsonEntityUtil("1000","成功");
         jsonEntityUtil.setEntity(list);
        jsonEntityUtil.setEntity(list);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","失败");
        }
       return jsonEntityUtil;
    }
    @Override
    public JsonEntityUtil getPayerPayList(Integer pageSize, Integer pageNum, String roomCode, String userId){
        JsonEntityUtil jsonEntityUtil=null;
        try {
           // String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List<PayerPayListDTO> list=new ArrayList<>();
            list=this.paymentRecordsMapper.getPayerPayList(roomCode,userId,(pageNum-1)*10,10);
            com.rbi.wx.wechatpay.dto.paymentlist.PayerInfoDTO payerInfoDTO=this.paymentRecordsMapper.getPayerInfoSimple(userId);
            payerInfoDTO.setPayList(list);
           jsonEntityUtil=new JsonEntityUtil("1000","成功");
           jsonEntityUtil.setEntity(payerInfoDTO);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","失败");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getPayRoom(String roomCode) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.paymentRecordsMapper.getProperty(roomCode);
            PayMentRoomDTO payMentRoom=this.paymentRecordsMapper.getPayMentRoom(userId,roomCode);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(payMentRoom);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","失败");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getPayerInfo(String userIdAppKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            PayerInfoDTO payerInfoDTO=this.paymentRecordsMapper.getPayerInfo(userId);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(payerInfoDTO);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","失败");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getPayMentCoupon(String userIdAppKey,String roomCode, String chargeCode, Integer payMonth) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");

        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","失败");
        }
        return jsonEntityUtil;
    }
}
