package com.rbi.admin.controller.connect;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.dto.structure.CustomerNameDTO;
import com.rbi.admin.entity.dto.structure.VillageStructureDTO;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.OwnerService;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.service.connect.VillageChooseService;
import com.rbi.admin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    OwnerService ownerService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    StructureService structureService;
    @Autowired
    VillageChooseService villageChooseService;
    @Autowired
    RedisUtil redisUtil;

    /**树分页查询
    * */
    @RequestMapping(value = "/findOwnerAllByPage", method = RequestMethod.POST)
    public ResponseVo<PageData> findOwnerAllByPage(@RequestBody JSONObject json, HttpServletRequest request) throws Exception {
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
                    PageData pageData = ownerService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(),roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区业主查询成功", pageData);
                }
                if (level.equals("1")) {
                    PageData pageData = ownerService.findVillageByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区业主查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = ownerService.findRegionByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块业主查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = ownerService.findBuildingByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "楼栋业主查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = ownerService.findUnitByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "单元业主查询成功", pageData);
                } else if (level.equals("5")) {
                    PageData pageData = ownerService.findRoomByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "房间查询成功", pageData);
                } else {
                    return ResponseVo.build("1003", "异常查询");
                }
            }else if(type.equals("商户")) {
                int roomType = 3;
                if (code == null || "".equals(code)) {
                    List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
                    PageData pageData = ownerService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(),roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区业主查询成功", pageData);
                }
                if (level.equals("1")) {
                    PageData pageData = ownerService.findVillageByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区业主查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = ownerService.findRegionByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块业主查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = ownerService.findBuildingByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "楼栋业主查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = ownerService.findUnitByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "单元业主查询成功", pageData);
                } else if (level.equals("5")) {
                    PageData pageData = ownerService.findRoomByPage(code,roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "房间查询成功", pageData);
                } else {
                    return ResponseVo.build("1003", "异常查询");
                }
            }else{
                List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
                PageData pageData = ownerService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(),1, pageNo, pageSize);
                return ResponseVo.build("1000", "小区业主查询成功", pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    /** 1 房间号  2 手机号  3 身份证号  4 姓名
     * */
    @RequestMapping(value = "/findOwnerByCondition", method = RequestMethod.POST)
    public ResponseVo<PageData> findOwnerByCondition(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String condition = json.getString("condition");
            String value = json.getString("value");
            if (condition.equals("roomCode")) {
                PageData pageData = ownerService.findOwnerByRoomCode(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }else if (condition.equals("phone")){
                PageData pageData = ownerService.findOwnerByPhone(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }else if (condition.equals("idNumber")){
                PageData pageData = ownerService.findOwnerByIdNumber(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }else {
                PageData pageData = ownerService.findOwnerBySurname(value, organizationId, pageNo, pageSize);
                return ResponseVo.build("1000", "查询成功", pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/findToUpdate", method = RequestMethod.POST)
    public ResponseVo<Map<String, Object>> findToUpdate(@RequestBody JSONObject json) {
        try {
            String roomCode = json.getString("roomCode");
//            String customerUserId = json.getString("customerUserId");
            Map<String, Object> map = ownerService.findToUpdate(roomCode);
            return ResponseVo.build("1000", "查询成功", map);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @RequestMapping(value = "/findOwnerDetail", method = RequestMethod.POST)
    public ResponseVo<Map<String, Object>> findRoomDetail(@RequestBody JSONObject json,HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String roomCode = json.getString("roomCode");
            String customerUserId = json.getString("customerUserId");
            Map<String, Object> map = ownerService.findOwnerRoomDetail(organizationId,roomCode,customerUserId);
            return ResponseVo.build("1000", "查询成功", map);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    /**业主添加、业主修改新版
 * */
    @PostMapping("/addOwner")
    public ResponseVo addOwnerNew(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String status = ownerService.addOwner(json, userId);
            if (status.equals("1000")){
                redisUtil.set(Constants.REDISKEY_PROJECT+Constants.VILLAGE_TREE+organizationId,null);
                return ResponseVo.build("1000","操作成功");
            }else {
                return ResponseVo.build("1002",status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "房屋面积输入格式错误");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseVo.build("1003", "房屋面积输入格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }













    /**其他模块接口
     * */
    @RequestMapping(value = "/findCustomerByRoomCode", method = RequestMethod.POST)
    public ResponseVo<List<CustomerNameDTO>> findCustomerByRoomCode(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String roomCode = json.getString("roomCode");
            List<CustomerNameDTO> customerByRoomCode = ownerService.findCustomerByRoomCode(roomCode,organizationId);
            return ResponseVo.build("1000", "查询成功", customerByRoomCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }





}
