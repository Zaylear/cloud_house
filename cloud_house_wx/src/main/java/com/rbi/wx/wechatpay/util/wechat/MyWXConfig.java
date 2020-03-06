package com.rbi.wx.wechatpay.util.wechat;





import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.rbi.wx.wechatpay.util.Constants.APPID;
import static com.rbi.wx.wechatpay.util.Constants.MCH_ID;
import static com.rbi.wx.wechatpay.util.Constants.MCH_KEY;

public class MyWXConfig extends WXPayConfig {
    private byte[] certData;
   public MyWXConfig() throws Exception{
       String certPath = "D:/cert/apiclient_cert.p12";
       File file = new File(certPath);
       InputStream certStream = new FileInputStream(file);
       this.certData = new byte[(int) file.length()];
       certStream.read(this.certData);
       certStream.close();
    }

    @Override
    String getAppID() {
        return APPID;
    }

    @Override
    String getMchID() {
        return MCH_ID;
    }

    @Override
    String getKey() {
        return MCH_KEY;
    }

    @Override
    InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("api.mch.weixin.qq.com",true);
            }
        };
    }


}
