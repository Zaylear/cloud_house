package com.rbi.wx.wechatpay.controller.wechatmean;

import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Api(value = "微信公众号服务")
@Controller
public class WeChatMean {
    @Autowired
    private HTTPRequest httpRequest;
    @PostMapping("/getacces")
    @ResponseBody
    public String getAcces(){
       return httpRequest.httpRequest("https://api.weixin.qq.com/cgi-bin/token?",
                "GET",
                "grant_type=client_credential&appid="+Constants.APPID+"&secret="+ Constants.APPSECRET);

    }
    @PostMapping("/putmean")
    @ResponseBody
    public String putMean(String access){
      String msg="{\n" +
              "    \"button\": [\n" +
              "        {\n" +
              "            \"name\": \"物业服务\", \n" +
              "            \"sub_button\": [\n" +
              "                {\n" +
              "                    \"type\": \"view\", \n" +
              "                    \"name\": \"意见反馈\", \n" +
              "                    \"url\": \"http://wy.gyrbi.com/wx/#/login?status=2\"\n" +
              "                },\n" +
              "                {\n" +
              "                    \"type\": \"view\", \n" +
              "                    \"name\": \"信息发布\", \n" +
              "                    \"url\": \"http://wy.gyrbi.com/wx/#/login?status=2\"\n" +
              "                },\n" +
              "                {\n" +
              "                    \"type\": \"view\", \n" +
              "                    \"name\": \"网上商城\", \n" +
              "                    \"url\": \"http://wy.gyrbi.com/wx/#/login?status=2\"\n" +
              "                }\n" +
              "            ]\n" +
              "        }, \n" +
              "        {\n" +
              "            \"name\": \"个人中心\", \n" +
              "            \"sub_button\": [\n" +
              "        {\n" +
              "            \"name\": \"个人中心\", \n" +
              "            \"type\": \"view\", \n" +
              "            \"url\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Constants.APPID+"&redirect_uri=http://wy.gyrbi.com&response_type=code&scope=snsapi_userinfo#wechat_redirect\"\n" +
              "        },\n" +              "              " +
              "               {\n" +
              "                    \"type\": \"view\", \n" +
              "                    \"name\": \"我的意见\", \n" +
              "                    \"url\": \"http://wy.gyrbi.com/wx/#/login?status=2\"\n" +
              "                },\n" +
              "                {\n" +
              "                    \"type\": \"view\", \n" +
              "                    \"name\": \"预约服务\", \n" +
              "                    \"url\": \"http://wy.gyrbi.com/wx/#/login?status=2\"\n" +
              "                }\n" +
              "                ]\n" +
              "                }\n" +
              "        } \n" +
              "    ]\n" +
              "}";
      System.out.println(access);
     return httpRequest.httpRequest("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access,
             "POST",
             msg);
    }
}
