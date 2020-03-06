package com.rbi.admin.controller.connect;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.dto.structure.VillageStructureDTO;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.OwnerService;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.service.connect.VacantRoomService;
import com.rbi.admin.service.connect.VillageChooseService;
import com.rbi.admin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/vacantRoom")
public class VacantRoomController {
    @Autowired
    VacantRoomService vacantRoomService;
    @Autowired
    StructureService structureService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    OwnerService ownerService;
    @Autowired
    VillageChooseService villageChooseService;
    @Autowired
    RedisUtil redisUtil;


    /**树分页查询
     * */
    @RequestMapping(value = "/findRoomAllByPage", method = RequestMethod.POST)
    public ResponseVo<PageData> findOwnerAllByPage(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String code = json.getString("code");
            String level = json.getString("level");
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            String type = json.getString("type");

            if (type.equals("住宅")) {
                String roomType = "1";
                if (level.equals("1")) {
                    PageData pageData = vacantRoomService.findVillageByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区空置房查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = vacantRoomService.findRegionByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块空置房查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = vacantRoomService.findBuildingByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "栋栋空置房查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = vacantRoomService.findUnitByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "单元空置房查询成功", pageData);
                } else if (level.equals("5")) {
                    PageData pageData = vacantRoomService.findRoomByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "房间空置房查询成功", pageData);
                }else {
                    return ResponseVo.build("1003", "异常查询");
                }
            }else if (type.equals("商户")) {
                String roomType = "3";
                if (level.equals("1")) {
                    PageData pageData = vacantRoomService.findVillageByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区空置房查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = vacantRoomService.findRegionByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块空置房查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = vacantRoomService.findBuildingByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "栋栋空置房查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = vacantRoomService.findUnitByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "单元空置房查询成功", pageData);
                } else if (level.equals("5")) {
                    PageData pageData = vacantRoomService.findRoomByPage(code, roomType, pageNo, pageSize);
                    return ResponseVo.build("1000", "房间空置房查询成功", pageData);
                }else {
                    return ResponseVo.build("1003", "异常查询");
                }
            }else {
                String roomType = "1";
                String token = request.getHeader("appkey");
                String userId = JwtToken.getClaim(token, "userId");
                String organizationId = systemSettingService.findOrganizationId(userId);
                List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
                PageData pageData = vacantRoomService.findVillageByPage(villageStructureDTOS.get(0).getVillageCode(), roomType, pageNo, pageSize);
                return ResponseVo.build("1000", "小区空置房查询成功", pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }


    @RequestMapping(value = "/findRoomByRoomCode", method = RequestMethod.POST)
    public ResponseVo<PageData> findRoomByRoomCode(@RequestBody JSONObject json) {
        try {
            String roomCode = json.getString("roomCode");
            int pageNo = json.getInteger("pageNo");
            int pageSize = json.getInteger("pageSize");
            PageData pageData = vacantRoomService.findRoomByRoomCode(roomCode, pageNo, pageSize);
            return ResponseVo.build("1000", "查询成功", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

    @PostMapping("/addOwner2")
    public ResponseVo addSingleOwner(@RequestBody JSONObject json, HttpServletRequest request) {
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            String status = ownerService.addOwnerNew(json, userId);
            if (status.equals("1000")){
                redisUtil.set(Constants.REDISKEY_PROJECT+Constants.VILLAGE_TREE+organizationId,null);
                return ResponseVo.build("1000","操作成功");
            }else {
                return ResponseVo.build("1003",status);
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



}
