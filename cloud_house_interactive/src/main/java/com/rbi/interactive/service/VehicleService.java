package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.VehicleInformationDO;
import com.rbi.interactive.utils.PageData;

import java.util.List;

public interface VehicleService {

    void leadingIn(JSONObject jsonObject,String userId);

    void add(JSONObject jsonObject,String userId);

    void update(VehicleInformationDO vehicleInformationDO);

    void deleteByIds(List<Integer> ids);

    PageData findByPage(int pageNum, int pageSize, String userId);

}
