package com.rbi.interactive.service.impl.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.dao.IReportDAO;
import com.rbi.interactive.dao.ReportDAO;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.entity.OriginalBillDO;
import com.rbi.interactive.entity.ReportDO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.service.ReportService;
import com.rbi.interactive.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private final static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired(required = false)
    IReportDAO iReportDAO;

    @Autowired
    ReportDAO reportDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Override
    public List<ReportDO> findReport() {
        List<ReportDO> reportDOS = reportDAO.findByParentCode("0");
        return reportDOS;
    }

    @Override
    public Map<String, Object> findCriteriaByReportCode(String parentCode) {
        List<ReportDO> displayResults = reportDAO.findByParentCodeAndParamType(parentCode,1);
        List<ReportDO> queryCriteria = reportDAO.findByParentCodeAndParamType(parentCode,2);
        Map<String, Object> map = new HashMap<>();
        map.put("displayResults",displayResults);
        map.put("queryCriteria",queryCriteria);
        return map;
    }

    @Override
    public PageData findDataByPage(String code, String title, JSONArray displayResults, JSONArray queryCriteria, Integer pageNum, Integer pageSize, String userId,String startTime,String endTime) {

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        if ("收款汇总表".equals(title)){
            String sql0 = "select ";
            String sql1 = " from original_bill where ";
            String str;
            String key = "";
            for (int i = 0; i < displayResults.size();i++){
                str = displayResults.get(i).toString();
                key = Tools.analysisStr(str,":",1).replaceAll("\"","").replaceAll("\\{","");
                sql0+= key+",";
            }
            sql0.substring(0,sql0.length()-1);
            for (int i = 0;i < queryCriteria.size();i++){
                str = queryCriteria.get(i).toString();
                key = Tools.analysisStr(str,":",1).replaceAll("\"","").replaceAll("\\{","");
                sql1+= key+"='"+queryCriteria.getJSONObject(i).getString(key)+"' And ";
            }
            int pageNo = (pageNum-1)*pageSize;
            String sql = sql0.substring(0,sql0.length()-1)+sql1+"organization_id = '"+organizationId+"' and idt>'"+startTime+"' and idt<'"+endTime+"' ORDER BY user_id limit "+pageNo+","+pageSize;
            String sqlCount = "select count(*)"+sql1+"organization_id = '"+organizationId+"'";
            logger.info("【报表服务类】分页查询报表数据失败，DATA：{}，生成SQL语句：{},{}",title,sql,sqlCount);

            int count = iReportDAO.findDataCount(sqlCount);
            List<OriginalBillDO> reportDOS = iReportDAO.findDataByPage(sql);
            int totalPage;
            if (count%pageSize==0){
                totalPage = count/pageSize;
            }else {
                totalPage = count/pageSize+1;
            }
            return new PageData(pageNum,pageSize,totalPage,count,reportDOS);
        }
        return null;
    }

    @Override
    public String generatingReport(String code, String title, JSONArray displayResults, JSONArray queryCriteria, String userId,String startTime,String endTime) throws IOException {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        if ("收款汇总表".equals(title)){
            String sql0 = "select ";
            String sql1 = " from original_bill where ";
            String str;
            String key = "";
            List<String> keys = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            for (int i = 0; i < displayResults.size();i++){
                str = displayResults.get(i).toString();
                key = Tools.analysisStr(str,":",1).replaceAll("\"","").replaceAll("\\{","");
                keys.add(key);
                titles.add(displayResults.getJSONObject(i).getString(keys.get(i)));
                sql0+= key+",";
            }
            sql0.substring(0,sql0.length()-1);
            for (int i = 0;i < queryCriteria.size();i++){
                str = queryCriteria.get(i).toString();
                key = Tools.analysisStr(str,":",1).replaceAll("\"","").replaceAll("\\{","");
                sql1+= key+"='"+queryCriteria.getJSONObject(i).getString(key)+"' And ";
            }
            String sql = sql0.substring(0,sql0.length()-1)+sql1+"organization_id = '"+organizationId+"' and idt>'"+startTime+"' and idt<'"+endTime+"' ORDER BY user_id";
            logger.info("【报表服务类】分页查询报表数据失败，DATA：{}，生成SQL语句：{}",title,sql);
            List<OriginalBillDO> originalBillDOS = iReportDAO.findDataByPage(sql);
            List<Map<String,Object>> mapList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject();
            for (OriginalBillDO originalBillDO:originalBillDOS){
                jsonObject = JSONObject.parseObject(JSONObject.toJSON(originalBillDO).toString());
                Map<String,Object> map = new HashMap<>();
                for (int i = 0; i < keys.size();i++){
                    map.put(displayResults.getJSONObject(i).getString(keys.get(i)),jsonObject.get(Tools.lineToHump(keys.get(i))));
                }
                mapList.add(map);
            }

            ExcelUtils.writeExcel(Constants.REPORT_FILE_PATH+ DateUtil.date(DateUtil.YEAR_MONTH_DAY)+title+".xlsx","sheet",titles,mapList);

            return Constants.UBUNTU_REPORT_FILE_PATH+ DateUtil.date(DateUtil.YEAR_MONTH_DAY)+title+".xlsx";
        }
        return null;
    }

    @Override
    public String generatingCollectionSummaryReport(JSONObject jsonObject,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        //处理当天时间或时间段时间
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        

        String time = DateUtil.date(DateUtil.YEAR_MONTH_DAY);//获取当前时间


        return null;
    }

    @Override
    public String generatingExpenseVerificationForm(String startTime,String endTime,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();



        return null;
    }


}
