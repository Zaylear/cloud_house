package com.rbi.interactive.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSMSUtil {


    /**
     * 单条发送
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     * 单条短信发送
     * @param accessKeyId
     * @param secret
     * @param phoneNumbers 手机号
     * @param signName     签名
     * @param templateCode  短信模板ID
     * @param templateParam 短信模板变量值 JSON格式
     * @return
     */
    public static Boolean singleSend(String accessKeyId, String secret, String phoneNumbers, String signName, String templateCode, JSONObject templateParam){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);//手机号
        request.putQueryParameter("SignName", signName);//签名名称
        request.putQueryParameter("TemplateCode", templateCode);//短信模板ID
        request.putQueryParameter("TemplateParam", templateParam.toString());//验证码参数
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("verificationCode",156464);
        return jsonObject;
    }


}
