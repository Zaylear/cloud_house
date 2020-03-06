package com.rbi.controller.interactive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.LogLoginDO;
import com.rbi.entity.UserInfoDO;
import com.rbi.service.interactive.LoginService;
import com.rbi.service.interactive.PermissionAuthenticationService;
import com.rbi.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    PermissionAuthenticationService permissionAuthenticationService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseVo<Map<String,Object>> login(@RequestBody JSONObject request,HttpServletRequest httpServletRequest){

        UserInfoDO userInfoDO = JSON.toJavaObject(request,UserInfoDO.class);
        String username = request.getString("username");
        String password = request.getString("password");
        String module = request.getString("module");
        String openId = "";
        String IP = null;
        IP = httpServletRequest.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(IP) || IP.length() == 0 || "unknown".equalsIgnoreCase(IP)) {
            IP = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(IP) || "unknown".equalsIgnoreCase(IP)) {
            IP = httpServletRequest.getRemoteAddr();
        }


//        if (Constants.WX_MODULE.equals(module)){
//            openId = request.getString("openId");
//            return ResponseVo.build("1007","暂不支持微信登录");
//        }else
        if (Constants.WEB_MODULE.equals(module)){
            try {
                if (null==username||null==password){
                    return ResponseVo.build("1001","用户名或密码不能为空！");
                }
                Map<String,Object> loginStatus = loginService.login(username,password,module,IP);
                if (loginStatus.get("status").equals("1013")||loginStatus.get("status").equals("1014")){
                    logger.info("【用户登录类】用户登录失败username={}",username);
                    return ResponseVo.build("1004","用户名或密码错误！");
                }else if (loginStatus.get("status").equals("1015")){
                    logger.info("【用户登录类】用户登录失败，无权限访问username={}",username);
                    return ResponseVo.build("1003","拒绝访问！");
                }else if (loginStatus.get("status").equals("1016")){
                    logger.info("【用户登录类】用户登录失败，无权限访问username={}",username);
                    return ResponseVo.build("1003","用户已在线！");
                }else {
                    loginStatus.remove("status");
                    logger.info("【用户登录类】用户登录成功username={}",username);
                    return ResponseVo.build("1000","请求成功！",loginStatus);
                }
            }catch (NullPointerException e){
                return ResponseVo.build("1005","用户名不存在！");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("【用户登录类】服务器处理失败username={}，error：{}",username,e);
                return ResponseVo.build("1002","服务器处理失败！");
            }
        } else if (Constants.ADMIN_MODULE.equals(module)) {
            try {
                if (null==username||null==password){
                    return ResponseVo.build("1001","用户名或密码不能为空！");
                }
                Map<String,Object> loginStatus = loginService.loginAdmin(username,password,module);
                if (loginStatus.get("status").equals("1013")||loginStatus.get("status").equals("1014")){
                    logger.info("【用户登录类】用户登录失败username={}",username);
                    return ResponseVo.build("1004","用户名或密码错误！");
                }else if (loginStatus.get("status").equals("1015")){
                    logger.info("【用户登录类】用户登录失败，无权限访问username={}",username);
                    return ResponseVo.build("1003","拒绝访问！");
                }else {
                    loginStatus.remove("status");
                    logger.info("【用户登录类】用户登录成功username={}",username);
                    return ResponseVo.build("1000","请求成功！",loginStatus);
                }
            }catch (NullPointerException e){
                return ResponseVo.build("1005","用户名不存在！");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("【用户登录类】服务器处理失败username={}，error：{}",username,e);
                return ResponseVo.build("1002","服务器处理失败！");
            }
        }else if (Constants.BD_MODULE.equals(module)){
            logger.info("【用户登录类】用户登录成功username={}",username);
            return ResponseVo.build("1003","拒绝访问！");
        }else {
            logger.warn("【用户登录类】登录模块不存在module={}", module);
            return ResponseVo.build("1005", "登录模块不存在！");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public ResponseVo<String> logout(HttpServletRequest request){
        try {

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

            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            loginService.logout(userId,IP);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.info("【退出登录】服务器处理失败");
            return ResponseVo.build("1002","服务器处理失败！");
        }
    }

    @RequestMapping(value = "/refresh/appkey",method = RequestMethod.POST)
    public ResponseVo<String> refreshToken(HttpServletRequest httpServletRequest){
        try {
            String token = httpServletRequest.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            token = JwtToken.createToken(userId);
            return ResponseVo.build("1000","请求成功！",token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002","服务器处理失败！");
        }
    }

//    @RequestMapping(value = "/permit/auth",method = RequestMethod.POST)
//    public ResponseVo<String> permitAuth(@RequestBody JSONObject request, HttpServletRequest httpServletRequest){
//        try {
//            String url = request.getString("url");
//            String token = httpServletRequest.getHeader("appkey");
//            if (StringUtils.isBlank(url)){
//                return ResponseVo.build("1001","参数不能为空！");
//            }
//
//            if (permissionAuthenticationService.permitAuth(url,token)){
//                return ResponseVo.build("1000","请求成功！");
//            }else {
//                return ResponseVo.build("1003","无权访问！");
//            }
//        } catch (Exception e) {
//            return ResponseVo.build("1002","服务器处理异常！");
//        }
//    }

    @RequestMapping(value = "/token/verification",method = RequestMethod.POST)
    public ResponseVo<String> tokenVerification(){
        return ResponseVo.build("1000","请求成功");
    }


    @PostMapping(value = "/findLoginLogByOrganizationId")
    public ResponseVo<PageData> findLoginLogByOrganizationId(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            PageData pageData = loginService.findLoginLogByOrganizationId(jsonObject,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【登录请求类】查询登录日志失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
