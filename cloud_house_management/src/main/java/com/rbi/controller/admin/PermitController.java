package com.rbi.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.PermitDO;
import com.rbi.service.admin.PermitService;
import com.rbi.util.PageData;
import com.rbi.util.PinYin;
import com.rbi.util.ResponseVo;
import com.rbi.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/permit")
public class PermitController {

    private final static Logger logger = LoggerFactory.getLogger(PermitController.class);

    @Autowired
    PermitService permitService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseVo addPermit(@RequestBody JSONObject jsonObject){
        try {
            PermitDO permitDO = JSONObject.parseObject(jsonObject.toJSONString(),PermitDO.class);
            permitDO.setPermisCode(permitDO.getParentCode()+"-"+ PinYin.getPinYinHeadChar(permitDO.getTitle()).toUpperCase());
            Boolean isSuccess = permitService.addPermit(permitDO);
            if (isSuccess){
                return ResponseVo.build("1000","请求成功");
            }else {
                return ResponseVo.build("1005","数据已存在");
            }

        } catch (Exception e) {
            logger.error("【权限管理类】新增权限数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败,ERROR: {}");
        }
    }

    @PostMapping("/update")
    public ResponseVo<String> updatePermit(@RequestBody JSONObject jsonObject){
        try {
            PermitDO permitDO = JSONObject.parseObject(jsonObject.toJSONString(),PermitDO.class);
            permitService.updatePermit(permitDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【权限管理类】更新权限数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/findPermitByPage")
    public ResponseVo<PageData> findPermitByPage(@RequestBody JSONObject jsonObject){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = permitService.findPermitByPage(pageNo,pageSize);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【权限管理类】分页查询权限数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/deletePermitByPermitCodes")
    public ResponseVo<String> deletePermitByPermitCodes(@RequestBody JSONObject jsonObject){
        try {
            List<String> permitCodes = new ArrayList<>(Arrays.asList(jsonObject.getString("permitCodes").split(",")));
            permitService.deletePermitByPermitCodes(permitCodes);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【权限管理类】批量删除权限数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
