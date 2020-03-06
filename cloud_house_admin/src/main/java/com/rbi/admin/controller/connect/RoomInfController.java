package com.rbi.admin.controller.connect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.RoomInfoService;
import com.rbi.admin.service.connect.VillageChooseService;
import com.rbi.admin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/roomInfo")
public class RoomInfController {
    @Autowired
    RoomInfoService roomInfoService;
    @Autowired
    VillageChooseService villageChooseService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/deleteRoom")
    public ResponseVo deleteRoom(@RequestBody JSONObject json , HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);

            JSONArray result = json.getJSONArray("data");
            roomInfoService.deleteRoom(result);
            redisUtil.set(Constants.REDISKEY_PROJECT+Constants.VILLAGE_TREE+organizationId,null);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/logout")
    public ResponseVo logout(@RequestBody JSONObject json){
        try {
            roomInfoService.logout(json);
            return ResponseVo.build("1000","注销成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/deleteSingleCustomer")
    public ResponseVo deleteSingleCustomer(@RequestBody JSONObject json){
        try {
            roomInfoService.deleteSingleCustomer(json);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


}
