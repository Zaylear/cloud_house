package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/parkingSpace")
public class ParkingSpaceController {

    private final static Logger logger = LoggerFactory.getLogger(ParkingSpaceController.class);

    @Autowired
    ParkingSpaceService parkingSpaceService;

    /**
     * 表格引入
     */
    @PostMapping("/import")
    public ResponseVo excelImport(MultipartFile file, HttpServletRequest request) {
        try {
                String token = request.getHeader("appkey");
                String userId = JwtToken.getClaim(token, "userId");
                Map<String,Object> map = parkingSpaceService.excelImport(file, userId);
                return ResponseVo.build("1000", "导入完成", map);
        } catch (Exception e) {
            logger.error("【业主信息导入请求类】批量处理业主信息失败！，ERROR：{}", e);
            return ResponseVo.build("1002", "服务器处理异常，ERROR：{}", e);
        }
    }

    /**
     * 新增车位基础信息信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ResponseVo add(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token, "userId");
            String status = parkingSpaceService.add(jsonObject, userId);
            if (status.equals("10000")) {
                return ResponseVo.build("1000", "请求成功");
            } else {
                return ResponseVo.build("1002", status);
            }
        }catch (JSONException e){
            logger.error("【车位信息新增请求类】新增车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","数据类型错误");
        }catch (NumberFormatException e){
            logger.error("【车位信息新增请求类】新增车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","数据类型错误");
        } catch (Exception e) {
            logger.error("【车位信息新增请求类】新增车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 更新车位基础信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/update")
    public ResponseVo update(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            parkingSpaceService.update(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        }catch (JSONException e){
            logger.error("【更新车位基础信息请求类】更新车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","数据类型错误");
        }catch (NumberFormatException e){
            logger.error("【更新车位基础信息请求类】更新车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","数据类型错误");
        } catch (Exception e) {
            logger.error("【更新车位基础信息请求类】更新车位基础信息失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     * 批量删除车位基础信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/deleteByIds")
    public ResponseVo<Object> deleteByIds(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            List<Integer> idList = new ArrayList<>();
            for (String id:ids){
                idList.add(Integer.parseInt(id));
            }
            parkingSpaceService.deleteByIds(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (NumberFormatException e) {
            logger.error("【车位信息删除请求类】批量删除车位基础信息失败，ERROR：{}",e);
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
                    PageData pageData = parkingSpaceService.findFirstByPage(pageNo,pageSize,organizationId);
                    return ResponseVo.build("1000","小区车位查询成功",pageData);
                }else if (level.equals("1")) {
                    PageData pageData = parkingSpaceService.findVillage(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "小区车位查询成功", pageData);
                } else if (level.equals("2")) {
                    PageData pageData = parkingSpaceService.findRegion(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "地块车位查询成功", pageData);
                } else if (level.equals("3")) {
                    PageData pageData = parkingSpaceService.findBuilding(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "楼栋车位查询成功", pageData);
                } else if (level.equals("4")) {
                    PageData pageData = parkingSpaceService.findParkingSpaceCode(code, pageNo, pageSize);
                    return ResponseVo.build("1000", "车位查询成功", pageData);
                } else {
                    return ResponseVo.build("1002", "异常查询");
                }
            }else{
                PageData pageData = parkingSpaceService.findFirstByPage(pageNo,pageSize,organizationId);
                return ResponseVo.build("1000","小区车位查询成功",pageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002", "服务器处理异常");
        }
    }

}
