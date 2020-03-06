package com.rbi.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.service.VillageService;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.PinYin;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/village")
public class VillageController {
        @Autowired
        VillageService villageService;

        @RequestMapping(value = "/findByPage",method = RequestMethod.POST)
        public ResponseVo<PageData> findByPage(@RequestBody JSONObject request){
            try {
                int pageNo = request.getInteger("pageNo");
                int pageSize = request.getInteger("pageSize");
                PageData pageData = villageService.findByPage(pageNo,pageSize);
                return ResponseVo.build("1000","查询成功",pageData);
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002","服务器处理异常");
            }
        }

        @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
        public ResponseVo deleteByIds(@RequestBody JSONObject request){
            try {
                JSONArray result = request.getJSONArray("data");
                villageService.deleteByIds(result);
                return ResponseVo.build("1000","删除成功");
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002", "服务器处理异常");
            }
        }

        @RequestMapping(value = "/update",method = RequestMethod.POST)
        public ResponseVo update(@RequestParam("id")Integer id,@RequestParam("villageName")String villageName,
                                 @RequestParam("organizationId")String organizationId, @RequestParam("organizationName")String organizationName,
                                 @RequestParam("constructionArea")Double constructionArea, @RequestParam("greeningRate")Double greeningRate,
                                 @RequestParam("publicArea")Double publicArea, @RequestParam("districtCode")String districtCode,
                                 @RequestParam("districtName")String districtName,@RequestParam("enable")String enable,@RequestParam("idt")String idt,
                                 @RequestParam(value="file",required=false) MultipartFile img){
            try {
                villageService.update(id,villageName,organizationId, organizationName,
                        constructionArea, greeningRate, publicArea, districtCode,
                        districtName,enable, idt,img);
                return ResponseVo.build("1000","修改成功");
            }catch (Exception e){
                System.out.println(e);
                return ResponseVo.build("1002","服务器处理异常");
            }
        }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseVo<String> addFile(@RequestParam("villageName")String villageName,
                                      @RequestParam("organizationId")String organizationId, @RequestParam("organizationName")String organizationName,
                                      @RequestParam("constructionArea")Double constructionArea, @RequestParam("greeningRate")Double greeningRate,
                                      @RequestParam("publicArea")Double publicArea, @RequestParam("districtCode")String districtCode,
                                      @RequestParam("districtName")String districtName,
                                      @RequestParam(value="file",required=false) MultipartFile img) throws IOException {
        try {
            String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();
            villageService.add(villageCode,villageName,
                    organizationId,organizationName,
                    constructionArea, greeningRate,
                    publicArea, districtCode,
                    districtName, img);
            return ResponseVo.build("200", "插入成功");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("500", "处理异常");
        }
    }

}
