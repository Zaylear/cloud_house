package com.rbi.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.entity.edo.DivisonDO;
import com.rbi.admin.entity.dto.DivisonDTO;
import com.rbi.admin.entity.dto.structure.DivisonCodeDTO;
import com.rbi.admin.service.DivisonService;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/divison")
public class DivisonController {
    @Autowired
    DivisonService divisonService;
    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    @PostMapping("/choosePid")
    public ResponseVo<List<DivisonDTO>> findAll(){
        try {
            List<DivisonDTO> divisonDTOS = divisonService.findTree();
            return ResponseVo.build("1000","请求成功",divisonDTOS);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/add")
    public ResponseVo add(@RequestBody JSONObject json){
        try {
            System.out.println("开始");
            DivisonDO divisonDO = JSON.toJavaObject(json,DivisonDO.class);
            List<DivisonCodeDTO> divisonCodeDTOS = divisonService.findDivisonCode();
            for (int i = 0;i<divisonCodeDTOS.size();i++){
                if (divisonDO.getDivisonCode().equals(divisonCodeDTOS.get(i).getDivisonCode())){
                    return ResponseVo.build("1005","编号重复");
                }
            }
            divisonService.addDivison(divisonDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/update")
    public ResponseVo update(@RequestBody JSONObject json){
        try {
            DivisonDO divisonDO = JSON.toJavaObject(json,DivisonDO.class);
            List<DivisonCodeDTO> divisonCodeDTOS = divisonService.findDivisonCodeNotId(divisonDO.getId());
            for (int i = 0;i<divisonCodeDTOS.size();i++){
                if (divisonDO.getDivisonCode().equals(divisonCodeDTOS.get(i).getDivisonCode())){
                    return ResponseVo.build("1005","编号重复");
                }
            }
            divisonService.updateDivison(divisonDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @RequestMapping(value = "/deleteByIds",method = RequestMethod.POST)
    public ResponseVo deleteByIdsA(@RequestBody JSONObject request){
        try {
            JSONArray result = request.getJSONArray("data");
            divisonService.deleteByIds(result);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }
}
