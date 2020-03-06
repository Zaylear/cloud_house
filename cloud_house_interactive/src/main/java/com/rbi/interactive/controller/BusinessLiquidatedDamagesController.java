package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.dto.RoomCodeAndMobilePhoneDTO;
import com.rbi.interactive.service.BusinessLiquidatedDamagesService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/liquidated/damages")
public class BusinessLiquidatedDamagesController {

    private final static Logger logger = LoggerFactory.getLogger(BusinessLiquidatedDamagesController.class);


    @Autowired
    BusinessLiquidatedDamagesService businessLiquidatedDamagesService;

    /**
     * 批量计算违约金，通过表格导入的形式
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/batch/processing")
    public ResponseVo batchProcessingLiquidatedDamages(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1) {
                String token = request.getHeader("appkey");
                String userId = JwtToken.getClaim(token, "userId");
                Map<String, Object> map = businessLiquidatedDamagesService.batchProcessingLiquidatedDamages(file, userId);
                return ResponseVo.build("1000", "请求成功", map);
            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
        } catch (Exception e) {
            logger.error("【违约金请求类】批量处理违约金失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }

    }

    /**
     * 分页查询违约金计算结果
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByPage")
    public ResponseVo findByPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = businessLiquidatedDamagesService.findByPage(pageNo,pageSize,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname,userId);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【违约金请求类】分页查询异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    /**
     * 更新违约金计算结果，即在原来计算基础上更改违约金到期时间再次计算
     * 只有更新（修改）的违约金数据需要审核
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/update")
    public ResponseVo update(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            businessLiquidatedDamagesService.update(jsonObject,userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【违约金请求类】更新数据异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    /**
     * 分页查询审核未通过的违约金数据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByNotPassPage")
    public ResponseVo findByNotPassPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = businessLiquidatedDamagesService.findByNotByPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【违约金请求类】分页查询未通过异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询待审核的违约金数据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByWaitAuditPage")
    public ResponseVo findByWaitAuditPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = businessLiquidatedDamagesService.findByWaitAuditPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【违约金请求类】分页查询待审异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询待复审的违约金数据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByWaitReviewPage")
    public ResponseVo findByWaitReviewPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = businessLiquidatedDamagesService.findByWaitReviewPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【违约金请求类】分页查询待复审异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 分页查询已审核的违约金数据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByAlreadyAuditPage")
    public ResponseVo findByAlreadyAuditPage(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String villageCode = jsonObject.getString("villageCode");
            String regionCode = jsonObject.getString("regionCode");
            String buildingCode = jsonObject.getString("buildingCode");
            String unitCode = jsonObject.getString("unitCode");
            String roomCode = jsonObject.getString("roomCode");
            String mobilePhone = jsonObject.getString("mobilePhone");
            String idNumber = jsonObject.getString("idNumber");
            String surname = jsonObject.getString("surname");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = businessLiquidatedDamagesService.findByAlreadyAuditPage(pageNo,pageSize,userId,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【违约金请求类】分页查询已审核异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 审核通过处理接口
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/auditPass")
    public ResponseVo auditPass(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            businessLiquidatedDamagesService.auditPass(jsonObject.getInteger("id"),userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【违约金请求类】审核通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 复审通过处理接口
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/reviewPass")
    public ResponseVo reviewPass(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            businessLiquidatedDamagesService.reviewPass(jsonObject.getInteger("id"),userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【违约金请求类】复审通过处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 审核未通过处理接口
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/noPass")
    public ResponseVo noPass(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            businessLiquidatedDamagesService.noPass(jsonObject.getInteger("id"),jsonObject.getString("remarks"),userId);
            return ResponseVo.build("1000","请求成功");
        } catch (Exception e) {
            logger.error("【违约金请求类】审核拒绝处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 删除违约金信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/delete")
    public ResponseVo delete(@RequestBody JSONObject jsonObject){
        try {
            List<String> ids = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));
            List<Integer> idList = new ArrayList<>();
            for (String id:ids){
                idList.add(Integer.parseInt(id));
            }
            businessLiquidatedDamagesService.delete(idList);
            return ResponseVo.build("1000","请求成功");
        } catch (NumberFormatException e) {
            logger.error("【违约金请求类】删除违约金信息，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 批量计算违约金数据，通过选择业主信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/onekeyCalculationLiquidatedDamages")
    public ResponseVo onekeyCalculationLiquidatedDamages(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            List<RoomCodeAndMobilePhoneDTO> roomCodeAndMobilePhoneDTOS = JSONArray.parseArray(jsonObject.getJSONArray("roomCodeAndMobilePhoneDTOS").toJSONString(),RoomCodeAndMobilePhoneDTO.class);
            String startTime = jsonObject.getString("startTime");
            String endTime = jsonObject.getString("endTime");
            String liquidatedDamageDueTime = jsonObject.getString("liquidatedDamageDueTime");
            Map<String,Object> map = businessLiquidatedDamagesService.onekeyCalculationLiquidatedDamages(roomCodeAndMobilePhoneDTOS,startTime,endTime,liquidatedDamageDueTime,userId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【违约金请求类】批量处理违约金失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }
}
