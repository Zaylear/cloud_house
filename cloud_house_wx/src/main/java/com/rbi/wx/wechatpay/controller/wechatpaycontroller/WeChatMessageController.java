package com.rbi.wx.wechatpay.controller.wechatpaycontroller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.entity.ModuleEntity.ModuleOne;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.service.wechatservice.WeChatApiService;
import com.rbi.wx.wechatpay.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WeChatMessageController {
    @Autowired
    private WeChatApiService weChatApiService;
    @RequestMapping("/")
    public void api(HttpServletRequest request, HttpServletResponse response) {
        Constants.response=response;
        this.weChatApiService.api(request,response);
    }

    @PostMapping("/gettoken")
    @ResponseBody
    public JsonEntityUtil getToken(@RequestBody JSONObject jsonObject){
         return this.weChatApiService.getToken(jsonObject.getString("code"));
    }
    @PostMapping("/sendmsg")
    @ResponseBody
    public String websocket(ModuleOne moduleOne){
     return this.weChatApiService.sendModlue(moduleOne);
    }

    @PostMapping("/getticket")
    @ResponseBody
    public JsonEntityUtil getTicket(){
        return this.weChatApiService.getTicket();
    }
}
