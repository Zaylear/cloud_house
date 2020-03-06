package com.rbi.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.auth0.jwt.interfaces.Claim;
import com.rbi.dao.ILogLoginDAO;
import com.rbi.dao.UserInfoDAO;
import com.rbi.entity.LogLoginDO;
import com.rbi.entity.UserInfoDO;
import com.rbi.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Map;

public class BasicInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BasicInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String ipAdress = request.getHeader("ipAdrress");

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

        if (StringUtils.isBlank(ipAdress)){
            ipAdress = IP;
        }

        String token = request.getHeader("appkey");
        if ("".equals(token)||null==token) {
            response.sendRedirect(request.getContextPath() + "/");
            logger.info("【拦截器】用户未登录，不允许进行请求:" + request.getRequestURL());
            response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
            return false;
        }else {
            Map<String, Claim> claims = null;
            try {
                claims = JwtToken.verifyToken(token);
            }catch (NullPointerException e){
                logger.error("【拦截器】token校验失败！error：{},IP:{}",e,ipAdress);
                response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
                return false;
            }
            catch (Exception e) {
                logger.error("【拦截器】token校验失败！error：{},IP:{}",e,ipAdress);
                response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
                return false;
            }
            Claim userIdClaim = claims.get("userId");
            if (null == userIdClaim || StringUtils.isEmpty(userIdClaim.asString())) {
                logger.info("【拦截器】非法token验证失败,IP:{}",ipAdress);
                response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
                return false;
            }else {
                String userId = String.valueOf(userIdClaim.asString());

                UserInfoDAO userInfoDAO = getMapper(UserInfoDAO.class,request);

                ILogLoginDAO iLogLoginDAO = getMapper(ILogLoginDAO.class,request);
                LogLoginDO logLoginDO = iLogLoginDAO.findNewestLoginData(userId);
                UserInfoDO userInfo = userInfoDAO.findByUserId(userId);
                if (Tools.judgeContainsStr(Constants.LOGOUTREGEX,URL)){
                    userInfoDAO.updateLoginStatus(0,userId);
                    iLogLoginDAO.updateUdt(DateUtil.timeStamp(),logLoginDO.getUserId(),logLoginDO.getIdt());
                    logger.info("【拦截器】用户{}认证通过",userInfo.getUsername());
                    return true;
                }
                if (!ipAdress.equals(logLoginDO.getIpAddress())){
                    iLogLoginDAO.updateUdt(DateUtil.timeStamp(),logLoginDO.getUserId(),logLoginDO.getIdt());
                    userInfoDAO.updateLoginStatus(0,logLoginDO.getUserId());
                    logger.info("【拦截器】账号异地登录！username={},IP:{}",userInfo.getUsername(),ipAdress);
                    response.getWriter().write(ResponseVo.build("1003", "您的账号已在其他地方登陆！").toString());
                    return false;
                }
                /**
                 *查询权限，根据URL验证权限
                 */
//                if (Constants.AUTHENTICATION_REGEX.equals(URL)){
//                    List<String> permisUrlS = iPermissionAuthenticationDao.findPermisUrlS(userId);
//                    if (permisUrlS.size()>0){
//                        if (!Tools.ifInclude(permisUrlS,authURL)){
//                            logger.info("【拦截器】用户无权访问！IP:{},username={}",IP,userInfo.getUsername());
//                            response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//                            return false;
//                        }
//                    }else {
//                        logger.info("【拦截器】用户无权访问！IP:{},username={}",IP,userInfo.getUsername());
//                        response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
//                        return false;
//                    }
//                }

                if (null==userInfo){
                    logger.info("【拦截器】伪造token用户不存在！username={},IP:{}",userInfo.getUsername(),ipAdress);
                    response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
                    return false;
                }
                if (1==userInfo.getLoginStatus()){
                    logger.info("【拦截器】用户token验证成功！username={},date={}",userInfo.getUsername(), DateUtil.date(DateUtil.FORMAT_PATTERN));
                    iLogLoginDAO.updateUdt(DateUtil.timeStamp(),logLoginDO.getUserId(),logLoginDO.getIdt());
                    return true;
                }else {
                    if (ipAdress.equals(logLoginDO.getIpAddress())){
                        iLogLoginDAO.updateUdt(DateUtil.timeStamp(),logLoginDO.getUserId(),logLoginDO.getIdt());
                        userInfoDAO.updateLoginStatus(1,logLoginDO.getUserId());
                        logger.info("【拦截器】用户token验证成功！username={},date={}",userInfo.getUsername(), DateUtil.date(DateUtil.FORMAT_PATTERN));
                        return true;
                    }
                    logger.info("【拦截器】用户处于离线状态！IP:{},username={}",ipAdress,userInfo.getUsername());
                    response.getWriter().write(ResponseVo.build("1003", "访问拒绝！").toString());
                    return false;
                }
            }
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