package com.rbi.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.rbi.service.RoleService;
import com.rbi.util.PageData;
import com.rbi.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/role")
public class RoleAdminController {


    private final static Logger logger = LoggerFactory.getLogger(RoleAdminController.class);

    @Autowired
    RoleService roleService;

    @PostMapping("/findAllByPage")
    public ResponseVo<PageData> addPermit(@RequestBody JSONObject jsonObject){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = roleService.findAllPage(pageNo,pageSize);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【角色管理请求类】分页查询角色数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败,ERROR: {}");
        }
    }

}
