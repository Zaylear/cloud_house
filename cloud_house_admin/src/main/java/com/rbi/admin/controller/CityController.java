package com.rbi.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.CityDO;
import com.rbi.admin.service.CityService;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city")
public class CityController {

        @Autowired
        CityService cityService;


        @RequestMapping(value = "/findByPage",method = RequestMethod.POST)
        public ResponseVo<PageData> findByPage(@RequestBody JSONObject request){
            try {
                int pageNo = request.getInteger("pageNo");
                int pageSize = request.getInteger("pageSize");
                PageData pageData = cityService.findByPage(pageNo,pageSize);
                return ResponseVo.build("1000","查询成功",pageData);
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002","服务器处理异常");
            }
        }

        @RequestMapping(value = "/add",method = RequestMethod.POST)
        public ResponseVo add(@RequestBody JSONObject request){
            try {
                CityDO cityDO = JSON.toJavaObject(request,CityDO.class);
                cityService.add(cityDO);
                return ResponseVo.build("1000","新增成功");
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002","服务器处理异常");
            }
        }

        @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
        public ResponseVo deleteByIds(@RequestBody JSONObject request){
            try {
                JSONArray result = request.getJSONArray("data");
                cityService.deleteByIds(result);
                return ResponseVo.build("1000","删除成功");
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002", "服务器处理异常");
            }
        }

        @RequestMapping(value = "/update",method = RequestMethod.POST)
        public ResponseVo update(@RequestBody JSONObject request){
            try {
                CityDO cityDO = JSON.toJavaObject(request,CityDO.class);
                cityService.update(cityDO);
                return ResponseVo.build("1000","修改成功");
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002","服务器处理异常");
            }
        }
}
