package com.rbi.wx.wechatpay.util;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

public class Constants {

    public final static String PHOTOUPLOADURL="/var/www/html/wx-header";
    public final static String PHOTOREADURL="http://wy.gyrbi.com/photo/";
    /**
     * 请求类型
     */
    public final static String HEADER="Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; GT-I9300 Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/5.2.380";
    public final static String APPLICATION_JSON = "application/json;charset=UTF-8";

    public final static String APPLICATION_X_WWW_FORM_URLLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * uuid = 26875117-76f1-431f-a006-47ac0fa3a213
     * key = rbi2019
     */
    public final static String PUBLICKEY="Rbi@2018";
    public final static String PRIVATEKEY="";

    /** token秘钥，请勿泄露，请勿随便修改 */
    public static final String SECRET = "f62e4cf9293003ff14f1d373f4c1c40f";
    /** token 过期时间: 15分钟 */
    public static final int CALENDARFIELD = Calendar.MINUTE;
    public static final int CALENDARINTERVAL = 15;
    /**商户的KEY*/
    public static final String MCH_KEY="69b7b929c99f0a01c195d65ec22ea627";
    /**预下单的回调地址*/
    public static final String NOTIFY_URL="http://wy.gyrbi.com/cloud_house_wx-0.0.1-SNAPSHOT/success";
    /**微信公众平台对接的口令*/
    public static final String WECHATTOKEN="rbi";
    public static final String BODY="费用缴费";
    public static final String DOMAIN_API="api.mch.weixin.qq.com";
    public static final String DATE_URL="http://192.168.28.236:7848";
    /**微信的APPID*/
   public static final String APPID= "wxccc7e28f7cb8170c";
   // public static final String APPID= "wx2de755a0d77db109";
    /**支付类型*/
    public static final String TRADE_TYPE="JSAPI";
    /**微信端APP密钥*/
    public static final String APPSECRET="337fa6813f9a03452d885154c93a44d1";
   // public static final String APPSECRET="6693fda7a9c1562757f6d67f423467f3";
    public static final String APPKEY="/usr/work/cloud_house/wx/cloud_house_wx-0.0.1-SNAPSHOT/WEB-INF/classes/static/apiclient_cert.p12";
//    public static final String APPKEY=Constants.class.getResource("/").toString().
//            replace("file:","").replace("target/classes/",
//            "src/main/java/com/rbi/wx/config/apiclient_cert.p12");
    /**商户的ID*/
    public static final String MCH_ID="1577504931";
    public static final String LOGOUTREGEX = ".*/logout";
    public static final String WECHATUSERNAME="";
    public static final String WECHATTOKENURL="https://api.weixin.qq.com/sns/oauth2/access_token";
    /**微信公众号开发者的OPENID*/
    public static final String WECHATOPENID="";
    public static HttpServletResponse response;
    public final static String REDISKEY_PROJECT = "cloud_house:";
    public final static String VILLAGE_TREE = "village_tree:";
    public final static String PROPERTY_DUE_TIME = "property_due_time:";
}
