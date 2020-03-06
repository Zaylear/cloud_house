package com.rbi.admin.config;


import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.util.*;
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

        //判断 是否是微信浏览器
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if(userAgent.indexOf("micromessenger")>-1){//微信客户端
            request.setAttribute("isWx","1");
            return true;
        }

        String URL = request.getRequestURL().toString();

//        String token = request.getHeader("appkey");
//
//        if ("".equals(token)||null==token) {
//            response.sendRedirect(request.getContextPath() + "/");
//            logger.info("【拦截器】用户未登录，不允许进行请求:" + request.getRequestURL());
//            response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//            return false;
//        }else {
//            Map<String, Claim> claims = null;
//            try {
//                claims = JwtToken.verifyToken(token);
//            } catch (Exception e) {
//                logger.error("【拦截器】token校验失败！error：{},IP:{}",e,IP);
//                response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//                return false;
//            }
//            Claim userIdClaim = claims.get("userId");
//            if (null == userIdClaim || StringUtils.isEmpty(userIdClaim.asString())) {
//                logger.info("【拦截器】非法token验证失败,IP:{}",IP);
//                response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//                return false;
//            }else {
//                String userId = String.valueOf(userIdClaim.asString());
//
//                UserInfoDAO userInfoDAO = getMapper(UserInfoDAO.class,request);
//
//                UserInfoDO userInfo = userInfoDAO.findByUserId(userId);
//
//                /**
//                 *查询权限，根据URL验证权限
//                 */
////                if (Constants.AUTHENTICATION_REGEX.equals(URL)){
////
////                    JSONObject jsonObject = new JSONObject();
////                    jsonObject.put("url",URL);
////
////                    String result = HttpRequestUtil.postDownloadJson(
////                            Constants.AUTH_LOCAL_HOST+Constants.AUTH_PATH,
////                            jsonObject.toString(),Constants.APPLICATION_JSON,token).toString();
////
////                    response.getWriter().write(result);
////                }
//
//                if (null==userInfo){
//                    logger.info("【拦截器】伪造token用户不存在！username={},IP:{}",userInfo.getUsername(),IP);
//                    response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//                    return false;
//                }
//                if (1==userInfo.getLoginStatus()){
//                    logger.info("【拦截器】用户token验证成功！username={},date={}",userInfo.getUsername(), DateUtil.date(DateUtil.FORMAT_PATTERN));
//                    return true;
//                }else {
//                    logger.info("【拦截器】用户处于离线状态！IP:{},username={}",IP,userInfo.getUsername());
//                    response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//                    return false;
//                }
//            }
//        }

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