package com.rbi.wx.wechatpay.util.uploadutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HTTPUploadUtil {
    public String postFile(String targetUrl, byte[] fileByte) {

        try {
            URL url = new URL(targetUrl.trim());
            // 打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置允许输入输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 设置不用缓存
            urlConnection.setUseCaches(false);
            // 设置传递方式
            urlConnection.setRequestMethod("POST");
            // 设置维持长连接
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置字符集
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置文件长度和类型
            // 设置文件类型
            urlConnection.setRequestProperty("Content-Type", "application/x-form-urlencoded");
            // 开始连接请求
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            // 获取上传文件的输入流

                out.write(fileByte);

            out.flush();
            out.close();
            //TODO
            // 请求返回的状态
            if (200 == urlConnection.getResponseCode()) {
                // 关闭数据流

                // 请求返回的数据
                InputStream in = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = in.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }

                return baos.toString("utf-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        return "fail";
    }

}
