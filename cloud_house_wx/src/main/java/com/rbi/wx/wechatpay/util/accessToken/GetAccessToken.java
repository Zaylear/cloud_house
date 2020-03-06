package com.rbi.wx.wechatpay.util.accessToken;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

import static com.rbi.wx.wechatpay.util.Constants.APPID;
import static com.rbi.wx.wechatpay.util.Constants.APPSECRET;

public class GetAccessToken implements Serializable {
   private static String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ Constants.APPID+"&secret="+Constants.APPSECRET;
    public static String getAccessToken(RedisUtil redisUtil){
        redisUtil.setRedisDb(1);
        if (redisUtil.get("accessToken")==null) {
            RestTemplate restTemplate=new RestTemplate();
            ResponseEntity<String> response=restTemplate.getForEntity(url,String.class);
            JSONObject jsonObject= JSON.parseObject(response.getBody());
            long time=jsonObject.getLong("expires_in");
            redisUtil.set("accessToken",jsonObject.getString("access_token"),time-600);
            return jsonObject.getString("access_token");
        }else {
            return redisUtil.get("accessToken").toString();
        }
    }
}
