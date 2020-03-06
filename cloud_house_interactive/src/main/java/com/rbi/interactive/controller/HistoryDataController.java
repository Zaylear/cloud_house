package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.service.FrontOfficeCashierService;
import com.rbi.interactive.service.HistoryDataService;
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
import java.util.Map;


@RequestMapping("/history_data")
@RestController
public class HistoryDataController {

    private final static Logger logger = LoggerFactory.getLogger(HistoryDataController.class);

    @Autowired
    HistoryDataService historyDataService;

    /**
     * 导入历史数据，通过表格导入
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/import")
    public ResponseVo importHistoryData(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1) {

            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = historyDataService.importHistoryData(file,userId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【收费请求类】批量导入历史数据失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常，ERROR：{}",e);
        }

    }



    /**
     * 导入累计历史数据，通过表格导入
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/import/cumulative")
    public ResponseVo importHistoryDataCumulative(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1) {

            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = historyDataService.importHistoryDataCumulative(file,userId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【收费请求类】批量导入历史数据失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常，ERROR：{}",e);
        }
    }




    /**
     * 导入历史数据，通过表格导入
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/importExclusiveParkingSpace")
    public ResponseVo importExclusiveParkingSpaceHistoryData(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1) {

            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = historyDataService.importExclusiveParkingSpaceHistoryData(file,userId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【收费请求类】批量导入专有车位历史数据失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常，ERROR：{}",e);
        }

    }

    /**
     * 导入专有车位历史数据累计，通过表格导入
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/importExclusiveParkingSpace/cumulative")
    public ResponseVo importExclusiveParkingSpaceHistoryDataCumulative(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1) {

            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = historyDataService.importExclusiveParkingSpaceHistoryDataCumulative(file,userId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【收费请求类】批量导入专有车位历史数据失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常，ERROR：{}",e);
        }

    }


    /**
     * 分页历史数据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByPage")
    public ResponseVo<PageData> findCouponPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String roomCode = jsonObject.getString("roomCode");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = historyDataService.findByPage(pageNo,pageSize,userId,roomCode);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【历史数据】分页查询异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }



    /**
     * 分页查询专有车位历史数据
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findExclusiveParkingSpaceHistoryDataByPage")
    public ResponseVo<PageData> findExclusiveParkingSpaceHistoryDataByPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String parkingSpaceCode = jsonObject.getString("parkingSpaceCode");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData pageData = historyDataService.findExclusiveParkingSpaceHistoryDataByPage(pageNo,pageSize,userId,parkingSpaceCode);

            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【历史数据】分页查询异常e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
