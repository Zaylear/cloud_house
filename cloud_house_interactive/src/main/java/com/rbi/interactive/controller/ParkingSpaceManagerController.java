package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.ParkingSpaceInfoDO;
import com.rbi.interactive.entity.VehicleInformationDO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.ParkingSpaceManagerService;
import com.rbi.interactive.utils.DateUtil;
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
import java.util.List;

@RestController
@RequestMapping("/parkingSpaceManager")
public class ParkingSpaceManagerController {

    private final static Logger logger = LoggerFactory.getLogger(ParkingSpaceManagerController.class);

    @Autowired
    ParkingSpaceManagerService parkingSpaceManagerService;

    /**
     * 根据房间编号查询车牌号
     * @param jsonObject
     * @return
     */
    @PostMapping("/findLicensePlateNumberByRoomCode")
    public ResponseVo<List<VehicleInformationDO>>  findLicensePlateNumberByRoomCode(@RequestBody JSONObject jsonObject){
        try {
            String roomCode = jsonObject.getString("roomCode");
            List<VehicleInformationDO> vehicleInformationDOList = parkingSpaceManagerService.findLicensePlateNumberByRoomCode(roomCode);
            return ResponseVo.build("1000","请求成功",vehicleInformationDOList);
        } catch (Exception e) {
            logger.error("【车位管理请求类】根据房间号查询车牌异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    /**
     * 查询收费项目
     * @param request
     * @return
     */
    @PostMapping("/findChargeItem")
    public ResponseVo findChargeItem(HttpServletRequest request){

        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            List<ChargeItemDO> chargeItemDOS = parkingSpaceManagerService.findChargeItem(userId);
            return ResponseVo.build("1000","请求成功",chargeItemDOS);
        } catch (Exception e) {
            logger.error("【车位管理请求类】查询收费项目异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 查询车位到期时间
     */
    @PostMapping("/findParkingSpaceDueTime")
    public ResponseVo findParkingSpaceDueTime(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String roomCode = jsonObject.getString("roomCode");
            String parkingSpaceCode = jsonObject.getString("parkingSpaceCode");
            String mobilePhone = jsonObject.getString("mobilePhone");

            String dueTime = parkingSpaceManagerService.findParkingSpaceDueTime(roomCode,parkingSpaceCode,mobilePhone,userId);
            return ResponseVo.build("1000","请求成功",dueTime);
        } catch (Exception e) {
            logger.error("【车位管理请求类】查询车位到期时间失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
    /**
     * 计算费用
     * @param jsonObject
     * @return
     */
    @PostMapping("/calculationCost")
    public ResponseVo calculationCost(@RequestBody JSONObject jsonObject){

        try {
            BillDetailedDO billDetailedDO = parkingSpaceManagerService.calculationCost(jsonObject);
            return ResponseVo.build("1000","请求成功",billDetailedDO);
        } catch (Exception e) {
            logger.error("【车位管理请求类】计算车位费费用异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 通过地块编号查询车位信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/findParkingSpaceCodeByRegionCode")
    public ResponseVo findParkingSpaceCodeByRegionCode(@RequestBody JSONObject jsonObject){
        try {
            String regionCode = jsonObject.getString("regionCode");
            List<ParkingSpaceInfoDO> parkingSpaceInfoDOS = parkingSpaceManagerService.findParkingSpaceCodeByRegionCode(regionCode);
            return ResponseVo.build("1000","请求成功",parkingSpaceInfoDOS);
        } catch (Exception e) {
            logger.error("【车位管理请求类】根据地块查询车位信息异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    /**
     * 车位费用新增
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ResponseVo add(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            parkingSpaceManagerService.add(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【车位管理请求类】车位费新增异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询费用单
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByPage")
    public ResponseVo findByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = parkingSpaceManagerService.findByPage(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【车位管理请求类】车位费新增异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
