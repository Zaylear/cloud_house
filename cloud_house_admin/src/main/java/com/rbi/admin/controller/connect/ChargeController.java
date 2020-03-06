package com.rbi.admin.controller.connect;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.ChargeItemDO;
import com.rbi.admin.entity.dto.ChargeDTO;
import com.rbi.admin.entity.dto.PropertyChargeDTO;
import com.rbi.admin.service.ChargeItemService;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.ChargeService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/charge")
public class ChargeController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeDTO.class);

    @Autowired
    ChargeService chargeService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    ChargeItemService chargeItemService;

    @RequestMapping(value = "/property",method = RequestMethod.POST)
    public ResponseVo<PropertyChargeDTO> propertyCharge(@RequestBody JSONObject request) {
        try {
            String chargeCode = request.getString("chargeCode");
            int datedif = Integer.parseInt(request.getString("datedif"));
            PropertyChargeDTO propertyChargeDTO= chargeService.findPropertyCharge(chargeCode,datedif);
            return ResponseVo.build("1000","查询成功",propertyChargeDTO);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByChargeCodes",method = RequestMethod.POST)
    public ResponseVo<List<ChargeItemDO>> findByChargeCodes(@RequestBody JSONObject request) {
        try {
            String chargeCodes = request.getString("chargeCodes");
            List<ChargeItemDO> chargeItemDOS = chargeService.findByChargeCodes(chargeCodes);
            return ResponseVo.build("1000", "查询成功", chargeItemDOS);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByPage",method = RequestMethod.POST)
    public ResponseVo<PageData> findByPage(@RequestBody JSONObject json, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            PageData pageData = chargeItemService.findByPage(organizationId,pageNo,pageSize);
            return ResponseVo.build("1000","查询成功",pageData);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }



    @RequestMapping(value = "/findChargeDetail",method = RequestMethod.POST)
    public ResponseVo<Map<String,Object>> findChargeDetail(@RequestBody JSONObject json){
        try {
            String chargeCode = json.getString("chargeCode");
            Map<String,Object> map  = chargeService.findChargeDetail(chargeCode);
            return ResponseVo.build("1000","查询成功",map);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseVo addCharge(@RequestBody JSONObject json,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String status = chargeService.add(json,organizationId);
            if (status.equals("10000")){
                return ResponseVo.build("1000","添加成功");
            }else{
                return ResponseVo.build("1003",status);
            }
        } catch (JSONException e) {
            return ResponseVo.build("1003", "输入参数类型错误");
        } catch (NumberFormatException e) {
            return ResponseVo.build("1003", "输入参数类型错误");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseVo updateCharge(@RequestBody JSONObject json,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String status = chargeService.update(json,organizationId);
            if (status.equals("10000")){
                return ResponseVo.build("1000","修改成功");
            }else {
                return ResponseVo.build("1003",status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "输入参数类型错误");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "输入参数类型错误");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
    public ResponseVo deleteByIds(@RequestBody JSONObject request){
        try {
            JSONArray result = request.getJSONArray("data");
            chargeService.deleteByIds(result);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/deleteChargeDetail",method = RequestMethod.POST)
    public ResponseVo deleteChargeDetail(@RequestBody JSONObject request){
        try {
            int id = request.getInteger("id");
            chargeService.deleteChargeDetail(id);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1000", "系统无id");
        }
    }

    @RequestMapping(value = "/findByChargeName",method = RequestMethod.POST)
    public ResponseVo<PageData> findByChargeName(@RequestBody JSONObject json, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String chargeName= json.getString("chargeName");
            PageData pageData = chargeService.findByChargeName(chargeName,organizationId,pageNo,pageSize);
            return ResponseVo.build("1000","查询成功",pageData);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }





}
