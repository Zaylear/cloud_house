package com.rbi.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.CityDAO;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.entity.edo.CityDO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    @Autowired
    CityDAO cityDAO;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    public PageData findByPage(int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<CityDO> cityDOS  = cityDAO.findByPage(pageNo,pageSize);
        int totalPage = 0;
        int count = cityDAO.findNum();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,cityDOS);
    }

    public void add(CityDO cityDO){
        cityDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        cityDAO.save(cityDO);
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
        idsDeleteDAO.deleteCity(ids);
    }

    public void update(CityDO cityDO){
        cityDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        cityDAO.saveAndFlush(cityDO);
    }
}
