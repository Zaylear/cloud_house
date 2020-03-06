package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ReportDO;
import com.rbi.interactive.utils.PageData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<ReportDO> findReport();

    Map<String,Object> findCriteriaByReportCode(String parentCode);

    PageData findDataByPage(String code, String title, JSONArray displayResults, JSONArray queryCriteria, Integer pageNum, Integer pageSize, String userId,String startTime,String endTime);

    String generatingReport(String code, String title, JSONArray displayResults, JSONArray queryCriteria, String userId,String startTime,String endTime) throws IOException;

    //生成收款汇总表
    String generatingCollectionSummaryReport(JSONObject jsonObject, String userId);

    //生成费用核销表
    String generatingExpenseVerificationForm(String startTime,String endTime,String userId);
}
