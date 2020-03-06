package com.rbi.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.dto.Organization2DTO;
import com.rbi.admin.entity.dto.OrganizationDTO;
import com.rbi.admin.entity.edo.OrganizationDO;
import com.rbi.admin.service.OrganizationService;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseVo addOrganization(@RequestBody JSONObject request){
        try {
            OrganizationDO organizationDO = JSON.toJavaObject(request,OrganizationDO.class);
            String status = organizationService.addOrganization(organizationDO);
            if (status == "10000"){
                return ResponseVo.build("1000","新增成功");
            }else {
                return ResponseVo.build("1002","组织名重复，请修改组织名");
            }
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseVo updateOrganization(@RequestBody JSONObject request){
        try {
            OrganizationDO organizationDO = JSON.toJavaObject(request,OrganizationDO.class);
            organizationService.updateOrganization(organizationDO);
            return ResponseVo.build("1000","修改成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }


    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public ResponseVo<OrganizationDO> findById(@RequestBody JSONObject request){
        try {
            int id = request.getInteger("id");
            OrganizationDO organizationDO = organizationService.findById(id);
            return ResponseVo.build("1000","修改成功",organizationDO);
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
            PageData pageData = organizationService.findByPage(pageNo,pageSize);
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
            organizationService.deleteByIds(result);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/findOrganizationId",method = RequestMethod.POST)
    public ResponseVo<List<OrganizationDTO>> findOrganizationId(){
        try {
            List<OrganizationDTO> organizationDTOS =  organizationService.findOrganizationId();
            return ResponseVo.build("1000","查询成功",organizationDTOS);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/choosePid")
    public ResponseVo<List<Organization2DTO>> findAll(){
        try {
            List<Organization2DTO> organization2DTOS = organizationService.findTree();
            return ResponseVo.build("1000","请求成功",organization2DTOS);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }




}
