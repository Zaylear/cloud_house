package com.rbi.admin.controller.connect;

import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.service.ExcelService;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    ExcelService excelService;


    @RequestMapping(value = "/owner", method = RequestMethod.POST)
    public ResponseVo OwnerExport(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String level = json.getString("level");
            String code = json.getString("code");
            int identity = json.getInteger("identity");
            String status = excelService.OwnerExport(level,code,identity,organizationId);
            String [] result = status.split(";");
            if ("10000".equals(result[0])){
                return ResponseVo.build("1000", result[1],result[2]);
            }else {
                return ResponseVo.build("1003", result[0]);
            }
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseVo.build("1003",e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public ResponseVo CustomerExport(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String level = json.getString("level");
            String code = json.getString("code");
            int identity = json.getInteger("identity");
            String status = excelService.CustomerExport(level,code,identity,organizationId);
            String [] result = status.split(";");
            if ("10000".equals(result[0])){
                return ResponseVo.build("1000", result[1],result[2]);
            }else {
                return ResponseVo.build("1002", result[0]);
            }
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseVo.build("1002",e.toString());
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/vacantRoom", method = RequestMethod.POST)
    public ResponseVo roomExport(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String level = json.getString("level");
            String code = json.getString("code");
            String status = excelService.vacantRoomExport(level,code,organizationId);

            String [] result = status.split(";");
            if ("10000".equals(result[0])){
                return ResponseVo.build("1000", result[1],result[2]);
            }else {
                return ResponseVo.build("1002", result[0]);
            }
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseVo.build("1002",e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }



}
