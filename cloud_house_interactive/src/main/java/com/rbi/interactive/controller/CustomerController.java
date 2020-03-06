package com.rbi.interactive.controller;


import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.CustomerException;
import com.rbi.interactive.entity.CustomerInfoDO;
import com.rbi.interactive.service.CustomerService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    /**
     * 通过手机号、房间号查询客户住房信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/findByMobilePhone")
    public ResponseVo<Map<String,Object>> findByMobilePhone(@RequestBody JSONObject jsonObject){
        try {
            String phone = jsonObject.getString("mobilePhone");
            String roomCode = jsonObject.getString("roomCode");
            Map<String,Object> map = customerService.findByMobilePhone(phone,roomCode);

            if ("1000".equals(map.get("status"))){
                map.remove("status");
                return ResponseVo.build("1000","请求成功！",map);
            }else if ("1001".equals(map.get("status"))){
                return ResponseVo.build("10051","客户不存在！");
            }else{
                return ResponseVo.build("10052","客户与房间未匹配！");
            }
        } catch (Exception e) {
            logger.error("【客户信息查询类】根据手机号和房间号查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/findByRoomCode")
    public ResponseVo<Map<String,Object>> findByRoomCode(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = customerService.findByRoomCode(jsonObject,userId);
            return ResponseVo.build("1000","查询成功",map);
        }catch (CustomerException e){
            logger.error("【客户信息查询类】该房号没有业主信息，ERROR：{}",e);
            return ResponseVo.build("1005","该房号没有业主信息");
        }catch (Exception e) {
            logger.error("【客户信息查询类】根据房间号查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
