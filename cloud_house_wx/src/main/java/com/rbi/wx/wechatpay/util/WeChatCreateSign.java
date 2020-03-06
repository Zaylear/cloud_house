package com.rbi.wx.wechatpay.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeChatCreateSign {
    public static String getNow(String str){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date()).toString()+str;
    }
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * 构造package
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key.toString()).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            temp.append(valueString);
        }
        return temp.toString();
    }
    /**
     * HmacSHA256类型签名
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String paySignDesposit(Map<String, String> map, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> params = new HashMap<String, String>();
        Set<String> set = map.keySet();
        for (String string : set) {
            if(!map.get(string).equals("")){
                params.put(string, String.valueOf(map.get(string)));
            }
        }
        String string1 = createSign(params);
        String stringSignTemp = string1 + "&key=" + key;
        //return DigestUtils.sha256Hex(stringSignTemp).toUpperCase();
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        //  utf-8 : 解决中文加密不一致问题,必须指定编码格式
        return byteArrayToHexString(sha256_HMAC.doFinal(stringSignTemp.getBytes("utf-8"))).toUpperCase();
    }


    public static String getSign(Map params,String key1) {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value =  params.get(key).toString();
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                sb.append(key);
                sb.append("=");
                sb.append(value);
            } else {
                sb.append(key);
                sb.append("=");
                sb.append(value);
                sb.append("&");
            }
        }
       sb.append("&key=" + key1);
        System.out.println("sb"+sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }



}
