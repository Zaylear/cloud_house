package com.rbi.wx.wechatpay.configuration;


import com.rbi.wx.wechatpay.requestentity.JsonUtil;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class TestInter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.print(request.getRequestURL());
//        System.out.println(request.getHeader("APPKEY"));
//        if (request.getHeader("APPKEY")==null){
//            System.out.println("失败");
//            return false;
//        }else {
//            System.out.print(request.getRequestURL()+"==---"+request.getHeader("APPKEY")+"--------");
//            System.out.println("成功");
//            return true;
//        }

    return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
