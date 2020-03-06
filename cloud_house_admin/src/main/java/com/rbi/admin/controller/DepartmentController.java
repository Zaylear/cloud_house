package com.rbi.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.DepartmentDO;
import com.rbi.admin.entity.dto.Department2DTO;
import com.rbi.admin.service.DepartmentService;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.PinYin;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    SystemSettingService systemSettingService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseVo addDepartment(@RequestBody JSONObject request){
        try {
            DepartmentDO departmentDO = JSON.toJavaObject(request,DepartmentDO.class);
            String deptId = departmentDO.getOrganizationId()+"-"+PinYin.getPinYinHeadChar(departmentDO.getDeptName()).toUpperCase();
            int i = departmentService.findNum(deptId);
            if (i != 0){
                return ResponseVo.build("1002","名称重复，请从新输入部门名称");
            }
            departmentDO.setDeptId(deptId);
            int flag = departmentDO.getFlag()+1;
            departmentDO.setFlag(flag);
            departmentService.addDepartment(departmentDO);
            return ResponseVo.build("1000","新增成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseVo updateDepartment(@RequestBody JSONObject request){
        try {
            DepartmentDO departmentDO = JSON.toJavaObject(request,DepartmentDO.class);
            int flag = departmentDO.getFlag()+1;
            departmentDO.setFlag(flag);
            departmentService.updateDepartment(departmentDO);
            return ResponseVo.build("1000","修改成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }



    @RequestMapping(value = "/findByPage",method = RequestMethod.POST)
    public ResponseVo<PageData> findByPage(@RequestBody JSONObject request){
        try {
            int pageNo = request.getInteger("pageNo");
            int pageSize = request.getInteger("pageSize");
            PageData pageData = departmentService.findByPage(pageNo,pageSize);
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

            departmentService.deleteByIds(result);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    //
    @PostMapping("/choosePid")
    public ResponseVo<List<Department2DTO>> findAll(@RequestBody JSONObject json){
        try {
            String organizationId = json.getString("organizationId");
            List<Department2DTO> department2DTOS = departmentService.findTree(organizationId);
            return ResponseVo.build("1000","请求成功",department2DTOS);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/choosePid2")
    public ResponseVo<List<Department2DTO>> findAll2(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            System.out.println(organizationId);
            List<Department2DTO> department2DTOS = departmentService.findTree(organizationId);
            return ResponseVo.build("1000","请求成功",department2DTOS);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


}
