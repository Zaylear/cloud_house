package com.rbi.wx.wechatpay.service.wechatservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.entity.ModuleEntity.ModuleOne;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import com.rbi.wx.wechatpay.util.XmlUtil;
import com.rbi.wx.wechatpay.util.accessToken.GetAccessToken;
import com.rbi.wx.wechatpay.util.modulemsg.CacheConfig;
import com.rbi.wx.wechatpay.util.modulemsg.ModuleMsg;
import com.rbi.wx.wechatpay.util.modulemsg.demo.ChchingConfig;
import com.rbi.wx.wechatpay.util.modulemsg.demo.ModuleConfig;
import com.rbi.wx.wechatpay.util.wechat.WeChatToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.rbi.wx.wechatpay.util.Constants.APPID;
import static com.rbi.wx.wechatpay.util.Constants.APPSECRET;
import static com.rbi.wx.wechatpay.util.Constants.WECHATOPENID;
import static com.rbi.wx.wechatpay.util.DateUtil.FORMAT_PATTERN;

@Service
public class WeChatApiService {

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 通过code获取access_token
     * @param code
     * @return
     */
    public JsonEntityUtil getToken(String code){
        JsonEntityUtil jsonEntityUtil=null;
        RestTemplate restTemplate=new RestTemplate();
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APPID+"&secret="+APPSECRET+"&code="+code+"&grant_type=authorization_code";
        ResponseEntity<String> response=restTemplate.getForEntity(url,String.class);
        System.out.print(response.getBody());
        JSONObject jsonObject= JSON.parseObject(response.getBody());
        JSONObject responseJson=new JSONObject();
        if (jsonObject.getString("access_token")==null){
            jsonEntityUtil=new JsonEntityUtil("1003","请求出错");
        }else {
            jsonEntityUtil=new JsonEntityUtil("1000","请求成功");

            responseJson.put("openId",jsonObject.getString("openid"));

        }
        String accessToken= GetAccessToken.getAccessToken(this.redisUtil);
        String userInfoUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+jsonObject.getString("openid")+"&lang=zh_CN";
        RestTemplate restTemplateUser=new RestTemplate();
        ResponseEntity<String> responseUser=restTemplateUser.getForEntity(userInfoUrl,String.class);
        JSONObject userJson=JSON.parseObject(responseUser.getBody());
        responseJson.put("userInfo",userJson);
        jsonEntityUtil.setEntity(responseJson);
        return jsonEntityUtil;
    }

    /**
     * 如果请求为GET就是微信公众号对接
     * 如果为POST就是微信发送了消息过来
     * @param request
     * @param response
     */
    public void api(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());
        if (request.getMethod().equals("GET")){
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (WeChatToken.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
        out.close();
        out = null;
    }       else {
            System.out.println("微信请求");
            //POST请求
            try {
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                Map<String,String> msgMap= XmlUtil.xmlToMap(request);
                String fromUserNmae=msgMap.get("FromUserName");
                System.out.print(fromUserNmae);
                PrintWriter out;
                String replyMsg = "<xml>"
                        + "<ToUserName><![CDATA["+msgMap.get("FromUserName")+"]]></ToUserName>"//回复用户时，这里是用户的openid；但用户发送过来消息这里是微信公众号的原始id
                        + "<FromUserName><![CDATA["+msgMap.get("ToUserName")+"]]></FromUserName>"//这里填写微信公众号 的原始id；用户发送过来时这里是用户的openid
                        + "<CreateTime>1531553112194</CreateTime>"//这里可以填创建信息的时间，目前测试随便填也可以
                        + "<MsgType><![CDATA[text]]></MsgType>"//文本类型，text，可以不改
                        + "<Content><![CDATA["+"\n" +
                        "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constants.APPID+"&redirect_uri=http://wy.gyrbi.com/wx/index.html&response_type=code&scope=snsapi_userinfo#wechat_redirect"+"]]></Content>"//文本内容
                        + "<MsgId>1234567890123456</MsgId> "//消息id，随便填，但位数要够
                        + " </xml>";
                out=response.getWriter();
                out.print(replyMsg);
                out.close();
                out=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拼接成xml的格式发送消息给微信
     * @param openid  用户ID
     * @param msg 内容
     * @param response
     */
    public void sendmsg(String openid,String msg,HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out;
            String replyMsg = "<xml>"
                    + "<ToUserName><![CDATA["+openid+"]]></ToUserName>"//回复用户时，这里是用户的openid；但用户发送过来消息这里是微信公众号的原始id
                    + "<FromUserName><![CDATA["+WECHATOPENID+"]]></FromUserName>"//这里填写微信公众号 的原始id；用户发送过来时这里是用户的openid
                    + "<CreateTime>1531553112194</CreateTime>"//这里可以填创建信息的时间，目前测试随便填也可以
                    + "<MsgType><![CDATA[text]]></MsgType>"//文本类型，text，可以不改
                    + "<Content><![CDATA["+msg+"]]></Content>"//文本内容
                    + "<MsgId>1234567890123456</MsgId> "//消息id，随便填，但位数要够
                    + " </xml>";
            out=response.getWriter();
            out.print(replyMsg);
            out.close();
            out=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String sendModlue(ModuleOne moduleOne){
        HashMap<String,String>  hashMap=new HashMap<>();
        hashMap.put("openId",moduleOne.getOpenId());
        hashMap.put("url","");
        hashMap.put("first","你好尊敬的用户你有一份缴费清单");
        hashMap.put("keyword1",moduleOne.getHouseId());
        hashMap.put("keyword2",moduleOne.getUserName());
        hashMap.put("keyword3",moduleOne.getPhone());
        hashMap.put("keyword4", DateUtil.date(FORMAT_PATTERN));
        hashMap.put("remark",moduleOne.getRemark());
        ModuleConfig moduleConfig=new ModuleConfig();
        CacheConfig<RedisUtil> chchingConfig=new ChchingConfig(redisUtil);
        ModuleMsg moduleMsg=new ModuleMsg(chchingConfig,moduleConfig);
        return moduleMsg.senMsg(hashMap,"_xUjUdUUoZlAMyZN1ZdpSLcnsGxGfLe13ztMwQNWCCg");
           }


    @Autowired
    private HTTPRequest httpRequest;
    public JsonEntityUtil getTicket(){
        JsonEntityUtil jsonEntityUtil=null;
        String accessToeknURL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Constants.APPID+"&secret="+ Constants.APPSECRET;

        String accessTokenStr=httpRequest.httpRequest(accessToeknURL,"GET","");
        System.out.println(accessTokenStr);
        JSONObject accessJson=JSON.parseObject(accessTokenStr);
        System.out.println(accessTokenStr);
        if (accessJson.getString("access_token")==null){
            return jsonEntityUtil=new JsonEntityUtil("1003","请求出错");
        }
        String ticketURL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessJson.getString("access_token")+"&type=jsapi";
        String ticketStr=httpRequest.httpRequest(ticketURL,"GET","");
        JSONObject ticketJson=JSON.parseObject(ticketStr);
        System.out.println(ticketStr);
        if (!ticketJson.getString("errmsg").equals("ok")){
            return jsonEntityUtil=new JsonEntityUtil("1003","请求出错");
        }
        JSONObject responseJson=new JSONObject();
        responseJson.put("ticket",ticketJson.getString("ticket"));
        jsonEntityUtil=new JsonEntityUtil("1000","请求成功");
        jsonEntityUtil.setEntity(responseJson);
        return jsonEntityUtil;
    }
    }

