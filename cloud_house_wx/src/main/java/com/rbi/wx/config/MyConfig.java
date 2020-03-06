package com.rbi.wx.config;


import com.rbi.wx.wechatpay.util.official.IWXPayDomain;
import com.rbi.wx.wechatpay.util.official.WXPayConfig;
import com.rbi.wx.wechatpay.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {

    private byte[] certData;

    public MyConfig() throws Exception {
        String certPath = Constants.APPKEY;
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return Constants.APPID;
    }

    public String getMchID() {
        return Constants.MCH_ID;
    }

    public String getKey() {
        return Constants.MCH_KEY;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

//    public int getHttpConnectTimeoutMs() {
//        return 8000;
//    }
//
//    public int getHttpReadTimeoutMs() {
//        return 10000;
//    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(Constants.DOMAIN_API,true);
            }
        };
        return iwxPayDomain;
    }
}