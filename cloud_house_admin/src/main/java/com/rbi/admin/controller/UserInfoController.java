package com.rbi.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.UserInfoDO;
import com.rbi.admin.entity.dto.WxUserInfoDTO;
import com.rbi.admin.entity.dto.User2DTO;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.UserInfoService;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping(value = "/user")
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    StructureService structureService;
    @Autowired
    SystemSettingService systemSettingService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseVo<Object> add(@RequestBody JSONObject json,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = structureService.findOrganizationId(userId);
            String organizationName = structureService.findOrganizationName(userId);
            UserInfoDO userInfoDO = JSON.toJavaObject(json,UserInfoDO.class);
            userInfoDO.setOrganizationId(organizationId);
            userInfoDO.setOrganizationName(organizationName);
            String status = userInfoService.addUserInfo(userInfoDO);
            if (status.equals("10000")){
                logger.info("【用户添加成功】用户名：{}",userInfoDO.getUsername());
                return ResponseVo.build("1000","添加成功");
            } else{
                return ResponseVo.build("1003",status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "输入参数类型错误");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "输入参数类型错误");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }


    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseVo<Object>update(@RequestBody JSONObject json,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = structureService.findOrganizationId(userId);
            String organizationName = structureService.findOrganizationName(userId);
            UserInfoDO userInfoDO = JSON.toJavaObject(json,UserInfoDO.class);
            userInfoDO.setOrganizationId(organizationId);
            userInfoDO.setOrganizationName(organizationName);
            String status = userInfoService.updateUserInfo(userInfoDO);
            if (status == "10000"){
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
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }



/**
*修改主题颜色
* */
    @RequestMapping(value = "/updateTheme",method = RequestMethod.POST)
    public ResponseVo<Object>updateTheme(@RequestBody JSONObject json,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String theme = json.getString("theme");
            userInfoService.updateTheme(theme,userId);
            return ResponseVo.build("1000","修改成功");
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }


    @RequestMapping(value = "/findByPage",method = RequestMethod.POST)
    public ResponseVo<PageData> findByPage(@RequestBody JSONObject json, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = structureService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            PageData pageData = userInfoService.findByPage(organizationId,pageNo,pageSize);
            return ResponseVo.build("1000","查询成功",pageData);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
    public ResponseVo deleteByIdsA(@RequestBody JSONObject request){
        try {
            JSONArray result = request.getJSONArray("data");
            userInfoService.deleteByIds(result);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    //userId 查询用户信息
    @RequestMapping(value = "/findByUId",method = RequestMethod.POST)
    public ResponseVo<Object>findById(@RequestBody JSONObject request){
        try {
            String userId = request.getString("userId");
            UserInfoDO userInfoDO1 = userInfoService.findByUserId(userId);
            return ResponseVo.build("1000","查询成功",userInfoDO1);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    //userId用户信息+组织机构信息
    @RequestMapping(value = "/findByUserId",method = RequestMethod.POST)
    public ResponseVo<Object>findByIdM(@RequestBody JSONObject request){
        try {
            String userId = request.getString("userId");
            User2DTO userDO = userInfoService.findMById(userId);
            return ResponseVo.build("1000","查询成功",userDO);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    /**
     * 查询用户是否存在
     * 否则新增 并返回userId
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/wxAddUser",method = RequestMethod.POST)
    public ResponseVo wxAddUser(@RequestBody JSONObject jsonObject){
        try {
            WxUserInfoDTO wxUserInfoDTO=JSON.toJavaObject(jsonObject,WxUserInfoDTO.class);
            String userId=this.userInfoService.findUserIdByPhone(wxUserInfoDTO);
            return ResponseVo.build("1000","操作成功",userId);
        }catch (Exception e){
            return ResponseVo.build("1005","服务器处理异常");
        }
    }

    /** 4 姓名
     * */
    @RequestMapping(value = "/findByName", method = RequestMethod.POST)
    public ResponseVo<PageData> findByName(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String realName = json.getString("realName");
            PageData pageData = userInfoService.findByName(organizationId,realName, pageNo, pageSize);
            return ResponseVo.build("1000", "查询成功", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    public ResponseVo<Object> fresh(@RequestBody JSONObject json){
        try {
            String userId = json.getString("userId");
            String phone = json.getString("phone");
            String salt = json.getString("salt");
            userInfoService.resetPassword(userId,phone,salt);
            return ResponseVo.build("1000","密码重置成功");
        } catch (Exception e) {
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }


}
