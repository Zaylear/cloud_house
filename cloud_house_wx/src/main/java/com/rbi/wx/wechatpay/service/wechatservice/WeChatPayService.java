package com.rbi.wx.wechatpay.service.wechatservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.rbi.common.interactive.CostAdd;
import com.rbi.common.interactive.PropertyFeeDueTime;
import com.rbi.common.interactive.ThisSystemOrderId;
import com.rbi.wx.wechatpay.controller.wechatpaycontroller.WxPayController;
import com.rbi.wx.wechatpay.dto.paysuecces.ParkingSpaceRentalRoomDTO;
import com.rbi.wx.wechatpay.dto.paysuecces.ParkingSpaceRentalUserDTO;
import com.rbi.wx.wechatpay.mapper.PaySuccessMapper;
import com.rbi.wx.wechatpay.mapper.PaymentRecordsMapper;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.util.*;
import com.rbi.wx.wechatpay.util.modulemsg.ModuleMsg;
import com.rbi.wx.wechatpay.util.modulemsg.demo.ChchingConfig;
import com.rbi.wx.wechatpay.util.modulemsg.demo.ModuleConfig;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import com.rbi.wx.wechatpay.util.successfactory.SuccessFactory;
import com.rbi.wx.wechatpay.util.successfactory.SuccessInterface;
import com.rbi.wx.wechatpay.util.successmapperresult.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

@Service
public class WeChatPayService {

    @Reference()
    private ThisSystemOrderId thisSystemOrderId;
     @Reference()
     private PropertyFeeDueTime propertyFeeDueTime;
    @Autowired
    private RedisUtil redisUtil;
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(WxPayController.class);
    private final String SUCCESSMSG="<xml> \n" +
            "\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml> \n";
    /**
     * 当支付成功的时候回调url传回数据
     * 如果数据无误保存到数据库
     * @param response
     * @param request
     */
      public void success(HttpServletResponse response,HttpServletRequest request){
             Map<String,String> msgMap = null;
             String dueTime="";
        try {
       msgMap=XmlUtil.xmlToMap(request);
            this.redisUtil.setRedisDb(1);
         Map redisMap=this.redisUtil.hmget(msgMap.get("out_trade_no"));
        // Map redisMap=this.redisUtil.hmget("1576721528369");
        //            Map redisMap=new HashMap();
        //            redisMap.put("roomCode","WLC-NH01-8-1-2303");
        //            redisMap.put("userId","AF8214FED01FADD0CE2AA71813E566C9");
        //            redisMap.put("chargeCode","WLCWYFUZX-WYF.ZZ");
        //            redisMap.put("datedif","12");
        //            redisMap.put("couponId","-1");
            RoomInfo roomInfo=new RoomInfo();
                try {
                    roomInfo=paySuccessMapper.getRoomInf(redisMap.get("roomCode").toString());
                    //roomInfo=paySuccessMapper.getRoomInf("WLC-NH01-9-2-401");
                    try {
                        dueTime=propertyFeeDueTime.propertyFeeDueTime(roomInfo.getRoomCode(),roomInfo.getOrganizationId());
                        redisMap.put("dueTime",dueTime);
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.info("新增数据出现错误");

                    }
                    this.redisUtil.setRedisDb(1);
                    String orderId=this.thisSystemOrderId.thisSystemOrderId(roomInfo.getOrganizationId());
                    if (redisMap.get("type").equals("zulin")){
                        zulin(redisMap);
                    }else if (redisMap.get("type").equals("guanli")){
                        guanli(redisMap);
                    }
                    redisMap.put("orderId",orderId);
                    SuccessInterface success= SuccessFactory.getSuccess(this.paySuccessMapper.chargeType(redisMap.get("chargeCode").toString()));
                    success.addOriginalBill(redisMap,paySuccessMapper,rsaGetStringUtil);
                    this.redisUtil.setRedisDb(2);
                    redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+roomInfo.getOrganizationId()+"-"+roomInfo.getRoomCode(),"NAN");
                    this.redisUtil.setRedisDb(1);
            }catch (Exception e){
                e.printStackTrace();
                logger.info("新增数据出现错误");
            }
//
//          String i="-1";
//            if (i.equals("-1")){
//                return;
//            }
           // Set<String> set=redisMap.keySet();
//            for (String s:set){
//                System.out.println("Redis的数据---->"+s+":"+redisMap.get(s));
//            }
            OutputStream outputStream=response.getOutputStream();
            outputStream.write(SUCCESSMSG.getBytes("UTF-8"));
            outputStream.close();
            ChchingConfig chchingConfig=new ChchingConfig(redisUtil);
            ModuleConfig moduleConfig=new ModuleConfig();
            ModuleMsg moduleMsg=new ModuleMsg(chchingConfig,moduleConfig);
            HashMap<String,String> map=new HashMap<>();
            map.put("first","物业费缴费成功");
            map.put("keyword1","香瓜");
           // map.put("keyword2",redisMap.get("total_fee").toString());
            map.put("keyword3","微信支付");
            map.put("keyword4",DateUtil.date("yyyy-mm-dd"));
            map.put("keyword5","费用");
            map.put("remark","请查看订单");
          //  map.put("openId",redisMap.get("openid").toString());
            map.put("url","www.baidu.com");
         moduleMsg.senMsg(map,"3j4vqP_jP8kPeTOOVootHTPHJ6yA234KrxC5rVupbHA");
            logger.info("订单号"+msgMap.get("out_trade_no")+"已完成");
        } catch (Exception e) {
            logger.info("订单号"+msgMap.get("out_trade_no")+"出现异常");
            e.printStackTrace();
        }
    }
    public  Boolean zulin (Map dataMap) throws Exception{
        JSONObject reponseData=new JSONObject();
        ParkingSpaceRentalRoomDTO parkingSpaceRentalRoomDTO=this.paymentRecordsMapper.getRoomInfo(dataMap.get("roomCode").toString()
                ,dataMap.get("organizationId").toString());
        ParkingSpaceRentalUserDTO parkingSpaceRentalUserDTO=this.paymentRecordsMapper.getUserInfo(dataMap.get("userId").toString());
        /**
         * 基本参数
         */
        reponseData.put("paymentMethod","8");
        reponseData.put("organizationId",dataMap.get("organizationId").toString());
        reponseData.put("villageName",parkingSpaceRentalRoomDTO.getVillageName());
        reponseData.put("villageCode",parkingSpaceRentalRoomDTO.getVillageCode());
        reponseData.put("regionCode",parkingSpaceRentalRoomDTO.getRegionCode());
        reponseData.put("regionName",parkingSpaceRentalRoomDTO.getRegionName());
        reponseData.put("buildingCode",parkingSpaceRentalRoomDTO.getBuildingCode());
        reponseData.put("buildingName",parkingSpaceRentalRoomDTO.getBuildingName());
        reponseData.put("unitCode",parkingSpaceRentalRoomDTO.getUnitCode());
        reponseData.put("unitName",parkingSpaceRentalRoomDTO.getUnitName());
        reponseData.put("roomCode",parkingSpaceRentalRoomDTO.getRoomCode());
        reponseData.put("roomSize",parkingSpaceRentalRoomDTO.getRoomSize());
        reponseData.put("surname",parkingSpaceRentalUserDTO.getSurname());
        reponseData.put("mobilePhone",parkingSpaceRentalUserDTO.getMobilePhone());
        reponseData.put("idNumber",parkingSpaceRentalUserDTO.getIdNumber());

        reponseData.put("billDetailedDOArrayList",new ArrayList<>());
        reponseData.put("costDeduction",new ArrayList<>());

        /**
         * 订单参数
         */
        JSONArray jsonArray=JSONArray.parseArray(dataMap.get("data").toString());
        reponseData.put("parkingSpaceCostDetailDOList",jsonArray);
        Double actualMoneyCollection=getActualMoneyCollection(jsonArray);
        reponseData.put("correctedAmount",0);
        reponseData.put("amountTotalReceivable",actualMoneyCollection);
        reponseData.put("actualTotalMoneyCollection",actualMoneyCollection);
        return this.costAdd.addBill(reponseData,null);
}

    public  Boolean guanli (Map dataMap) throws Exception{
        JSONObject reponseData=new JSONObject();
        ParkingSpaceRentalRoomDTO parkingSpaceRentalRoomDTO=this.paymentRecordsMapper.getRoomInfo(dataMap.get("roomCode").toString()
                ,dataMap.get("organizationId").toString());
        ParkingSpaceRentalUserDTO parkingSpaceRentalUserDTO=this.paymentRecordsMapper.getUserInfo(dataMap.get("userId").toString());
        /**
         * 基本参数
         */
        reponseData.put("paymentMethod","8");
        reponseData.put("organizationId",dataMap.get("organizationId").toString());
        reponseData.put("villageName",parkingSpaceRentalRoomDTO.getVillageName());
        reponseData.put("villageCode",parkingSpaceRentalRoomDTO.getVillageCode());
        reponseData.put("regionCode",parkingSpaceRentalRoomDTO.getRegionCode());
        reponseData.put("regionName",parkingSpaceRentalRoomDTO.getRegionName());
        reponseData.put("buildingCode",parkingSpaceRentalRoomDTO.getBuildingCode());
        reponseData.put("buildingName",parkingSpaceRentalRoomDTO.getBuildingName());
        reponseData.put("unitCode",parkingSpaceRentalRoomDTO.getUnitCode());
        reponseData.put("unitName",parkingSpaceRentalRoomDTO.getUnitName());
        reponseData.put("roomCode",parkingSpaceRentalRoomDTO.getRoomCode());
        reponseData.put("roomSize",parkingSpaceRentalRoomDTO.getRoomSize());
        reponseData.put("surname",parkingSpaceRentalUserDTO.getSurname());
        reponseData.put("mobilePhone",parkingSpaceRentalUserDTO.getMobilePhone());
        reponseData.put("idNumber",parkingSpaceRentalUserDTO.getIdNumber());
        reponseData.put("payerPhone",parkingSpaceRentalUserDTO.getMobilePhone());
        reponseData.put("payerName",parkingSpaceRentalUserDTO.getSurname());
        reponseData.put("billDetailedDOArrayList",new ArrayList<>());
        reponseData.put("costDeduction",new ArrayList<>());
        /**
         * 订单参数
         */
        JSONArray jsonArray=JSONArray.parseArray(dataMap.get("data").toString());
        reponseData.put("parkingSpaceCostDetailDOList",jsonArray);
        Double actualMoneyCollection=getActualMoneyCollection(jsonArray);
        reponseData.put("correctedAmount",0);
        reponseData.put("amountTotalReceivable",actualMoneyCollection);
        reponseData.put("actualTotalMoneyCollection",actualMoneyCollection);
        return this.costAdd.addBill(reponseData,null);
    }
    public Double getActualMoneyCollection(JSONArray jsonArray){
        Double actualMoneyCollection=0.0;
        for (Object o:jsonArray){
            Double actualMoneyCollection1=0.0;
            JSONObject jsonObject=(JSONObject) o;
            if (jsonObject.getDouble("actualMoneyCollection")==null){
                actualMoneyCollection1=0.0;
            }else {
                actualMoneyCollection=jsonObject.getDouble("actualMoneyCollection");
            }
            actualMoneyCollection+=actualMoneyCollection1;
        }
        return actualMoneyCollection;
    }

    @Reference
    private CostAdd costAdd;
    @Autowired
    private HTTPRequest httpRequest;
    @Autowired
    private PaymentRecordsMapper paymentRecordsMapper;
    @Autowired
    private PaySuccessMapper paySuccessMapper;
    @Autowired
    private RSAGetStringUtil rsaGetStringUtil;
    /**
     * 对OriginalBill需要的对象进行组装
     * @param map
     */
//    public boolean addOriginalBill(Map map) {
//        try {
//            PayMentChargeEntity payMentChargeEntity=this.paySuccessMapper.getPayMentCharge(map.get("chargeCode").toString(),map.get("datedif").toString());
//            PayMentPayerEntity payMentPayerEntity=this.paySuccessMapper.getPayMentPayerEntity(this.rsaGetStringUtil.getPublicPassword(map.get("userId").toString()));
//            PayMentRoomEntity payMentRoomEntity=this.paySuccessMapper.getPayMentRoomEntity(map.get("roomCode").toString());
//            PayMentCouponEntity payMentCouponEntity=this.paySuccessMapper.getPayMentCouponEntity(map.get("couponId").toString());
//            Integer Datedif=1;
//            if (payMentChargeEntity.getDatedif()>0||payMentChargeEntity.getDatedif()!=null){
//                Datedif=payMentChargeEntity.getDatedif();
//            }
//            double amountReceivable=payMentChargeEntity.getChargeStandard()
//                    *payMentRoomEntity.getRoomSize()
//                    *Datedif;
//            double actualMoneyCollection=amountReceivable*payMentChargeEntity.getDiscount()*0.1;
//            double preferentialAmount=amountReceivable-actualMoneyCollection;
//            double couponMoney=0.0;
//            if (payMentCouponEntity.getBalanceAmount()>actualMoneyCollection){
//                couponMoney=actualMoneyCollection;
//                actualMoneyCollection=0.0;
//                this.paySuccessMapper.updateCoupon(couponMoney,payMentCouponEntity.getId().toString());
//            }else {
//                couponMoney=payMentCouponEntity.getBalanceAmount();
//                actualMoneyCollection-=couponMoney;
//                this.paySuccessMapper.updateCoupon(couponMoney,payMentCouponEntity.getId().toString());
//                this.paySuccessMapper.updateType(payMentCouponEntity.getId().toString());
//            }
//            String orderId=map.get("out_trade_no").toString();
//            PayMentRecordEntity payMentRecordEntity=new PayMentRecordEntity();
//            payMentRecordEntity.setOrderId(orderId);
//            payMentRecordEntity.setActualMoneyCollection((float) actualMoneyCollection);
//            payMentRecordEntity.setAmountReceivable((float) amountReceivable);
//            payMentRecordEntity.setPreferentialAmount(preferentialAmount+"");
//            this.paySuccessMapper.addOriginalBill(payMentRecordEntity,payMentChargeEntity
//                    ,payMentRoomEntity,payMentPayerEntity,payMentPayerEntity.getUserPhone(),payMentCouponEntity,couponMoney);
//            return true;
//        } catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
}
