package com.rbi.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.SystemSettingDO;
import com.rbi.admin.entity.dto.SystemSettingDTO;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/setting")
public class SystemSettingController {
    private static final Logger logger = LoggerFactory.getLogger(SystemSettingDO.class);

    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    StructureService structureService;

    @RequestMapping(value = "/findSettingType", method = RequestMethod.POST)
    public ResponseVo<List<SystemSettingDTO>>findSettingType(){
        try {
            List<SystemSettingDTO> systemSettingDTOS = systemSettingService.findSettingType();
            return ResponseVo.build("1000","",systemSettingDTOS);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseVo<Object> addSetting(@RequestBody JSONObject json,HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            String organizationId = systemSettingService.findOrganizationId(userId);

            SystemSettingDO systemSettingDO = JSON.toJavaObject(json, SystemSettingDO.class);
            String settingCode = organizationId+"-" + PinYin.getPinYinHeadChar(systemSettingDO.getSettingName()).toUpperCase();//+"-"+ Tools.randLetter(2)
            systemSettingDO.setSettingCode(settingCode);
            systemSettingDO.setOrganizationId(organizationId);
            systemSettingService.addSetting(systemSettingDO);

            logger.info("【设置添加成功】名称:{},类型:{}", systemSettingDO.getSettingName(), systemSettingDO.getSettingType());
            return ResponseVo.build("1000", "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public ResponseVo<Object> delete(@RequestBody JSONObject request) {
        try {
            int id  = request.getInteger("id");
            systemSettingService.deleteSetting(id);
            return ResponseVo.build("1000", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseVo<Object> updateSetting(@RequestBody JSONObject request) {
        try {
            SystemSettingDO systemSettingDO = JSON.toJavaObject(request, SystemSettingDO.class);
            systemSettingService.updateSetting(systemSettingDO);
            return ResponseVo.build("1000", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public ResponseVo<Object> findById(@RequestBody JSONObject request) {
        try {
            int id = Integer.parseInt(request.getString("id"));
            SystemSettingDO systemSettingDO = systemSettingService.findById(id);
            return ResponseVo.build("1000", "请求成功", systemSettingDO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }




    @RequestMapping(value = "/findByPage", method = RequestMethod.POST)
    public ResponseVo<PageData> findByPage(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            PageData pageData = systemSettingService.findByPage(organizationId,pageNo,pageSize);
            return ResponseVo.build("1000", "请求成功", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
    public ResponseVo deleteByIds(@RequestBody JSONObject request){
        try {
            JSONArray result = request.getJSONArray("data");
            systemSettingService.deleteByIds(result);
            return ResponseVo.build("1000","接收成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/findByPageA", method = RequestMethod.POST)
    public ResponseVo<PageData> findByPageA(@RequestBody JSONObject json) {
        try {
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            PageData pageData = systemSettingService.findByPageA(pageNo,pageSize);
            return ResponseVo.build("1000", "请求成功", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/deleteByIdsA",method = RequestMethod.POST)
    public ResponseVo deleteByIdsA(@RequestBody JSONObject request){
        try {
            JSONArray result = request.getJSONArray("data");
            systemSettingService.deleteByIdsA(result);
            return ResponseVo.build("1000","接收成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/updateA", method = RequestMethod.POST)
    public ResponseVo<Object> updateSettingA(@RequestBody JSONObject request) {
        try {
            SystemSettingDO systemSettingDO = JSON.toJavaObject(request, SystemSettingDO.class);
            systemSettingService.updateSetting(systemSettingDO);
            return ResponseVo.build("1000", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/addA", method = RequestMethod.POST)
    public ResponseVo<Object> addSettingA(@RequestBody JSONObject json,HttpServletRequest request) {
        try {
            SystemSettingDO systemSettingDO = JSON.toJavaObject(json, SystemSettingDO.class);
            systemSettingService.addSetting(systemSettingDO);
            logger.info("【设置添加成功】名称:{},类型:{}", systemSettingDO.getSettingName(), systemSettingDO.getSettingType());
            return ResponseVo.build("1000", "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/findChargeStatus", method = RequestMethod.POST)
    public ResponseVo<Object> findChoose2() {
        try {
            List<SystemSettingDTO> systemSettingDTOS = systemSettingService.findChoose2("CHARGE-STATUS");
            return ResponseVo.build("1000", "新增成功",systemSettingDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器处理异常error:{}", e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }



    @PostMapping("/findAdmChoose")
    public ResponseVo<Map<String,Object>> findAdmChoose(@RequestBody JSONObject json) {
        try {
            JSONArray array = json.getJSONArray("data");
            Map<String,Object> map = systemSettingService.findAdmChoose(array);
            return ResponseVo.build("1000", "查询成功",map);
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/findPaymentMethod")
    public ResponseVo<Map<String,Object>> findPaymentMethod() {
        try {
            Map<String,Object> map = systemSettingService.findPaymentMethod();
            return ResponseVo.build("1000", "查询成功",map);
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @PostMapping("/findNatChoose")
    public ResponseVo<Map<String,Object>> findNatChoose(@RequestBody JSONObject json,HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = structureService.findOrganizationId(userId);
            JSONArray array = json.getJSONArray("data");
            Map<String,Object> map = systemSettingService.findNatChoose(array,organizationId);
            return ResponseVo.build("1000", "查询成功",map);
        } catch (Exception e) {
            System.out.println("错误:" + e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }



}





