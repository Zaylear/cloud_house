package com.rbi.controller.interactive;

import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.RoleDO;
import com.rbi.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {

    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    RoleService roleService;

    @PostMapping("/findByOrganizationIdAndPage")
    public ResponseVo<PageData> findByOrganizationIdAndPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            Integer pageNo = jsonObject.getInteger("pageNo");
            Integer pageSize = jsonObject.getInteger("pageSize");

            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            PageData pageData = roleService.findByOrganizationIdAndPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        }catch (Exception e){
            logger.error("【角色信息管理类】分页查询角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }



    @PostMapping("/admin/findByPage")
    public ResponseVo findByPage(@RequestBody JSONObject jsonObject){
        try {
            Integer pageNo = jsonObject.getInteger("pageNo");
            Integer pageSize = jsonObject.getInteger("pageSize");
            String organizationId = jsonObject.getString("organizationId");
            String roleName = jsonObject.getString("roleName");

            PageData pageData = roleService.findByPage(pageNo,pageSize,organizationId,roleName);
            return ResponseVo.build("1000","请求成功",pageData);
        }catch (Exception e){
            logger.error("【角色信息管理类】分页查询管理端角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/add")
    public ResponseVo<String> add(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            RoleDO roleDO = JSONObject.parseObject(jsonObject.toJSONString(),RoleDO.class);
            roleService.add(roleDO,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【角色信息管理类】新增角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/admin/add")
    public ResponseVo addAdmin(@RequestBody JSONObject jsonObject){
        try {
            RoleDO roleDO = JSONObject.parseObject(jsonObject.toJSONString(),RoleDO.class);
            roleService.addAdmin(roleDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【角色信息管理类】新增角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/deleteByIds")
    public ResponseVo<String> deleteByIds(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            List<Integer> idList = new ArrayList<>();
            for (String id:ids){
                idList.add(Integer.parseInt(id));
            }
            roleService.deleteByIds(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【角色信息管理类】删除角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/update")
    public ResponseVo<String> update(@RequestBody JSONObject jsonObject){
        try {
            RoleDO roleDO = JSONObject.parseObject(jsonObject.toJSONString(),RoleDO.class);
            if (null==roleDO.getId()||null==roleDO.getOrganizationId()){
                return ResponseVo.build("1001","请求参数不能为空");
            }
            roleService.update(roleDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【角色信息管理类】更新角色信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
