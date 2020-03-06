package com.rbi.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.ProvinceDAO;
import com.rbi.admin.entity.edo.ProvinceDO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinceService {
    @Autowired
    ProvinceDAO provinceDAO;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    public PageData findByPage(int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<ProvinceDO> organizationDOS  = provinceDAO.findByPage(pageNo,pageSize);
        int totalPage = 0;
        int count = provinceDAO.findNum();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,organizationDOS);
    }

    public void add(ProvinceDO provinceDO){
        provinceDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        provinceDAO.save(provinceDO);
    }

    public void deleteByIds(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("id");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "("+str+")";
        idsDeleteDAO.deleteProvince(ids);
    }

    public void update(ProvinceDO provinceDO){
        provinceDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        provinceDAO.saveAndFlush(provinceDO);
    }

}
