package com.rbi.wx.wechatpay.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.common.interactive.ParkingManagementFeeCost;
import com.rbi.common.interactive.ParkingSpaceRentalFeeCost;
import com.rbi.wx.config.MyConfig;
import com.rbi.wx.wechatpay.dto.parkingrate.ParkingManagement;
import com.rbi.wx.wechatpay.dto.parkingrate.Room;
import com.rbi.wx.wechatpay.dto.paymentrecordentity.WxPayDTO;
import com.rbi.wx.wechatpay.mapper.ParkingRateMapper;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.service.ParkingRateService;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.WeChatCreateSign;
import com.rbi.wx.wechatpay.util.official.WXPay;
import com.rbi.wx.wechatpay.util.official.WXPayConstants;
import com.rbi.wx.wechatpay.util.official.WXPayUtil;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.rbi.wx.wechatpay.util.Constants.APPID;
import static com.rbi.wx.wechatpay.util.Constants.MCH_KEY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ParkingRateServiceImpl implements ParkingRateService {
    private final static Logger logger = LoggerFactory.getLogger(ParkingRateServiceImpl.class);
    @Reference
    private ParkingSpaceRentalFeeCost parkingSpaceRentalFeeCost;
    @Reference
    private ParkingManagementFeeCost parkingManagementFeeCost;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RSAGetStringUtil rsaGetStringUtil;
    @Autowired
    private ParkingRateMapper parkingRateMapper;
    @Override
    public JsonEntityUtil getInfoByLicensePlate(JSONObject jsonObject) {

        JsonEntityUtil jsonEntityUtil=null;
        try{
            jsonObject.put("rentalRenewalStatus","1");
            JSONObject jsonObject1=this.parkingSpaceRentalFeeCost.parkingSpaceRentalFee(jsonObject,jsonObject.getString("organizationId"));
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(jsonObject1);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;

    }

    @Override
    public JsonEntityUtil wxPay(JSONObject jsonObject)
    {
        try {
            JSONObject jsonObject1=this.parkingSpaceRentalFeeCost.parkingSpaceRentalFee(jsonObject,jsonObject.getString("organizationId"));
            double totalFee= getActualMoneyCollection(jsonObject1.getJSONArray("data"));
//         totalFee = chargeReal.getMoney(chargePayRequestEntity,chargeRealMapper);
            totalFee=Double.valueOf(totalFee)*10;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String fee=(totalFee+"").replace(".","");
            redisUtil.setRedisDb(1);
            SortedMap<String,String> reqMap = new TreeMap<>();
            reqMap.put("body","车位缴费");// 商品描述
            reqMap.put("total_fee", fee);
            //  reqMap.put("total_fee", "1");
            // reqMap.put("total_fee", (int)fee+"");// 总金额
            //   System.out.println((int)fee+"");
            reqMap.put("spbill_create_ip","127.0.0.1");//終端IP
            reqMap.put ("openid", jsonObject.getString("openId"));//关注微信公众号获取的openid
            setMap(reqMap);
            if (WXPayUtil.isSignatureValid(reqMap, Constants.MCH_KEY, WXPayConstants.SignType.HMACSHA256)) {
                logger.info("【微信支付信息生成服务类】签名效验成功");
            } else {
                logger.info("【微信支付信息生成服务类】签名效验失败");
            }
            MyConfig myWxPayConfig = new MyConfig();
            WXPay wxpay = new WXPay(myWxPayConfig);
            Map<String, String> response = wxpay.unifiedOrder(reqMap);
            reqMap.put ("userId", this.rsaGetStringUtil.getPrivatePassword(jsonObject.getString("APPKEY")));
            reqMap.put ("roomCode", jsonObject.getString("roomCode"));
            reqMap.put("organizationId",jsonObject.getString("organizationId"));
            reqMap.put("data",jsonObject1.getJSONArray("data").toJSONString());
            reqMap.put("type","zulin");
            this.redisUtil.hmset(reqMap.get("out_trade_no"),(Map)reqMap,86400);
            System.out.println("response:  "+response.toString());
            String returnCode = response.get("return_code");
            JsonEntityUtil jsonEntityUtil=null;
            if (!"SUCCESS".equals(returnCode)) {
                logger.error("【微信支付信息生成服务类】生成微信支付信息失败！");
                return jsonEntityUtil=new JsonEntityUtil("1003","生成微信支付信息失败");
            }
            System.out.println(reqMap.toString());
            //  Map<String,String> backMap = new HashMap<>();
            System.out.println("返回的集合"+response.toString());
            HashMap<String,String> backMap=new HashMap<>();
            backMap.put("nonceStr",response.get("nonce_str"));
            backMap.put("package","prepay_id="+response.get("prepay_id"));
            backMap.put("signType","HMAC-SHA256");
            backMap.put("appId",Constants.APPID);
            backMap.put("timeStamp",(System.currentTimeMillis() / 1000)+"");
            String backMapSign=WXPayUtil.generateSignature(backMap, Constants.MCH_KEY, WXPayConstants.SignType.HMACSHA256);
            System.out.println("自己生成的签名---"+backMapSign);
            backMap.put("paySign",backMapSign);
            WxPayDTO wxpaydto=new WxPayDTO();
            wxpaydto.setNonceStr(backMap.get("nonceStr"));
            wxpaydto.setPackagedto(backMap.get("package"));
            wxpaydto.setPaySign(backMap.get("paySign"));
            wxpaydto.setSignType("HMAC-SHA256");
            wxpaydto.setTimeStamp(backMap.get("timeStamp"));
            logger.info("【微信支付信息生成服务类】生成微信支付信息成功！");
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(wxpaydto);
            return jsonEntityUtil;
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonEntityUtil("1003","生成微信支付信息失败");
        }
    }

    @Override
    public JsonEntityUtil parkingRatewxPay(JSONObject jsonObject) {
        try {
            JSONObject jsonObject1=this.parkingManagementFeeCost.parkingManagementFee(jsonObject,jsonObject.getString("organizationId"));
            double totalFee= jsonObject1.getJSONObject("data").getDouble("actualMoneyCollection");
//         totalFee = chargeReal.getMoney(chargePayRequestEntity,chargeRealMapper);
            totalFee=Double.valueOf(totalFee)*10;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String fee=(totalFee+"").replace(".","");
            redisUtil.setRedisDb(1);
            SortedMap<String,String> reqMap = new TreeMap<>();
            reqMap.put("body","车位管理费");// 商品描述
            reqMap.put("total_fee", fee);
            //  reqMap.put("total_fee", "1");
            // reqMap.put("total_fee", (int)fee+"");// 总金额
            //   System.out.println((int)fee+"");
            reqMap.put("spbill_create_ip","127.0.0.1");//終端IP
            reqMap.put ("openid", jsonObject.getString("openId"));//关注微信公众号获取的openid
            setMap(reqMap);
            if (WXPayUtil.isSignatureValid(reqMap, Constants.MCH_KEY, WXPayConstants.SignType.HMACSHA256)) {
                logger.info("【微信支付信息生成服务类】签名效验成功");
            } else {
                logger.info("【微信支付信息生成服务类】签名效验失败");
            }
            MyConfig myWxPayConfig = new MyConfig();
            WXPay wxpay = new WXPay(myWxPayConfig);
            Map<String, String> response = wxpay.unifiedOrder(reqMap);
            reqMap.put ("userId", this.rsaGetStringUtil.getPrivatePassword(jsonObject.getString("APPKEY")));
            reqMap.put ("roomCode", jsonObject.getString("roomCode"));
            reqMap.put("organizationId",jsonObject.getString("organizationId"));
            JSONArray jsonArray=new JSONArray();
            jsonArray.add(jsonObject1.get("data"));
            reqMap.put("data",jsonArray.toJSONString());
            reqMap.put("type","guanli");
            this.redisUtil.hmset(reqMap.get("out_trade_no"),(Map)reqMap,86400);
            System.out.println("response:  "+response.toString());
            String returnCode = response.get("return_code");
            JsonEntityUtil jsonEntityUtil=null;
            if (!"SUCCESS".equals(returnCode)) {
                logger.error("【微信支付信息生成服务类】生成微信支付信息失败！");
                return jsonEntityUtil=new JsonEntityUtil("1003","生成微信支付信息失败");
            }
            System.out.println(reqMap.toString());
            //  Map<String,String> backMap = new HashMap<>();
            System.out.println("返回的集合"+response.toString());
            HashMap<String,String> backMap=new HashMap<>();
            backMap.put("nonceStr",response.get("nonce_str"));
            backMap.put("package","prepay_id="+response.get("prepay_id"));
            backMap.put("signType","HMAC-SHA256");
            backMap.put("appId",Constants.APPID);
            backMap.put("timeStamp",(System.currentTimeMillis() / 1000)+"");
            String backMapSign=WXPayUtil.generateSignature(backMap, Constants.MCH_KEY, WXPayConstants.SignType.HMACSHA256);
            System.out.println("自己生成的签名---"+backMapSign);
            backMap.put("paySign",backMapSign);
            WxPayDTO wxpaydto=new WxPayDTO();
            wxpaydto.setNonceStr(backMap.get("nonceStr"));
            wxpaydto.setPackagedto(backMap.get("package"));
            wxpaydto.setPaySign(backMap.get("paySign"));
            wxpaydto.setSignType("HMAC-SHA256");
            wxpaydto.setTimeStamp(backMap.get("timeStamp"));
            logger.info("【微信支付信息生成服务类】生成微信支付信息成功！");
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(wxpaydto);
            return jsonEntityUtil;
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonEntityUtil("1003","生成微信支付信息失败");
        }
    }

    public JSONObject getParkingSpaceRentalFeeCostJson(Object o){
        return (JSONObject)o;
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

    public void setMap(SortedMap<String,String> reqMap) throws Exception {
        reqMap.put ("appid", APPID);//公众号ID
        reqMap.put("mch_id", Constants.MCH_ID);// 商户号
        String nonceStr = WXPayUtil.generateNonceStr();
        reqMap.put("nonce_str", nonceStr);//随机字符串
       // String id=this.payMentMapper.getOrganizationId(chargeCode);
        //   String orderId=this.thisSystemOrderIdService.thisSystemOrderId(id);
        reqMap.put("out_trade_no", System.currentTimeMillis()+"");//商户订单号
        reqMap.put ("notify_url", Constants.NOTIFY_URL);//通知地址
        reqMap.put("trade_type", Constants.TRADE_TYPE);// 交易类型
        reqMap.put("time_start", DateUtil.date(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN));// 交易起始吋伺
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN);
        reqMap.put("time_expire", df.format(new Date().getTime() + 30 * 60 * 1000));
        WXPayConstants.SignType signType = WXPayConstants.SignType.HMACSHA256;
        String sign = WXPayUtil.generateSignature(reqMap, MCH_KEY, signType);
        reqMap.put("sign", sign);//签名
    }
    public Map<String,String> getMap(Map<String,String> map){
        Map<String,String> paymap=new HashMap<>();
        paymap.put("appId",APPID);
        paymap.put("timeStamp",(System.currentTimeMillis() / 1000)+"");
        paymap.put("nonceStr",UUID.randomUUID().toString().replace("-",""));
        paymap.put("package",map.get("package"));
        paymap.put("signType","HMAC-SHA256");
        try {
            paymap.put("paySign", WeChatCreateSign.paySignDesposit(paymap, MCH_KEY));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return paymap;
    }


    @Override
    public JsonEntityUtil getRoomCode(JSONObject jsonObject) {

        JsonEntityUtil jsonEntityUtil=null;
        try{
            String userId=this.rsaGetStringUtil.getPrivatePassword(jsonObject.getString("APPKEY"));
            List<Room> roomList=this.parkingRateMapper.getRoomInfoByUserId(userId);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(roomList);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;

    }

    @Override
    public JsonEntityUtil getParkingSpaceCode(JSONObject jsonObject) {
        JsonEntityUtil jsonEntityUtil=null;
        try{
            String userId=this.rsaGetStringUtil.getPrivatePassword(jsonObject.getString("APPKEY"));
            String idCard=this.parkingRateMapper.getIdCard(userId);
            List<ParkingManagement> roomList=this.parkingRateMapper.getParkingManagementInfo(idCard);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(roomList);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getInfoByParkingSpaceCode(JSONObject jsonObject) {
        JsonEntityUtil jsonEntityUtil=null;
        try{
            JSONObject jsonObject1=this.parkingManagementFeeCost.parkingManagementFee(jsonObject,jsonObject.getString("organizationId"));
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(jsonObject1);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }
}
