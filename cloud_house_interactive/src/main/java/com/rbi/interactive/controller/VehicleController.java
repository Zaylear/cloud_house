package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ParkingSpaceInfoDO;
import com.rbi.interactive.entity.VehicleInformationDO;
import com.rbi.interactive.service.VehicleService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final static Logger logger = LoggerFactory.getLogger(ParkingSpaceController.class);

    @Autowired
    VehicleService vehicleService;

    @PostMapping("/leadingIn")
    public void leadingIn(){

    }

    /**
     * 新增车辆基础信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ResponseVo<Object> add(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            vehicleService.add(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【新增车辆信息】新增车辆信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 更新车辆基础信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/update")
    public ResponseVo<Object> update(@RequestBody JSONObject jsonObject){
        try {
            String id = jsonObject.getString("id");
            if (null==id||"null".equals(id)||"".equals(id)){
                return ResponseVo.build("1001","请求参数不能为空");
            }
            VehicleInformationDO vehicleInformationDO = JSONObject.parseObject(jsonObject.toJSONString(), VehicleInformationDO.class);
            vehicleService.update(vehicleInformationDO);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【更新车辆信息】更新车辆信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 批量删除车辆基础信息
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
            vehicleService.deleteByIds(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (NumberFormatException e) {
            logger.error("【删除车辆信息】删除车辆信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询车辆基础信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByPage")
    public ResponseVo<PageData> findByPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");


            PageData pageData = vehicleService.findByPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【车辆基础信息请求类】分页查询处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
