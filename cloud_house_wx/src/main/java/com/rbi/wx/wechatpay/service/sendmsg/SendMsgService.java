package com.rbi.wx.wechatpay.service.sendmsg;

import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.util.msgutil.SendSMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMsgService {
    @Autowired
    private RedisUtil redisUtil;
    public JsonEntityUtil sendMsg(String phone){
      return SendSMSUtil.sendMsg(phone,redisUtil);
    }



}
