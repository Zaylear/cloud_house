package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.dto.RoomInfoTreeDTO;
import com.rbi.interactive.service.RoomInfoTreeService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class RoomInfoTreeController {

    private final static Logger logger = LoggerFactory.getLogger(RoomInfoTreeController.class);

    @Autowired
    RoomInfoTreeService roomInfoTreeService;

    /**
     * 通过手机号查询客户住房信息，树结构
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/roomInfo/Tree")
    public ResponseVo roomInfoTree(@RequestBody JSONObject jsonObject, HttpServletRequest request){

        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = roomInfoTreeService.findRoomInfoByMobilePhone(userId,jsonObject.getString("mobilePhone"));
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【小区树请求类】服务器处理异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }
}
