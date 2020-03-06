package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import com.rbi.interactive.utils.PageData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


public interface ParkingSpaceManagementService {
    String add(JSONObject json,String userId);

    JSONArray findParkingSpaceCode(String userId);

    Map<String, Object> excelImport(MultipartFile file, String userId) throws IOException;

    void update(ParkingSpaceManagementDO parkingSpaceManagementDO);

    void deleteById(JSONArray jsonArray);

    PageData findFirstByPage(int pageNo, int pageSize, String organizationId);

    PageData findVillage(String villageCode, int pageNum, int pageSize);

    PageData findRegion(String regionCode,int pageNum, int pageSize);

    PageData findBuilding(String buildingCode,int pageNum, int pageSize);

    PageData findParkingSpaceCode(String ParkinSpaceCode,int pageNum, int pageSize);


}
