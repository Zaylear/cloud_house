package com.rbi.wx.wechatpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.service.sendmsg.SendMsgService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(value = "发送消息",hidden = true)
public class SendMsgController {
    @Autowired
    private SendMsgService sendMsgService;
    @PostMapping("/wx/sendmsg")
    @ResponseBody
    public JsonEntityUtil sendMsg(@RequestBody JSONObject jsonObject){
        return this.sendMsgService.sendMsg(jsonObject.get("phone").toString());
    }
}
