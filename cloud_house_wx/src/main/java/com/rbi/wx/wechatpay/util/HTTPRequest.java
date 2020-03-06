package com.rbi.wx.wechatpay.util;


import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
@Component
public class HTTPRequest {
   public  String JsonRequest(String requestUrl,String requestMethod,String outputStr) {
       // 创建SSLContext
       StringBuffer buffer=null;
       try {

           URL url = new URL(requestUrl);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();

           conn.setDoOutput(true);
           conn.setDoInput(true);
           conn.setUseCaches(false);
           // 设置请求方式（GET/POST）
           conn.setRequestMethod(requestMethod);
           conn.setRequestProperty("Content-type", "application/json");
           conn.setRequestProperty("user-agent",Constants.HEADER);
           // 当outputStr不为null时向输出流写数据
           if (null != outputStr) {
               OutputStream outputStream = conn.getOutputStream();
               // 注意编码格式
               outputStream.write(outputStr.getBytes("UTF-8"));
               outputStream.close();
           }
           // 从输入流读取返回内容
           InputStream inputStream = conn.getInputStream();
           InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
           BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
           String str = null;
           buffer = new StringBuffer();
           while ((str = bufferedReader.readLine()) != null) {
               buffer.append(str);
           }
           // 释放资源
           bufferedReader.close();
           inputStreamReader.close();
           inputStream.close();
           inputStream = null;
           conn.disconnect();
           return buffer.toString();
       } catch (ConnectException ce) {
           System.out.println("连接超时：{}"+ ce);
       } catch (Exception e) {
           System.out.println("https请求异常：{}"+ e);
       }
       return null;
   }
    public  String httpRequest(String requestUrl,String requestMethod,String outputStr){
        // 创建SSLContext
        StringBuffer buffer=null;
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
           buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}"+ ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}"+ e);
        }
        return null;


    }

}
