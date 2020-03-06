package com.rbi.interactive.config;


import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class BasicInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BasicInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String IP = null;

        IP = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(IP) || IP.length() == 0 || "unknown".equalsIgnoreCase(IP)) {
            IP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = request.getRemoteAddr();
        }

        String URL = request.getRequestURL().toString();
        try {
            String token = request.getHeader("appkey");

            String userId = JwtToken.getClaim(token,"userId");
            /**
             *查询权限，根据URL验证权限，通过redis认证权限
             */
//            if (Tools.judgeContainsStr(Constants.AUTHENTICATION_REGEX,URL)){
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("url",URL);
//
//                JSONObject result = JSONObject.parseObject(HttpRequestUtil.postDownloadJson(
//                        Constants.AUTHENTICATION_HOST + Constants.AUTH_PATH,
//                        jsonObject.toString(),Constants.APPLICATION_JSON,token).toString());
//                if ("1000".equals(result.getString("status"))){
//                    logger.info("【权限认证】认证通过 userId={},IP: {}",userId,IP);
//                    return true;
//                }else {
//                    logger.error("【权限认证】认证通过 userId={},IP: {}",userId,IP);
//                    response.getWriter().write(result.toString());
//                    return false;
//                }
//            }else {
            String path = Constants.TOKEN_VERIFICATION;
            JSONObject result = JSONObject.parseObject(Objects.requireNonNull(HttpRequestUtil.postHttpUtil(
                    path, "", Constants.APPLICATION_JSON, token,IP)).toString());
            if ("1000".equals(result.getString("status"))){
                logger.info("【拦截器认证】token认证通过 userId={},IP: {}",userId,IP);
                return true;
            }else {
                logger.info("【拦截器认证】token认证失败 userId={},IP：{}",userId,IP);
                response.getWriter().write(result.toString());
                return false;
            }
//            }
        } catch (IOException e) {
            response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
            return false;
        }catch (NullPointerException e){
            response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
            return false;
        }catch (Exception e){
            response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
            return false;
        }
    }

    /**
     * 拦截器获取JPA数据实例
     * @param clazz
     * @param request
     * @param <T>
     * @return
     */
    private <T> T getMapper(Class<T> clazz,HttpServletRequest request)
    {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }

}