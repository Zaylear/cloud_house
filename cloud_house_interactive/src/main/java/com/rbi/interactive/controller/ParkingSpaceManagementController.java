package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import com.rbi.interactive.service.ParkingSpaceManagementService;
import com.rbi.interactive.service.ParkingSpaceService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/parkingSpaceManagement")
public class ParkingSpaceManagementController {
    private final static Logger logger = LoggerFactory.getLogger(ParkingSpaceManagementController.class);
    @Autowired
    ParkingSpaceManagementService parkingSpaceManagementService;

    @Autowired
    ParkingSpaceService parkingSpaceService;

    @PostMapping("/add")
    public ResponseVo add(@RequestBody JSONObject json, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String status = parkingSpaceManagementService.add(json,userId);
            if (status.equals("10000")){
                return ResponseVo.build("1000","绑定成功");
            }else {
                return ResponseVo.build("1005","车位重复绑定");
            }
        }catch (Exception e){
            logger.error("【车位绑定新增请求类】绑定失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/findParkingCode")
    public ResponseVo<JSONArray> findParkingCode(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            JSONArray jsonArray = parkingSpaceManagementService.findParkingSpaceCode(userId);
            return ResponseVo.build("1000","查询成功",jsonArray);
        }catch (Exception e){
            logger.error("【车位绑定新增请求类】绑定失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    @PostMapping("/excelImport")
    public ResponseVo excelImport(MultipartFile file, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String, Object> map = parkingSpaceManagementService.excelImport(file,userId);
            return ResponseVo.build("1000","导入完成",map);
        }catch (Exception e){
            logger.error("【导入失败】，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    /**
     *分页查询车位基础信息
     * @return
     */
    @RequestMapping(value = "/findParkingAllByPage", method = RequestMethod.POST)
    public ResponseVo<PageData> findOwnerAllByPage(@RequestBody JSONObject json, HttpServletRequest request) throws Exception {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = parkingSpaceService.findOrganizationId(userId);
            String code = json.getString("code");
            String level = json.getString("level");
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String type = json.getString("type");
            if (type.equals("车位")) {
                if (code == null || "".equals(code)) {
                    PageData pageData = parkingSpaceManagementService.findFirstByPage(pageNo,pageSize,organizationId);
                    return ResponseVo.build("1000","小区车位查询成功",pageData);
                }else if (level.equals("1")) {
                    PageData pageData = parkingSpaceManagementService.findVillage(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区车位查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = parkingSpaceManagementService.findRegion(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块车位查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = parkingSpaceManagementService.findBuilding(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "楼栋车位查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = parkingSpaceManagementService.findParkingSpaceCode(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "车位查询成功", pageData);
                } else {
                    return ResponseVo.build("1002", "异常查询");
                }
            }else{
                PageData pageData = parkingSpaceManagementService.findFirstByPage(pageNo,pageSize,organizationId);
                return ResponseVo.build("1000","小区车位查询成功",pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @PostMapping("/delete")
    public ResponseVo delete(@RequestBody JSONObject json){
        try {
            JSONArray jsonArray = json.getJSONArray("data");
            parkingSpaceManagementService.deleteById(jsonArray);
            return ResponseVo.build("1000","删除成功");
        }catch (Exception e){
            logger.error("【车位绑定信息删除失败】删除失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    @PostMapping("/update")
    public ResponseVo update(@RequestBody JSONObject jsonObject){
        try {
            ParkingSpaceManagementDO parkingSpaceManagementDO = JSONObject.parseObject(jsonObject.toJSONString(),ParkingSpaceManagementDO.class);
            parkingSpaceManagementService.update(parkingSpaceManagementDO);
            return ResponseVo.build("1000","请求成功");
        }catch (JSONException e){
            logger.error("【更新车位基础信息请求类】更新车位失败，ERROR：{}",e);
            return ResponseVo.build("1002","数据类型错误");
        }catch (NumberFormatException e){
            logger.error("【更新车位基础信息请求类】更新车位失败，ERROR：{}",e);
            return ResponseVo.build("1002","数据类型错误");
        } catch (Exception e) {
            logger.error("【更新车位基础信息请求类】更新车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }


}
