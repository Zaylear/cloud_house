package com.rbi.wx.wechatpay.util.dowloadutil;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class Dowload {
    public void dowlod(HttpServletRequest request, HttpServletResponse response){
        String filename="test";
        response.setContentType("multipart/form-data");
        response.addHeader("Content-Disposition",String.format("attachment; filename=\"%s\"", "test.pdf"));

    }
}
