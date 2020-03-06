package com.rbi.admin.controller.connect;

import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.dto.ChartDTO;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.ChartService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    ChartService chartService;
    @Autowired
    SystemSettingService systemSettingService;

    @PostMapping("/nowChart")
    public ResponseVo<List<ChartDTO>> nowChart(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String roomCode = json.getString("roomCode");
            List<ChartDTO> chartDTOS = chartService.nowChart(roomCode, organizationId);
            return ResponseVo.build("1000", "查询成功", chartDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/yearChart")
    public ResponseVo<List<ChartDTO>> yearChart(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String roomCode = json.getString("roomCode");
            List<ChartDTO> chartDTOS = chartService.yearChart(roomCode, organizationId);
            return ResponseVo.build("1000", "查询成功", chartDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/totalChart")
    public ResponseVo<List<ChartDTO>> totalChart(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String roomCode = json.getString("roomCode");
            List<ChartDTO> chartDTOS = chartService.totalChart(roomCode, organizationId);
            return ResponseVo.build("1000", "查询成功", chartDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }



}
