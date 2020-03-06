package com.rbi.wx.wechatpay.util.modulemsg.Modlue;


import com.rbi.wx.wechatpay.util.modulemsg.httprequest.HttpRequest;


import java.util.HashMap;

import java.util.Set;

public class Modlue {
    private HashMap<String,String> msgMap;
    private String modlueId;

    public Modlue(HashMap<String, String> msgMap, String modlueId) {
        this.msgMap = msgMap;
        this.modlueId = modlueId;
    }

    /**
     * 拼接MAP成字符串
     * 并对接口发送请求
     * @param access
     * @return
     */
    public String sendMsg(String access){
        StringBuffer sb=new StringBuffer();
        sb.append(      "{\"touser\":\""+msgMap.get("openId")+
                "\",\"template_id\":\""+modlueId+
                "\",\"url\":\""+msgMap.get("url")+"\",\"data\":{");
        Set<String> set=msgMap.keySet();
        msgMap.remove("openId");
        msgMap.remove("url");
        int i=0;
        for(String s:set){
            sb.append("                   \""+s+
                    "\": {\"value\":\""+msgMap.get(s)
                    +"\",\"color\":\"#173177\"}");
            i++;
            if (msgMap.size()>i){
                sb.append(",");
            }
        }
        sb.append("           }\n" +
                "       }");
          System.out.println(sb.toString());
        return HttpRequest.httpRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access
        ,"POST",sb.toString());
    }
}
