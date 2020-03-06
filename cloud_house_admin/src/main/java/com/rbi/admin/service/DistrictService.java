package com.rbi.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.DistrictDAO;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.entity.edo.DistrictDO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictService {
    @Autowired
    DistrictDAO districtDAO;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    public PageData findByPage(int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<DistrictDO> districtDOS  = districtDAO.findByPage(pageNo,pageSize);
        int totalPage = 0;
        int count = districtDAO.findNum();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,districtDOS);
    }

    public void add(DistrictDO districtDO){
        districtDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        districtDAO.save(districtDO);
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
        idsDeleteDAO.deleteDistrict(ids);
    }

    public void update(DistrictDO districtDO){
        districtDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        districtDAO.saveAndFlush(districtDO);
    }
}
