package com.rbi.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.CustomerInfoDO;
import com.rbi.admin.entity.dto.RoomUserDTO;
import com.rbi.admin.service.CustomerInfoService;
import com.rbi.admin.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/customer")
public class CustomerInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);

    @Autowired
    CustomerInfoService customerInfoService;

    @RequestMapping(value = "/findByUserIds",method = RequestMethod.POST)
    public ResponseVo<List<CustomerInfoDO>>findByIds(@RequestBody JSONObject request){
        try {
            String userIds = request.getString("userIds");
            List<CustomerInfoDO> customerDOS = customerInfoService.findByUserIds(userIds);
            return ResponseVo.build("1000","成功",customerDOS);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }


    @RequestMapping(value = "/findByOpenId",method = RequestMethod.POST)
    public ResponseVo<CustomerInfoDO>findByOpenId(@RequestBody JSONObject request){
        try {
            String openId = request.getString("openId");
            CustomerInfoDO customerInfoDO = customerInfoService.finedByOpenId(openId);
            return ResponseVo.build("1000","成功",customerInfoDO);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    /**
     * 获取指定的房间信息和用户信息
     * @param
     * @return
     */
    @RequestMapping(value = "/findRoomUser",method = RequestMethod.POST)
    public ResponseVo findRoomUser (@RequestBody JSONObject request){
        String jj = request.getJSONArray("data").toJSONString();
        List list1 = JSON.parseArray(jj,RoomUserDTO.class);
        try {
            List resultList = customerInfoService.findRoomUser(list1);
            return ResponseVo.build("1000","成功",resultList);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

}
