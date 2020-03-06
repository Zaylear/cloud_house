package com.rbi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.RoleDO;
import com.rbi.entity.UserInfoDO;
import com.rbi.entity.dto.PermitDTO;
import com.rbi.entity.dto.RoleAndPermitDTO;
import com.rbi.entity.dto.UserAndRoleDTO;
import com.rbi.service.PermissionConfigurationService;
import com.rbi.util.JwtToken;
import com.rbi.util.PageData;
import com.rbi.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/permit/config")
public class PermissionConfigurationController {

    private final static Logger logger = LoggerFactory.getLogger(PermissionConfigurationController.class);

    @Autowired
    PermissionConfigurationService permissionConfigurationService;

    /**
     * 根据机构ID查询角色信息
     * @param request
     * @return
     */
    @PostMapping("/findRoleByOrganizationId")
    public ResponseVo<List<RoleDO>> findRoleByOrganizationId(HttpServletRequest request){

        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            List<RoleDO> roleDOS = permissionConfigurationService.findAllByOrganizationId(userId);
            return ResponseVo.build("1000","请求成功",roleDOS);
        } catch (Exception e) {
            logger.error("【权限配置类】查询角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/admin/findRoleByOrganizationId")
    public ResponseVo<List<RoleDO>> findRoleAllByOrganizationId(@RequestBody JSONObject jsonObject){

        try {
            String organizationId = jsonObject.getString("organizationId");
            List<RoleDO> roleDOS = permissionConfigurationService.findRoleAllByOrganizationId(organizationId);
            return ResponseVo.build("1000","请求成功",roleDOS);
        } catch (Exception e) {
            logger.error("【权限配置类】查询角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 查询所有权限信息
     * @return
     */
    @PostMapping("/findPermitAll")
    public ResponseVo<List<PermitDTO>> findAll(){
        try {
            return ResponseVo.build("1000","请求成功",permissionConfigurationService.findPermitAll());
        } catch (Exception e) {
            logger.error("【权限配置类】查询权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据角色编号查询权限信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/findPermitByRoleCode")
    public ResponseVo<List<RoleAndPermitDTO>> findPermitByRole(@RequestBody JSONObject jsonObject){

        try {
            String roleCode = jsonObject.getString("roleCode");
            List<RoleAndPermitDTO> roleAndPermitDTOS = permissionConfigurationService.findPermitByRole(roleCode);
            return ResponseVo.build("1000","请求成功",roleAndPermitDTOS);
        } catch (Exception e) {
            logger.error("【权限配置类】根据角色编号查询权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     * 根据用户编号查询角色信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/findRoleByUserId")
    public ResponseVo<List<UserAndRoleDTO>> findRoleByUserId(@RequestBody JSONObject jsonObject){

        try {
            String userId = jsonObject.getString("userId");
            List<UserAndRoleDTO> userAndRoleDTOS = permissionConfigurationService.findRoleByUserId(userId);

            return ResponseVo.build("1000","请求成功",userAndRoleDTOS);
        } catch (Exception e) {
            logger.error("【权限配置类】根据用户Id查询角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     *查询所有权限，供红鸟管理端使用
     * @return
     */
    @PostMapping("/admin/findPermitAll")
    public ResponseVo<List<PermitDTO>> findAdminPermitAll(){
        try {
            return ResponseVo.build("1000","请求成功",permissionConfigurationService.findAdminPermitAll());
        } catch (Exception e) {
            logger.error("【权限配置类】查询管理端权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据机构查询用户信息
     * @param request
     * @return
     */
    @PostMapping("/findUserInfoByOrganizationId")
    public ResponseVo<List<UserInfoDO>> findUserInfoByOrganizationId(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            return ResponseVo.build("1000","请求成功",permissionConfigurationService.findUserInfoByOrganizationId(userId));
        } catch (Exception e) {
            logger.error("【权限配置类】查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/admin/findUserInfoByOrganizationId")
    public ResponseVo<List<UserInfoDO>> findAdminUserInfoByOrganizationId(@RequestBody JSONObject jsonObject){
        try {
            String organizationId = jsonObject.getString("organizationId");
            return ResponseVo.build("1000","请求成功",permissionConfigurationService.findAdminUserInfoByOrganizationId(organizationId));
        } catch (Exception e) {
            logger.error("【权限配置类】查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 新增用户角色信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/addUserInfoAndRole")
    public ResponseVo<String> addUserInfoAndRole(@RequestBody JSONObject jsonObject){
        try {
            permissionConfigurationService.addUserInfoAndRole(jsonObject);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【权限配置类】查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 新增角色权限信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/addRoleAndPermit")
    public ResponseVo<String> addRoleAndPermit(@RequestBody JSONObject jsonObject){
        try {
            permissionConfigurationService.addRoleAndPermit(jsonObject);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【权限配置类】查询用户信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询用户角色信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findUserAndRolePage")
    public ResponseVo<PageData> findUserAndRolePage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            PageData pageData = permissionConfigurationService.findUserAndRolePage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【权限配置类】查询用户角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    @PostMapping("/admin/findUserAndRolePage")
    public ResponseVo<PageData> findAdminUserAndRolePage(@RequestBody JSONObject jsonObject){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String organizationId = jsonObject.getString("organizationId");
            PageData pageData = permissionConfigurationService.findAdminUserAndRolePage(pageNo,pageSize,organizationId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【权限配置类】查询用户角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询角色权限信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findRoleAndPermitPage")
    public ResponseVo<PageData> findRoleAndPermitPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            PageData pageData = permissionConfigurationService.findRoleAndPermitPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【权限配置类】查询角色权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/admin/findRoleAndPermitPage")
    public ResponseVo<PageData> findAdminRoleAndPermitPage(@RequestBody JSONObject jsonObject){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String organizationId = jsonObject.getString("organizationId");
            PageData pageData = permissionConfigurationService.findAdminRoleAndPermitPage(pageNo,pageSize,organizationId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【权限配置类】查询角色权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据id批量删除角色权限信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/deleteRoleAndPermitByIds")
    public ResponseVo<String> deleteRoleAndPermitByIds(@RequestBody JSONObject jsonObject){

        try {
//            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
//            List<Integer> idList = new ArrayList<>();
//            for (String id:ids){
//                idList.add(Integer.parseInt(id));
//            }
            JSONArray jsonObjects = jsonObject.getJSONArray("roleAndPermitCodes");
//            permissionConfigurationService.deleteRoleAndPermitByIds(idList);
            permissionConfigurationService.deleteRoleAndPermit(jsonObjects);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【权限配置类】删除角色权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据id批量删除用户角色信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/deleteUserAndRoleByIds")
    public ResponseVo<String> deleteUserAndRoleByIds(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            List<Integer> idList = new ArrayList<>();
            for (String id:ids){
                idList.add(Integer.parseInt(id));
            }

            permissionConfigurationService.deleteUserAndRoleByIds(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【权限配置类】删除角色权限信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

}
