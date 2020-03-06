package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ReportDO;
import com.rbi.interactive.service.ReportService;
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
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final static Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    ReportService reportService;

    /**
     * 查询报表类型
     * @return
     */
    @PostMapping("/findReport")
    public ResponseVo<List<ReportDO>> findReport(){
        try {
            List<ReportDO> reportDOS = reportService.findReport();
            return ResponseVo.build("1000","请求成功",reportDOS);
        }catch (Exception e){
            logger.error("【报表请求类】查询报表名称失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败！");
        }
    }

    /**
     * 根据报表编号查询报表设置详情
     * @param jsonObject
     * @return
     */
    @PostMapping("/findCriteriaByReportCode")
    public ResponseVo<Map<String,Object>> findCriteriaByReportCode(@RequestBody JSONObject jsonObject){
        try {
            String parentCode = jsonObject.getString("code");
            Map<String,Object> map = reportService.findCriteriaByReportCode(parentCode);
            return ResponseVo.build("1000","请求成功",map);
        }catch (Exception e){
            logger.error("【报表请求类】查询报表条件数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败！");
        }
    }

    /**
     * 分页查询报表内容
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findDataByPage")
    public ResponseVo<PageData> findDataByPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String code = jsonObject.getString("code");
            String title = jsonObject.getString("title");
            Integer pageNo = jsonObject.getInteger("pageNo");
            Integer pageSize = jsonObject.getInteger("pageSize");
            String startTime = jsonObject.getString("startTime");
            String endTime = jsonObject.getString("endTime");
            JSONArray displayResults = jsonObject.getJSONArray("displayResults");
            JSONArray queryCriteria = jsonObject.getJSONArray("queryCriteria");

            System.out.println(displayResults);
            System.out.println(queryCriteria);

            PageData pageData = reportService.findDataByPage(code,title,displayResults,queryCriteria,pageNo,pageSize,userId,startTime,endTime);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【报表请求类】根据条件分页查询数据失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 生成报表并导出
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/generatingReport")
    public ResponseVo<String> generatingReport(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String code = jsonObject.getString("code");
            String title = jsonObject.getString("title");
            String startTime = jsonObject.getString("startTime");
            String endTime = jsonObject.getString("endTime");
            JSONArray displayResults = jsonObject.getJSONArray("displayResults");
            JSONArray queryCriteria = jsonObject.getJSONArray("queryCriteria");
            String reportPath = reportService.generatingReport(code,title,displayResults,queryCriteria,userId,startTime,endTime);
            return ResponseVo.build("1000","请求成功",reportPath);
        }catch (IOException e){
            logger.error("【报表请求类】生成报表IO流处理异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }catch (Exception e) {
            logger.error("【报表请求类】服务器处理异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
