package com.rbi.wx.wechatpay.util.msgutil;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;

public class SendSMSUtil {


    /**
     *
     * @param phone             手机号
     * @param verificationCode  验证码
     */

    private static void sendMsg(String phone,String verificationCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FwcbpWitFXQMjnzBUbF", "R4ImVKeFTG88GiPQITLJt3FM8dnII8");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "菲斯赛维物业");
        request.putQueryParameter("TemplateCode", "SMS_175121966");
        request.putQueryParameter("TemplateParam", "{\"verificationCode\":\""+verificationCode+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static JsonEntityUtil sendMsg(String phone, RedisUtil redisUtil){
        redisUtil.setRedisDb(1);
        JsonEntityUtil jsonEntityUtil=null;
        try {
            int verificationCode=(int)((Math.random()*9+1)*100000);
            sendMsg(phone,verificationCode+"");
            if (redisUtil.hasKey("msg"+phone)){
                redisUtil.del("msg"+phone);
                redisUtil.set("msg" + phone, verificationCode, 3600);
            }else {
                redisUtil.set("msg" + phone, verificationCode, 3600);
            }
            jsonEntityUtil =new JsonEntityUtil("1000","发送成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

}
