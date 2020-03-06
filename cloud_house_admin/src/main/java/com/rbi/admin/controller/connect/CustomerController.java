package com.rbi.admin.controller.connect;


import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.dto.structure.VillageStructureDTO;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.CustomerService;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.service.connect.VillageChooseService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    StructureService structureService;
    @Autowired
    VillageChooseService villageChooseService;

    @RequestMapping(value = "/findCustomerAllByPage", method = RequestMethod.POST)
    public ResponseVo<PageData> findCustomerAllByPage(@RequestBody JSONObject json,HttpServletRequest request) throws Exception{
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String code = json.getString("code");
            String level = json.getString("level");
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String type = json.getString("type");
            if (type.equals("住宅")) {
                int roomType = 1;
                if (code == null || "".equals(code)) {
                    List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
                    PageData pageData = customerService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(),roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区租客查询成功", pageData);
                }
                if (level.equals("1")) {
                    PageData pageData = customerService.findVillageByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区租客查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = customerService.findRegionByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块租客查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = customerService.findBuildingByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "栋租客查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = customerService.findUnitByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "单元租客查询成功", pageData);
                } else if (level.equals("5")) {
                    PageData pageData = customerService.findRoomByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "房间租客查询成功", pageData);
                } else {
                    return ResponseVo.build("1003", "异常查询");
                }
            }else if(type.equals("商户")) {
                int roomType = 3;
                if (code == null || "".equals(code)) {
                    List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
                    PageData pageData = customerService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(),roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区租客查询成功", pageData);
                }
                if (level.equals("1")) {
                    PageData pageData = customerService.findVillageByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区租客查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = customerService.findRegionByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块租客查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = customerService.findBuildingByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "楼栋租客查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = customerService.findUnitByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "单元租客查询成功", pageData);
                } else if (level.equals("5")) {
                    PageData pageData = customerService.findRoomByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "房间租客查询成功", pageData);
                } else {
                    return ResponseVo.build("1003", "异常查询");
                }
            }else{
                List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
                PageData pageData = customerService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(),1, pageNo, pageSize);
                return ResponseVo.build("1000", "小区租客查询成功", pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/findCustomerDetail", method = RequestMethod.POST)
    public ResponseVo<Map<String, Object>> findCustomerDetail(@RequestBody JSONObject json) {
        try {
            String roomCode = json.getString("roomCode");
            String customerUserId = json.getString("customerUserId");
            Map<String, Object> map = customerService.findCustomerDetail(roomCode, customerUserId);
            return ResponseVo.build("1000", "查询成功", map);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    /** 1 房间号  2 手机号  3 身份证号  4 姓名
     * */
    @RequestMapping(value = "/findCustomerByCondition", method = RequestMethod.POST)
    public ResponseVo<PageData> findCustomerByCondition(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String condition = json.getString("condition");
            String value = json.getString("value");
            if (condition.equals("roomCode")) {
                PageData pageData = customerService.findCustomerByRoomCode(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }else if (condition.equals("phone")){
                PageData pageData = customerService.findCustomerByPhone(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }else if (condition.equals("idNumber")){
                PageData pageData = customerService.findCustomerByIdNumber(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }else {
                PageData pageData = customerService.findCustomerBySurname(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/addCustomer")
    public ResponseVo addCustomer(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String status = customerService.addCustomer(json, userId);
            if (status.equals("10000")){
                return ResponseVo.build("1000","操作成功");
            }else {
                return ResponseVo.build("1002",status);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }



}
