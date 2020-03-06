package com.rbi.wx.wechatpay.service.wechatservice;

import com.rbi.wx.config.MyConfig;
import com.rbi.wx.wechatpay.dto.paymentrecordentity.WxPayDTO;
import com.rbi.wx.wechatpay.mapper.ChargeRealMapper;
import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;

import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.chargepayfactory.ChargePayDTO;
import com.rbi.wx.wechatpay.util.chargepayfactory.ChargePayFactoryInterface;
import com.rbi.wx.wechatpay.util.chargepayfactory.ChargeTypeFactory;
import com.rbi.wx.wechatpay.util.official.WXPay;
import com.rbi.wx.wechatpay.util.official.WXPayConstants.SignType;
import com.rbi.wx.wechatpay.util.official.WXPayUtil;

import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.WeChatCreateSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class WxPayService {

    private final static Logger logger = LoggerFactory.getLogger(WxPayService.class);
    /**
     * 进行缴费
     */
     @Autowired
     private RedisUtil redisUtil;
     @Autowired
     private PayMentMapper payMentMapper;
     @Autowired
     private ChargeRealMapper chargeRealMapper;
  //   @Autowired
   //  private ThisSystemOrderIdService thisSystemOrderIdService;
     public JsonEntityUtil<WxPayDTO> wxPay(ChargePayRequestEntity chargePayRequestEntity, String ip, String userId){
        //根据orderId查询订单信息获取订单号,总价，商品描述
         try {
//         ChargeReal chargeReal= ChargeRealFactory.getChargeReal(this.chargeRealMapper.getChargeType(chargePayRequestEntity.getChargeCode()));
             ChargePayFactoryInterface chargType= ChargeTypeFactory.getChargePay(this.payMentMapper.findType(chargePayRequestEntity.getChargeCode()));
             ChargePayDTO chargePayDTO=chargType.getChargePayDTO(chargePayRequestEntity,this.payMentMapper);
         double totalFee= 0;
//         totalFee = chargeReal.getMoney(chargePayRequestEntity,chargeRealMapper);
         totalFee=Double.valueOf(chargePayDTO.getNewMoney())*10;
         DecimalFormat decimalFormat = new DecimalFormat("#.##");
         String fee=(totalFee+"").replace(".","");
         redisUtil.setRedisDb(1);
         SortedMap<String,String> reqMap = new TreeMap<>();
         reqMap.put("body","物业费缴纳");// 商品描述
         reqMap.put("total_fee", fee);
       //  reqMap.put("total_fee", "1");
        // reqMap.put("total_fee", (int)fee+"");// 总金额
          //   System.out.println((int)fee+"");
         reqMap.put("spbill_create_ip",ip);//終端IP
         reqMap.put ("openid", chargePayRequestEntity.getOpenId());//关注微信公众号获取的openid
         setMap(reqMap,chargePayRequestEntity.getChargeCode());
          if (WXPayUtil.isSignatureValid(reqMap, Constants.MCH_KEY, SignType.HMACSHA256)) {
             logger.info("【微信支付信息生成服务类】签名效验成功");
         } else {
             logger.info("【微信支付信息生成服务类】签名效验失败");
         }
        MyConfig myWxPayConfig = new MyConfig();
        WXPay wxpay = new WXPay(myWxPayConfig);
        Map<String, String> response = wxpay.unifiedOrder(reqMap);
        reqMap.put ("userId", userId);
        reqMap.put ("roomCode", chargePayRequestEntity.getRoomCode());
        reqMap.put("chargeCode",chargePayRequestEntity.getChargeCode());
        reqMap.put("datedif",chargePayRequestEntity.getDatedif());
        reqMap.put("couponId",chargePayRequestEntity.getCouponId());
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
        String backMapSign=WXPayUtil.generateSignature(backMap, Constants.MCH_KEY,SignType.HMACSHA256);
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
    public void setMap(SortedMap<String,String> reqMap,String chargeCode) throws Exception {
        reqMap.put ("appid", APPID);//公众号ID
        reqMap.put("mch_id", Constants.MCH_ID);// 商户号
        String nonceStr = WXPayUtil.generateNonceStr();
        reqMap.put("nonce_str", nonceStr);//随机字符串
        String id=this.payMentMapper.getOrganizationId(chargeCode);
     //   String orderId=this.thisSystemOrderIdService.thisSystemOrderId(id);
        reqMap.put("out_trade_no", System.currentTimeMillis()+"");//商户订单号
        reqMap.put ("notify_url", Constants.NOTIFY_URL);//通知地址
        reqMap.put("trade_type", Constants.TRADE_TYPE);// 交易类型
        reqMap.put("time_start", DateUtil.date(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN));// 交易起始吋伺
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN);
        reqMap.put("time_expire", df.format(new Date().getTime() + 30 * 60 * 1000));
        SignType signType = SignType.HMACSHA256;
        String sign = WXPayUtil.generateSignature(reqMap, Constants.MCH_KEY, signType);
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

}
