package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ChargeItemDO;
import com.rbi.interactive.entity.RoomAndChargeItemsDO;
import com.rbi.interactive.service.BaseCofingService;
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
@RequestMapping("/base/config")
public class BaseCofingController {

    private final static Logger logger = LoggerFactory.getLogger(BaseCofingController.class);

    @Autowired
    BaseCofingService baseCofingService;

    /**
     * 通过机构ID查询所有收费项目
     */
    @PostMapping("/findChargeItemByOrganizationId")
    public ResponseVo<List<ChargeItemDO>> findChargeItemByOrganizationId(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            List<ChargeItemDO> chargeItemDOS = baseCofingService.findChargeItemByOrganizationId(userId);
            return ResponseVo.build("1000","请求成功",chargeItemDOS);
        } catch (Exception e) {
            logger.error("【基础配置请求类】通过机构ID查询所有收费项目异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 新增房间收费项目配置信息
     */
    @PostMapping("/addChargeItemAndRoom")
    public ResponseVo bindChargeItemAndRoom(@RequestBody JSONObject jsonObject, HttpServletRequest request){

        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            RoomAndChargeItemsDO roomAndChargeItemsDO = JSONObject.parseObject(jsonObject.toJSONString(),RoomAndChargeItemsDO.class);
            baseCofingService.bindChargeItemAndRoom(roomAndChargeItemsDO,userId);

            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【基础配置请求类】新增房间收费项目配置信息，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询房间收费项目配置信息
     */
    @PostMapping("/findRoomAndChargeItemByPage")
    public ResponseVo findRoomAndChargeItemByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            PageData pageData = baseCofingService.findRoomAndChargeItemByPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【基础配置请求类】分页查询房间收费项目配置信息，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }

    }

    /**
     * 更新房间收费项目配置信息
     * @return
     */
    @PostMapping("/updateRoomAndChargeItem")
    public ResponseVo updateRoomAndChargeItem(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            RoomAndChargeItemsDO roomAndChargeItemsDO = JSONObject.parseObject(jsonObject.toJSONString(),RoomAndChargeItemsDO.class);
            baseCofingService.updateRoomAndChargeItem(roomAndChargeItemsDO,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【基础配置请求类】更新房间收费项目配置信息，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 删除房间收费项目配置信息
     * @return
     */
    @PostMapping("/deleteRoomAndChargeItem")
    public ResponseVo deleteRoomAndChargeItem(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            List<Integer> idList = new ArrayList<>();
            for (String id:ids){
                idList.add(Integer.parseInt(id));
            }
            baseCofingService.deleteRoomAndChargeItem(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (NumberFormatException e) {
            logger.error("【基础配置请求类】删除房间收费项目配置信息，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
