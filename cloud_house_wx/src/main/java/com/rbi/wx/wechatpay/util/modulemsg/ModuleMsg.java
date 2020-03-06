package com.rbi.wx.wechatpay.util.modulemsg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import com.rbi.wx.wechatpay.util.modulemsg.Modlue.Modlue;
import com.rbi.wx.wechatpay.util.modulemsg.httprequest.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

public class ModuleMsg {
    /**
     * 缓存配置
     */
    private CacheConfig cacheConfig=null;
    /**
     * 参数配置
     */
    private ModuleConfig moduleConfig;

    public ModuleMsg(ModuleConfig moduleConfig) {
        this.moduleConfig = moduleConfig;
    }

    public ModuleMsg(CacheConfig cacheConfig, ModuleConfig moduleConfig) {
        this.cacheConfig = cacheConfig;
        this.moduleConfig = moduleConfig;
    }


    /**
     * 发送消息
     * 如果开启缓存 如果缓存过期就插入新的缓存  如果没过期就直接取出
     * 如果没有开启缓存  直接获取通过url请求ACCESE
     * @param hashMap
     * @param moduleId
     * @return
     */
    public String senMsg(HashMap<String,String> hashMap,String moduleId){
        Modlue modlue=new Modlue(hashMap,moduleId);
        if (this.cacheConfig!=null){
            if (this.cacheConfig.getAccese()!=null){
                return modlue.sendMsg(cacheConfig.getAccese());
            }else {
                String accese=getAccess();
                cacheConfig.cacheAccese(accese);
                return modlue.sendMsg(accese);
            }
        }else {
            return noCacheSend(modlue,getAccess());
        }
    }

    /**
     * 没有缓存配置发送消息
     * @param modlue
     * @param accese
     * @return
     */
    private String noCacheSend(Modlue modlue,String accese){
       return modlue.sendMsg(accese);
    }

    /**
     * 得到Accese口令
     * @return
     */
    private String getAccess() {
        String result = HttpRequest.httpRequest("https://api.weixin.qq.com/cgi-bin/token?",
                "GET",
                "grant_type=client_credential&appid="+moduleConfig.getAppid()+"&secret="+moduleConfig.getSecret());
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.get("access_token").toString();
    }
}
