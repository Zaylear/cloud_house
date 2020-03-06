package com.rbi.controller;


import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.UserInfoDO;
import com.rbi.service.UserService;
import com.rbi.util.JwtToken;
import com.rbi.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    public final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * 根据用户ID查询用户信息
     * @param request
     * @return
     */
    @PostMapping("/findByUserId")
    public ResponseVo findByUserId(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            UserInfoDO userInfoDO = userService.findByUserId(userId);
            return ResponseVo.build("1000","请求成功",userInfoDO);
        } catch (Exception e) {
            logger.error("【用户请求请求类】根据用户Id查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     *更新用户密码
     * @param jsonObject
     * @return
     */
    @PostMapping("updatePassword")
    public ResponseVo updatePassword(@RequestBody JSONObject jsonObject){
        try {
            String newPassword = jsonObject.getString("newPassword");
            UserInfoDO userInfoDO = JSONObject.parseObject(jsonObject.toJSONString(),UserInfoDO.class);
            Boolean isSuccess = userService.updatePassword(userInfoDO,newPassword);
            if (isSuccess) {
                return ResponseVo.build("1000","请求成功");
            }else {
                return ResponseVo.build("1008","原始密码不匹配");
            }
        }catch (Exception e){
            logger.error("【用户请求请求类】修改用户密码异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 更新用户信息
     * @param jsonObject
     * @return
     */
    @PostMapping("update")
    public ResponseVo update(@RequestBody JSONObject jsonObject){
        try {
            UserInfoDO userInfoDO = JSONObject.parseObject(jsonObject.toJSONString(),UserInfoDO.class);
            Boolean isSuccess = userService.update(userInfoDO);
            if (isSuccess){
                return ResponseVo.build("1000","请求成功");
            }else {
                return ResponseVo.build("1005","参数已存在");
            }

        }catch (Exception e){
            logger.error("【用户请求请求类】更新用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
